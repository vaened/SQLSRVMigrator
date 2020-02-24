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
package pe.org.incn.sqlsrvmigrator.connection;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import pe.org.incn.sqlsrvmigrator.connection.statements.Filler;
import pe.org.incn.sqlsrvmigrator.connection.statements.InsertQueryBuilder;
import pe.org.incn.sqlsrvmigrator.connection.statements.MigrationException;
import pe.org.incn.sqlsrvmigrator.database.Table;
import pe.org.incn.sqlsrvmigrator.database.Model;
import pe.org.incn.sqlsrvmigrator.database.Orderable;

public class Dumper<T extends Model> {

    private final int limitBatch = 1000;
    private final InsertQueryBuilder query;
    private final Follower follower;
    private final Filler filler;

    public Dumper(InsertQueryBuilder query, Follower follower, Filler filler) {
        this.query = query;
        this.follower = follower;
        this.filler = filler;
    }

    public Integer execute(Connection connection, Class<T> clazz, List<T> values) throws IllegalArgumentException, IllegalAccessException, MigrationException, NoSuchFieldException {
        validateConnection(connection);
        if (values.isEmpty()) {
            return 0;
        }
        return insert(connection, clazz, values);
    }

    private int insert(Connection connection, Class<T> clazz, List<T> models) throws IllegalArgumentException, IllegalAccessException, MigrationException, NoSuchFieldException {
        int currentRecord = 0;
        Field[] fields = getFields(clazz);
        String queryString = createQueryString(clazz, fields);

        try (PreparedStatement statement = connection.prepareStatement(queryString)) {
            connection.setAutoCommit(false);
            for (T model : models) {
                setStatementValues(fields, statement, model);
                currentRecord++;
                if (currentRecord % limitBatch == 0 || currentRecord == models.size()) {
                    statement.executeBatch();
                }
            }

            if (models.get(0) instanceof Orderable) {
                follower.leaveTrace(connection, getLatestModel(models));
            }

            connection.commit();
        } catch (SQLException ex) {
            currentRecord = 0;
            throwMigrationException(clazz, ex);
        }

        return currentRecord;
    }

    private Model getLatestModel(List<T> models) {
        return models.stream().sorted().collect(Collectors.toList()).get(models.size() - 1);
    }

    private Field[] getFields(Class<T> clazz) throws NoSuchFieldException {
        List<Field> arrayListfields = new ArrayList<Field>(Arrays.asList(clazz.getDeclaredFields()));
        arrayListfields.add(Model.class.getField("deleted"));
        return arrayListfields.toArray(new Field[arrayListfields.size()]);
    }

    private void setStatementValues(Field[] fields, PreparedStatement statement, T model) throws SQLException, IllegalArgumentException, IllegalAccessException {
        int parameter = 1;
        for (Field field : fields) {
            filler.setValue(model, field, statement, parameter++);
        }
        statement.addBatch();
    }

    private String createQueryString(Class<T> clazz, Field[] fields) {
        Table table = clazz.getAnnotation(Table.class);
        return this.query.getQueryString(table, fields);
    }

    private void throwMigrationException(Class<T> clazz, SQLException ex) throws MigrationException {
        Table table = clazz.getDeclaredAnnotation(Table.class);
        String message = String.format("Couldn't execute the migration of the [%s] table", table.location());
        throw new MigrationException(String.format("%s: %s", message, ex.getMessage()), ex);
    }

    private void validateConnection(Connection connection) throws MigrationException {
        try {
            if (connection.isClosed()) {
                throw new MigrationException("The connection has been closed");
            }
        } catch (SQLException ex) {
            throw new MigrationException("There was a problem with the connection");
        }
    }
}
