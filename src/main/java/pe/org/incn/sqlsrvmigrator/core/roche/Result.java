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
        location = "R",
        destination = "ROCHE_RESULS",
        columns = {
            @Column(source = "2", type = Type.INTEGER, destination = "number"),
            @Column(source = "3", type = Type.STRING, destination = "test_ID", mutation = "cleanExam"),
            @Column(source = "4", type = Type.STRING, destination = "result_value"),
            @Column(source = "5", type = Type.STRING, destination = "measure"),
            @Column(source = "7", type = Type.STRING, destination = "abnormal_flag"),
            @Column(source = "9", type = Type.STRING, destination = "result_type"),
            @Column(source = "13", type = Type.DATE, format = "yyyyMMddHHmmss", destination = "completed_at"),}
)
public class Result extends Model {

    public Result(List<Value> values) {
        super(values);
    }

    public String cleanExam(Value exam) {
        return exam.getStringValue().replace("^", "");
    }
}
