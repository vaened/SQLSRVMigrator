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
package pe.org.incn.sqlsrvmigrator.database.components;

import java.util.Date;
import pe.org.incn.sqlsrvmigrator.database.Type;

public final class Value {

    private final Column column;
    private Object value;

    public Value(Column column, Object value) {
        this.column = column;
        setValue(value);
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Column getColumn() {
        return column;
    }

    public Type getType() {
        return column.type();
    }

    public Object getObjectValue() {
        return value;
    }

    public String getStringValue() {
        return value.toString();
    }

    public Date getDateValue() {
        return (Date) value;
    }

    public Integer getIntegerValue() {
        return Integer.parseInt(value.toString());
    }
}
