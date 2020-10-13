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

import java.util.ArrayList;
import java.util.List;
import pe.org.incn.sqlsrvmigrator.database.CollectionBuilder;
import pe.org.incn.sqlsrvmigrator.database.MappingException;
import pe.org.incn.sqlsrvmigrator.database.components.Model;
import pe.org.incn.sqlsrvmigrator.database.Compiler;

public class DBFCompiler<T extends Model> extends Compiler {

    private final List<Class<T>> models;

    public DBFCompiler(String text, List<Class<T>> models) {
        super(text);
        this.models = models;
    }

    protected CollectionBuilder<Class<T>> collectionBuilder() {
        DBFConnection cnn = new DBFConnection();
        return new DBFCollectionBuilder(cnn, new DBFAttributeCaster());
    }

    @Override
    public List<Model> build() throws MappingException {
        List<Model> mapped = new ArrayList<>();
        for (Class<T> model : models) {
            mapped.addAll(collectionBuilder().mapTableTo(model));
        }
        return mapped;
    }
}
