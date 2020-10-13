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
import java.util.Date;
import pe.org.incn.sqlsrvmigrator.Container;
import pe.org.incn.sqlsrvmigrator.config.Config;
import pe.org.incn.sqlsrvmigrator.database.components.Table;

public class Follower {

    private final Config config;

    private final Reader reader;

    public Follower(Reader reader) {
        this.config = Container.getConfig();
        this.reader = reader;
    }

    public void leaveTrace(Connection connection, Table table) throws SQLException {
        if (reader.hasMigration(connection, table)) {
            updateCrumb(connection, table);
        } else {
            createCrumb(connection, table);
        }
    }

    private void updateCrumb(Connection connection, Table table) throws SQLException {
        String queryString = String.format("UPDATE %s set updated_at= ? where table_name = ?", config.table());
        try (PreparedStatement statement = connection.prepareStatement(queryString)) {
            statement.setTimestamp(1, getCurrentTimestamp());
            statement.setString(2, getSQLTable(table));
            statement.execute();
        }
    }

    private void createCrumb(Connection connection, Table table) throws SQLException {
        String queryString = String.format("INSERT INTO %s (table_name, updated_at) values(?,?)", config.table());
        try (PreparedStatement statement = connection.prepareStatement(queryString)) {
            statement.setString(1, getSQLTable(table));
            statement.setTimestamp(2, getCurrentTimestamp());
            statement.execute();
        }
    }

    private Timestamp getCurrentTimestamp() {
        return new Timestamp(new Date().getTime());
    }

    private String getSQLTable(Table table) {
        return table.destination().isEmpty() ? table.location() : table.destination();
    }
}
