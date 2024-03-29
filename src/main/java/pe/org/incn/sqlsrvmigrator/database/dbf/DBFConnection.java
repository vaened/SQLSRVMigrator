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
package pe.org.incn.sqlsrvmigrator.database.dbf;

import com.linuxense.javadbf.DBFReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import pe.org.incn.sqlsrvmigrator.config.Config;
import pe.org.incn.sqlsrvmigrator.database.FileConnection;
import pe.org.incn.sqlsrvmigrator.database.Table;

/**
 *
 * @author enea dhack
 */
public class DBFConnection extends FileConnection {

    public DBFConnection(Config config) {
        super(config);
    }

    public DBFReader getTableFrom(Table table) throws FileNotFoundException {
        return this.toTable(new FileInputStream(this.getFilePath(table)));
    }

    protected DBFReader toTable(InputStream input) {
        return new DBFReader(input);
    }

    public void close(DBFReader reader) {
        reader.close();
    }

    private String getFilePath(Table table) {
        return getSourceFrom(table).getPath().concat(table.location() + ".dbf");
    }
}
