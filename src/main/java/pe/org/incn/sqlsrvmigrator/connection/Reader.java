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
import java.sql.ResultSet;
import java.sql.SQLException;
import pe.org.incn.sqlsrvmigrator.database.Table;

public class Reader {

    public Migration migration(Connection connection, Table table) {
        Migration migration = new Migration();

        try (PreparedStatement statement = connection.prepareStatement(createQueryString())) {

            statement.setString(1, table.destination().isEmpty() ? table.location() : table.destination());
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                migration.setTable(result.getString("table_name"));
                migration.setValue(result.getString("value"));
            }
        } catch (SQLException ex) {
            System.err.println("ERROR: migration table was not found -" + ex.getMessage());
        }

        return migration;
    }

    private String createQueryString() {
        return "select table_name, value from SQLSRV_migrations where table_name=?";
    }
}
