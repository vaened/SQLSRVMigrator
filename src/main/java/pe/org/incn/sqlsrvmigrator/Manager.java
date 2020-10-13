/*
 * The MIT License
 *
 * Copyright 2020 enea dhack.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package pe.org.incn.sqlsrvmigrator;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import pe.org.incn.sqlsrvmigrator.config.Config;
import pe.org.incn.sqlsrvmigrator.config.Database;
import pe.org.incn.sqlsrvmigrator.config.Source;
import pe.org.incn.sqlsrvmigrator.connection.Connector;
import pe.org.incn.sqlsrvmigrator.connection.Dumper;
import pe.org.incn.sqlsrvmigrator.connection.Migration;
import pe.org.incn.sqlsrvmigrator.connection.Reader;
import pe.org.incn.sqlsrvmigrator.connection.Truncator;
import pe.org.incn.sqlsrvmigrator.connection.statements.MigrationException;
import pe.org.incn.sqlsrvmigrator.database.MappingException;
import pe.org.incn.sqlsrvmigrator.database.Orderable;
import pe.org.incn.sqlsrvmigrator.database.components.Model;
import pe.org.incn.sqlsrvmigrator.database.components.Table;
import pe.org.incn.sqlsrvmigrator.database.Compiler;

public class Manager<T extends Model> {

    private final Config config;
    private final Connector connector;
    private final Dumper dumper;
    private final Reader reader;
    private final Truncator truncator;

    public Manager(Dumper dumper, Reader reader, Truncator truncator) {
        this.config = Container.getConfig();
        this.connector = Container.getConnector();
        this.dumper = dumper;
        this.reader = reader;
        this.truncator = truncator;
    }

    public boolean migrate(Compiler compiler) {
        try {
            Decorator decorator = Decorator.create(compiler.getText());

            List<Model> models = compiler.build();
            List<Result> results = migrate(models);

            results.forEach(result -> {
                if (result.isSuccess()) {
                  //  decorator.add(result.getTable(), result.getTotalRecordsAffected());
                    return;
                }
               // decorator.setError(result.getTable().destination(), result.getMessage());
            });
            //decorator.render();
            return models.size() == results.size();

        } catch (MappingException | MigrationException ex) {
            Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public List<Result> migrate(List<Model> models) throws MappingException, MigrationException {
        List<Result> results = new ArrayList<>();

        try {
            Iterator<Entry<Class<Model>, List<Model>>> iterator = buildModelsGrouppingByType(models);
            while (iterator.hasNext()) {
                Entry<Class<Model>, List<Model>> entry = iterator.next();
                Table table = entry.getKey().getAnnotation(Table.class);
                Source source = config.getSource(table.connection());
                results.add(execute(source, table, entry.getValue()));
            }
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return results;
    }

    private Result execute(Source source, Table table, List<Model> models) throws InstantiationException, IllegalAccessException {
        if (!source.isEnabled()) {
            return Result.disabled(table, models.size());
        }

        Database database = config.getDatabase(source);
        try (Connection connection = connector.getConnection(database)) {
            List<Model> collection = getModelsToMigrate(connection, table, models);
            int totalRecordsAffected = dumper.execute(connection, table, collection);
            return Result.migrated(table, totalRecordsAffected, models.size());
        } catch (SQLException | MigrationException ex) {
            Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
            return Result.error(table, models.size(), ex.getMessage());
        }
    }

    private List<Model> getModelsToMigrate(Connection connection, Table table, List<Model> models) throws InstantiationException, IllegalAccessException {
        if (table.orderable()) {
            Migration migration = getMigrationModel(connection, table);

            return filterUnmigratedModels(migration, models);
        }

        truncator.truncate(connection, table);
        return models;
    }

    private Iterator<Entry<Class<Model>, List<Model>>> buildModelsGrouppingByType(List<Model> models) throws MappingException {
        return models.stream().collect(Collectors.groupingBy(model -> (Class<Model>) model.getClass())).entrySet().iterator();
    }

    private List<Model> filterUnmigratedModels(Migration migration, List<Model> models) throws InstantiationException, IllegalAccessException {
        return models.stream().sorted()
                .filter(model -> ((Orderable) model).isUnregistered(migration))
                .collect(Collectors.toList());
    }

    private Migration getMigrationModel(Connection connection, Table table) {
        return reader.migration(connection, table);
    }

    private void showMigrationWasDisabledMessage(Table table) {
        System.err.println(String.format("Migration for source [%s.%s] was disabled", table.connection(), table.location()));
    }
}
