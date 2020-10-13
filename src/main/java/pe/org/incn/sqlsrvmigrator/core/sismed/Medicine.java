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
package pe.org.incn.sqlsrvmigrator.core.sismed;

import java.util.List;
import pe.org.incn.sqlsrvmigrator.database.components.Column;
import pe.org.incn.sqlsrvmigrator.database.components.Table;
import pe.org.incn.sqlsrvmigrator.database.Type;
import pe.org.incn.sqlsrvmigrator.database.components.Model;
import pe.org.incn.sqlsrvmigrator.database.components.Value;

@Table(
        connection = "sismed",
        location = "MMEDICAM",
        destination = "SISMED_MMEDICAM",
        columns = {
            @Column(source = "MEDNOM", type = Type.STRING),
            @Column(source = "MEDPRES", type = Type.STRING),
            @Column(source = "MEDCNC", type = Type.STRING),
            @Column(source = "MEDTIP", type = Type.STRING),
            @Column(source = "MEDPET", type = Type.STRING),
            @Column(source = "MEDFF", type = Type.STRING),
            @Column(source = "MEDEST", type = Type.STRING),
            @Column(source = "MEDACT", type = Type.STRING),
            @Column(source = "MEDINDVCTO", type = Type.STRING),
            @Column(source = "FECHAUPD", type = Type.DATE),
            @Column(source = "MEDESTVTA", type = Type.STRING),
            @Column(source = "CODIGO_SIG", type = Type.STRING)
        })
public class Medicine extends Model {

    public Medicine(List<Value> values) {
        super(values);
    }

    
    public String getSequencialUniqueValue() {
        return getValueAttribute("FECHAUPD").getStringValue();
    }
}
