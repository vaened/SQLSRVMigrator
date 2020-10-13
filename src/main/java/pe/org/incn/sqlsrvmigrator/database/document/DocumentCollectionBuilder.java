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
package pe.org.incn.sqlsrvmigrator.database.document;

import pe.org.incn.sqlsrvmigrator.database.document.pieces.Line;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import pe.org.incn.sqlsrvmigrator.config.Source;
import pe.org.incn.sqlsrvmigrator.database.CollectionBuilder;
import pe.org.incn.sqlsrvmigrator.database.MappingException;
import pe.org.incn.sqlsrvmigrator.database.components.Column;
import pe.org.incn.sqlsrvmigrator.database.components.Model;
import pe.org.incn.sqlsrvmigrator.database.components.Table;
import pe.org.incn.sqlsrvmigrator.database.components.Value;
import pe.org.incn.sqlsrvmigrator.database.components.Grouped;
import pe.org.incn.sqlsrvmigrator.database.document.pieces.File;

public class DocumentCollectionBuilder<T extends Grouped, M extends Model> extends CollectionBuilder<T> {

    private final DocumentConnection connection;
    private final DocumentAttributeCaster caster;

    public DocumentCollectionBuilder(DocumentConnection connection, DocumentAttributeCaster caster) {
        this.connection = connection;
        this.caster = caster;
    }

    @Override
    public List<Model> mapTableTo(T element) throws MappingException {
        Source source = element.source();
        List<File> files = connection.getTableFrom(source, element.delimiter());
        List<Model> collection = new ArrayList<>();

        for (File file : files) {
            final String GENERATED_KEY = UUID.randomUUID().toString();
            for (Line line : file.content()) {
                try {
                    collection.addAll(bindingModels(element.models(), line, GENERATED_KEY));
                } catch (NoSuchMethodException
                        | InstantiationException
                        | IllegalAccessException
                        | IllegalArgumentException
                        | InvocationTargetException ex) {
                    throw new MappingException(ex.getMessage(), ex);
                }
            }
        }

        return collection;
    }

    protected List<Model> bindingModels(List<Class<? extends Model>> models, Line line, final String GENERATED_UUID)
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        List<Model> mapped = new ArrayList<>();
        for (Class<? extends Model> clazz : models) {
            Table table = clazz.getAnnotation(Table.class);
            if (isAssignableTo(table, line)) {
                Model instance = createModelInstance(clazz, table, line);
                instance.setUUID(GENERATED_UUID);
                mapped.add(instance);
            }
        }
        return mapped;
    }

    protected Model createModelInstance(Class<? extends Model> clazz, Table table, Line line)
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        List<Column> columns = Arrays.asList(table.columns());
        List<Value> values = buildValuesFromTextLine(columns, line);
        return buildModel(clazz, columns, values);
    }

    protected List<Value> buildValuesFromTextLine(List<Column> columns, Line line) {
        return columns.stream().map(column -> {
            Object value = caster.cast(line.getSegment(Integer.parseInt(column.source())), column);
            return new Value(column, value);
        }).collect(Collectors.toList());
    }

    protected boolean isAssignableTo(Table table, Line line) {
        return line.getSegment(1).equals(table.location());
    }
}
