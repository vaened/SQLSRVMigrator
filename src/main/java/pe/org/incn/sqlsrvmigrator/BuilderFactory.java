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

import pe.org.incn.sqlsrvmigrator.config.Config;
import pe.org.incn.sqlsrvmigrator.config.Source;
import pe.org.incn.sqlsrvmigrator.database.Builder;
import pe.org.incn.sqlsrvmigrator.database.Table;
import pe.org.incn.sqlsrvmigrator.database.dbf.DBFAttributeCaster;
import pe.org.incn.sqlsrvmigrator.database.dbf.DBFCollectionBuilder;
import pe.org.incn.sqlsrvmigrator.database.dbf.DBFConnection;
import pe.org.incn.sqlsrvmigrator.database.Model;
import pe.org.incn.sqlsrvmigrator.database.document.FolderCollectionBuilder;
import pe.org.incn.sqlsrvmigrator.database.document.FolderConnection;

/**
 *
 * @author enea dhack
 */
public class BuilderFactory {

    private final Config config;

    public BuilderFactory(Config config) {
        this.config = config;
    }

    public <T extends Model> Builder<T> create(Class<T> clazz) throws UnsupportedOperationException {
        Table table = clazz.getAnnotation(Table.class);
        Source source = config.getSource(table.connection());
        switch (source.getDriver()) {
            case DBF:
                return this.createDBFCollectionBuilder();
            default:
                throw new UnsupportedOperationException(String.format("Unsupported Driver: %s", source.getDriver()));
        }
    }

    private DBFCollectionBuilder createDBFCollectionBuilder() {
        DBFConnection cnn = new DBFConnection(config);
        return new DBFCollectionBuilder(cnn, new DBFAttributeCaster());
    }

    private FolderCollectionBuilder createFolderCollectionBuilder() {
        FolderConnection cnn = new FolderConnection(config);
        return new FolderCollectionBuilder(cnn);
    }

}
