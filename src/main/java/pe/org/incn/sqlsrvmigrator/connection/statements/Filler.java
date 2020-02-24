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
package pe.org.incn.sqlsrvmigrator.connection.statements;

import java.lang.reflect.Field;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import pe.org.incn.sqlsrvmigrator.database.Column;
import pe.org.incn.sqlsrvmigrator.database.Model;

public class Filler {

    public <T extends Model> void setValue(T model, Field field, PreparedStatement statement, int parameter) throws IllegalArgumentException, IllegalAccessException, SQLException {
        field.setAccessible(true);

        if (field.getName().equals("deleted")) {
            statement.setBoolean(parameter, model.isDeleted());
            return;
        }

        Column column = field.getAnnotation(Column.class);
        switch (column.type()) {
            case STRING:
            case INTEGER:
            case FLOAT:
                statement.setObject(parameter, field.get(model), column.type().getType());
                break;

            case DATE:
                setDatestamp(statement, (Date) field.get(model), parameter);
                break;
        }
    }

    private void setDatestamp(PreparedStatement statement, Date date, int parameter) throws SQLException {
        if (date != null) {
            statement.setTimestamp(parameter, new Timestamp(date.getTime()));
        } else {
            statement.setNull(parameter, Types.TIMESTAMP);
        }
    }
}
