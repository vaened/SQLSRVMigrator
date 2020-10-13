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
import com.linuxense.javadbf.DBFRow;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import pe.org.incn.sqlsrvmigrator.database.components.Column;
import pe.org.incn.sqlsrvmigrator.database.CollectionBuilder;
import pe.org.incn.sqlsrvmigrator.database.MappingException;
import pe.org.incn.sqlsrvmigrator.database.components.Table;
import pe.org.incn.sqlsrvmigrator.database.components.Model;
import pe.org.incn.sqlsrvmigrator.database.components.Value;

public class DBFCollectionBuilder<T extends Model> extends CollectionBuilder<Class<T>> {

    private final DBFConnection connection;
    private final DBFAttributeCaster caster;

    public DBFCollectionBuilder(DBFConnection connection, DBFAttributeCaster caster) {
        this.connection = connection;
        this.caster = caster;
    }

    @Override
    public List<Model> mapTableTo(Class<T> element) throws MappingException {
        Table table = element.getAnnotation(Table.class);
        List<Column> columns = Arrays.asList(table.columns());

        try (DBFReader reader = connection.getTableFrom(table)) {
            List<Model> collection = new ArrayList<>();
            DBFRow row;

            while ((row = reader.nextRow()) != null) {
                T instance = createModelInstance(element, columns, row);
                instance.setUUID(UUID.randomUUID().toString());
                collection.add(instance);
            }

            return collection;
        } catch (NoSuchMethodException
                | SecurityException
                | InstantiationException
                | IllegalArgumentException
                | IllegalAccessException
                | FileNotFoundException
                | InvocationTargetException ex) {
            throw new MappingException("It's not possible to map the models", ex);
        }
    }

    protected T createModelInstance(Class<T> element, List<Column> columns, DBFRow row)
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        List<Value> values = buildValuesFromDBF(columns, row);
        return (T) buildModel(element, columns, values);
    }

    protected List<Value> buildValuesFromDBF(List<Column> columns, DBFRow row) {
        return columns.stream().map(column -> {
            Object value = caster.cast(row, column);
            return new Value(column, value);
        }).collect(Collectors.toList());
    }
}
