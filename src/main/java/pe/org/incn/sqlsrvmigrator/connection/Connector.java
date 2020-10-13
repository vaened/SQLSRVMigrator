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
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import pe.org.incn.sqlsrvmigrator.config.Database;

public class Connector {

    public Connection getConnection(Database database) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(getConnectionUrl(database), database.getUsername(), database.getPassword());
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Error Trace in getConnection() : " + ex.getMessage());
            Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private String getConnectionUrl(Database database) {
        return String.format("jdbc:sqlserver://%s:%s; databaseName = %s;", database.getServer(), database.getPort(), database.getName());
    }
}
