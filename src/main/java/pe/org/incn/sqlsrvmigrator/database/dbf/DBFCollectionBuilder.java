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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import pe.org.incn.sqlsrvmigrator.database.Builder;
import pe.org.incn.sqlsrvmigrator.database.Column;
import pe.org.incn.sqlsrvmigrator.database.Table;
import pe.org.incn.sqlsrvmigrator.database.Model;

/**
 *
 * @author enea dhack
 * @param <T>
 */
public class DBFCollectionBuilder<T extends Model> extends Builder<T> {

    private final DBFConnection conn;
    private final DBFAttributeCaster caster;

    public DBFCollectionBuilder(DBFConnection conn, DBFAttributeCaster caster) {
        this.conn = conn;
        this.caster = caster;
    }

    @Override
    public List mapTableTo(Class<T> clazz) throws InstantiationException, IllegalAccessException, FileNotFoundException {
        try (DBFReader table = this.conn.getTableFrom(clazz.getAnnotation(Table.class))) {
            List<T> collection = new ArrayList<>();
            DBFRow row;

            while ((row = table.nextRow()) != null) {
                T instance = (T) this.buildModelFromRow(clazz, row);
                instance.setDeleted(row.isDeleted());
                collection.add(instance);
            }

            return collection;
        }
    }

    protected T buildModelFromRow(Class<T> clazz, DBFRow row) throws IllegalAccessException, InstantiationException {
        Field[] fields = clazz.getDeclaredFields();
        T instance = (T) clazz.newInstance();

        for (Field field : fields) {
            field.setAccessible(true);
            field.set(instance, this.caster.cast(row, field.getAnnotation(Column.class)));
        }

        return instance;
    }
}
