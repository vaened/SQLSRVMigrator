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
import pe.org.incn.sqlsrvmigrator.database.Table;

public class Truncator {

    public int truncate(Connection connection, Table table) {
        try (PreparedStatement statement = connection.prepareStatement(createQueryString(table))) {
            return statement.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("ERROR: migration table was not found -" + ex.getMessage());
            return 0;
        }
    }

    private String createQueryString(Table table) {
        String name = table.destination().isEmpty() ? table.location() : table.destination();
        return String.format("truncate table %s", name);
    }
}
