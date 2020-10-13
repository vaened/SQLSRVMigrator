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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import pe.org.incn.sqlsrvmigrator.connection.statements.Filler;
import pe.org.incn.sqlsrvmigrator.connection.statements.InsertQueryBuilder;
import pe.org.incn.sqlsrvmigrator.connection.statements.MigrationException;
import pe.org.incn.sqlsrvmigrator.database.components.Table;
import pe.org.incn.sqlsrvmigrator.database.components.Model;
import pe.org.incn.sqlsrvmigrator.database.Orderable;
import pe.org.incn.sqlsrvmigrator.database.components.Value;

public class Dumper {

    private final int limitBatch = 1000;
    private final InsertQueryBuilder query;
    private final Follower follower;
    private final Filler filler;

    public Dumper(InsertQueryBuilder query, Follower follower, Filler filler) {
        this.query = query;
        this.follower = follower;
        this.filler = filler;
    }

    public Integer execute(Connection connection, Table table, List<Model> values) throws MigrationException {
        validateConnection(connection);
        if (values.isEmpty()) {
            return 0;
        }
        return insert(connection, table, values);
    }

    private int insert(Connection connection, Table table, List<Model> models) throws MigrationException {
        int currentRecord = 0;
        String queryString = query.getQueryString(table);

        try (PreparedStatement statement = connection.prepareStatement(queryString)) {
            connection.setAutoCommit(false);
            for (Model model : models) {
                currentRecord++;
                setStatementValues(statement, model);
                if (isBatchLimitReached(currentRecord, models.size())) {
                    statement.executeBatch();
                }
            }

            follower.leaveTrace(connection, table);
            connection.commit();
        } catch (SQLException ex) {
            currentRecord = 0;
            throwMigrationException(table, ex);
        }

        return currentRecord;
    }

    private boolean isBatchLimitReached(int currentRecord, int totalModelSize) {
        return currentRecord == totalModelSize || currentRecord % limitBatch == 0;
    }

    private Model getLatestModel(List<Model> models) {
        return models.stream().sorted().collect(Collectors.toList()).get(models.size() - 1);
    }

    private void setStatementValues(PreparedStatement statement, Model model) throws SQLException {
        int parameter = 1;
        for (Value value : model.values()) {
            filler.setValue(value, statement, parameter++);
        }
        statement.setString(parameter++, model.getUUID());
        statement.addBatch();
    }

    private void throwMigrationException(Table table, SQLException ex) throws MigrationException {
        String message = String.format("Couldn't execute data source migration [%s] to table [%s]", table.location(), table.destination());
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
