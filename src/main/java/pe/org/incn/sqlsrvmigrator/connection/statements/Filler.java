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

import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import pe.org.incn.sqlsrvmigrator.database.components.Model;
import pe.org.incn.sqlsrvmigrator.database.components.Value;

public class Filler {

    public <T extends Model> void setValue(Value value, PreparedStatement statement, int parameter) throws SQLException {

        switch (value.getColumn().type()) {
            case STRING:
            case INTEGER:
            case FLOAT:
                setGeneralDataType(statement, value, parameter);
                break;

            case DATE:
                setDatestamp(statement, value.getDateValue(), parameter);
                break;
        }
    }

    private void setGeneralDataType(PreparedStatement statement, Value value, int parameter) throws SQLException {
        if (value.getObjectValue() == null) {
            statement.setNull(parameter, value.getType().key());
            return;
        }

        statement.setObject(parameter, value.getObjectValue(), value.getType().key());
    }

    private void setDatestamp(PreparedStatement statement, Date date, int parameter) throws SQLException {
        if (date == null) {
            statement.setNull(parameter, Types.TIMESTAMP);
            return;
        }

        statement.setTimestamp(parameter, new Timestamp(date.getTime()));
    }
}
