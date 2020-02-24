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
import java.sql.Timestamp;
import pe.org.incn.sqlsrvmigrator.config.Config;
import pe.org.incn.sqlsrvmigrator.database.Model;
import pe.org.incn.sqlsrvmigrator.database.Orderable;
import pe.org.incn.sqlsrvmigrator.database.Table;

public class Follower {

    private final Config config;

    private final Reader reader;

    public Follower(Config config, Reader reader) {
        this.config = config;
        this.reader = reader;
    }

    public void leaveTrace(Connection connection, Model lastModel) throws SQLException {
        Class<? extends Model> clazz = lastModel.getClass();
        Table table = clazz.getAnnotation(Table.class);
        Migration migration = getMigration(connection, table);

        if (migration.isEmpty()) {
            createCrumb(connection, (Orderable) lastModel, table);
            return;
        }

        updateCrumb(connection, (Orderable) lastModel, table);
    }

    private void updateCrumb(Connection connection, Orderable lastModel, Table table) throws SQLException {
        String queryString = String.format("UPDATE %s set value = ?, updated_at= ? where table_name = ?", config.table());
        try (PreparedStatement statement = connection.prepareStatement(queryString)) {
            statement.setString(1, lastModel.getOrdererValue());
            statement.setTimestamp(2, getCurrentTimestamp());
            statement.setString(3, getSQLTable(table));
            statement.execute();
        }
    }

    private void createCrumb(Connection connection, Orderable lastModel, Table table) throws SQLException {
        String queryString = String.format("INSERT INTO %s (table_name, value, updated_at) values(?,?,?)", config.table());
        try (PreparedStatement statement = connection.prepareStatement(queryString)) {
            statement.setString(1, getSQLTable(table));
            statement.setString(2, lastModel.getOrdererValue());
            statement.setTimestamp(3, getCurrentTimestamp());
            statement.execute();
        }
    }

    private Migration getMigration(Connection connection, Table table) {
        return reader.migration(connection, table);
    }

    private Timestamp getCurrentTimestamp() {
        return new Timestamp(new java.util.Date().getTime());
    }

    private String getSQLTable(Table table) {
        return table.destination().isEmpty() ? table.location() : table.destination();
    }
}
