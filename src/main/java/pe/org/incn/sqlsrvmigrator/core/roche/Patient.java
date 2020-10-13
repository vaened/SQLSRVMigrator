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
package pe.org.incn.sqlsrvmigrator.core.roche;

import java.util.List;
import pe.org.incn.sqlsrvmigrator.database.Type;
import pe.org.incn.sqlsrvmigrator.database.components.Column;
import pe.org.incn.sqlsrvmigrator.database.components.Model;
import pe.org.incn.sqlsrvmigrator.database.components.Table;
import pe.org.incn.sqlsrvmigrator.database.components.Value;

@Table(
        connection = "roche",
        location = "P",
        destination = "ROCHE_PATIENTS",
        columns = {
            @Column(source = "2", type = Type.INTEGER, destination = "sequence_number"),
            @Column(source = "3", type = Type.STRING, destination = "external_ID"),
            @Column(source = "4", type = Type.STRING, destination = "clinic_history", mutation = "transformClinicHistory"),
            @Column(source = "6", type = Type.STRING, destination = "full_name", mutation = "cleanPatientFullName"),
            @Column(source = "8", type = Type.DATE, destination = "birthdate", format = "yyyyMMdd"),
            @Column(source = "9", type = Type.STRING, destination = "gender_id"),
            @Column(source = "24", type = Type.DATE, destination = "registered_at", format = "yyyyMMddHHmmss"),
            @Column(source = "26", type = Type.STRING, destination = "origin"),}
)
public class Patient extends Model {

    public Patient(List<Value> values) {
        super(values);
    }

    public Object transformClinicHistory(Value clinicHistory) {
        if (clinicHistory.getStringValue().matches("\\d+")) {
            return String.format("%07d", clinicHistory.getIntegerValue());
        }
        return "";
    }

    public Object cleanPatientFullName(Value name) {
        return name.getStringValue().replace('^', ' ');
    }
}
