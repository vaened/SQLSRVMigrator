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
package pe.org.incn.sqlsrvmigrator.database;

import java.lang.reflect.InvocationTargetException;
import pe.org.incn.sqlsrvmigrator.database.components.Model;
import java.util.List;
import java.util.stream.Collectors;
import pe.org.incn.sqlsrvmigrator.database.components.Column;
import pe.org.incn.sqlsrvmigrator.database.components.Value;

public abstract class CollectionBuilder<T> {

    public abstract List<Model> mapTableTo(T element) throws MappingException;

    protected Model buildModel(Class<? extends Model> clazz, List<Column> columns, List<Value> values)
            throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        Model model = clazz.getConstructor(List.class).newInstance(values);
        List<Column> mutations = columns.stream().filter(column -> !column.mutation().isEmpty()).collect(Collectors.toList());
        
        for (Column column : mutations) {
            Value oldValue = model.getValueAttribute(column.source());
            Object transformedValue = clazz.getMethod(column.mutation(), Value.class).invoke(model, oldValue);
            model.setValueAttribute(column.source(), transformedValue);
        }
        
        return model;
    }
}
