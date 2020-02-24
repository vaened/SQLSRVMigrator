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

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
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
import pe.org.incn.sqlsrvmigrator.connection.Migration;
import pe.org.incn.sqlsrvmigrator.connection.Dumper;
import pe.org.incn.sqlsrvmigrator.connection.Reader;
import pe.org.incn.sqlsrvmigrator.connection.Truncator;
import pe.org.incn.sqlsrvmigrator.connection.statements.MigrationException;
import pe.org.incn.sqlsrvmigrator.database.Builder;
import pe.org.incn.sqlsrvmigrator.database.Orderable;
import pe.org.incn.sqlsrvmigrator.database.Table;
import pe.org.incn.sqlsrvmigrator.database.Model;

public class Management {

    private final Config config;

    private final BuilderFactory factory;
    private final Connector connector;
    private final Dumper dumper;
    private final Presenter presenter;
    private final Reader reader;
    private final Truncator truncator;

    public Management(
            Config config,
            BuilderFactory factory,
            Connector connector,
            Dumper dumper,
            Presenter presenter,
            Reader reader,
            Truncator truncator) {
        this.config = config;
        this.factory = factory;
        this.connector = connector;
        this.dumper = dumper;
        this.presenter = presenter;
        this.reader = reader;
        this.truncator = truncator;
    }

    void migrate(List<Class< ? extends Model>> classes) {
        classes.forEach(clazz -> migrate(clazz));
    }

    public <T extends Model> void migrate(Class<T> clazz) {
        try {
            Iterator<Entry<Class<T>, List<T>>> iterator = mapModelToGroupByModelType(clazz);
            while (iterator.hasNext()) {
                Entry<Class<T>, List<T>> entry = iterator.next();
                Table table = entry.getKey().getAnnotation(Table.class);
                Source source = config.getSource(table.connection());

                if (!source.isEnabled()) {
                    showMigrationWasDisabledMessage(table);
                    return;
                }

                Database database = getDatabaseFrom(table);
                drawTable(table, execute(database, entry));
            }
        } catch (InstantiationException | IllegalAccessException | FileNotFoundException ex) {
            Logger.getLogger(Management.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Management.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MigrationException ex) {
            Logger.getLogger(Management.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchFieldException ex) {
            Logger.getLogger(Management.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private <T extends Model> int execute(Database database, Entry<Class<T>, List<T>> entry) throws InstantiationException, IllegalAccessException, IllegalArgumentException, MigrationException, NoSuchFieldException {
        try (Connection connection = connector.getConnection(database)) {
            List<T> collection = getModelsToMigrate(connection, entry);
            return dumper.execute(connection, entry.getKey(), collection);
        } catch (SQLException ex) {
            throw new MigrationException(ex.getMessage());
        }
    }

    private Database getDatabaseFrom(Table table) {
        Source source = config.getSource(table.connection());
        return config.getDatabase(source);
    }

    private <T extends Model> Iterator<Entry<Class<T>, List<T>>> mapModelToGroupByModelType(Class<T> clazz) throws InstantiationException, IllegalAccessException, FileNotFoundException {
        List<T> models = getModelsFrom(clazz);
        return models.stream().collect(Collectors.groupingBy(w -> (Class<T>) w.getClass())).entrySet().iterator();
    }

    private <T extends Model> List<T> getModelsFrom(Class<T> clazz) throws InstantiationException, IllegalAccessException, FileNotFoundException {
        Builder<T> builder = factory.create(clazz);
        return builder.mapTableTo(clazz);
    }

    private <T extends Model> List<T> getModelsToMigrate(Connection connection, Entry<Class<T>, List<T>> entry) throws InstantiationException, IllegalAccessException {
        Table table = entry.getKey().getAnnotation(Table.class);
        T model = entry.getKey().newInstance();

        if (IsOrdereableModel(model)) {
            Migration migration = getMigrationModel(connection, table);
            if (!migration.isEmpty()) {
                return filterUnmigratedModels(migration, entry.getValue());
            }
        }

        truncator.truncate(connection, table);
        return entry.getValue();
    }

    private <T extends Model> List<T> filterUnmigratedModels(Migration migration, List<T> models) throws InstantiationException, IllegalAccessException {
        return models.stream().sorted()
                .filter(model -> ((Orderable) model).isUnregistered(migration))
                .collect(Collectors.toList());
    }

    private <T extends Model> boolean IsOrdereableModel(T model) {
        return model instanceof Orderable;
    }

    private Migration getMigrationModel(Connection connection, Table table) {
        return reader.migration(connection, table);
    }

    private void showMigrationWasDisabledMessage(Table table) {
        System.err.println(String.format("Migration for source [%s.%s] was disabled", table.connection(), table.location()));
    }

    private void drawTable(Table table, int totalRecordsAffected) {
		this.presenter.render(table, totalRecordsAffected);
	}
}
