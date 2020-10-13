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
import pe.org.incn.sqlsrvmigrator.database.components.Model;
import pe.org.incn.sqlsrvmigrator.database.components.Table;
import pe.org.incn.sqlsrvmigrator.database.Type;
import pe.org.incn.sqlsrvmigrator.database.components.Value;

@Table(
        connection = "sismed",
        location = "tmovim",
        destination = "SISMED_TMOVIM",
        columns = {
            @Column(source = "MOVCODITIP", type = Type.STRING),
            @Column(source = "MOVNUMERO", type = Type.STRING),
            @Column(source = "ALMCODIORG", type = Type.STRING),
            @Column(source = "ALMORGVIR", type = Type.STRING),
            @Column(source = "ALMCODIDST", type = Type.STRING),
            @Column(source = "ALMDSTVIR", type = Type.STRING),
            @Column(source = "MOVTIPODCI", type = Type.STRING),
            @Column(source = "MOVNUMEDCI", type = Type.STRING),
            @Column(source = "MOVTIPODCO", type = Type.STRING),
            @Column(source = "MOVNUMEDCO", type = Type.STRING),
            @Column(source = "CCTCODIGO", type = Type.STRING),
            @Column(source = "CLICOD", type = Type.STRING),
            @Column(source = "PCTCOD", type = Type.STRING),
            @Column(source = "DIAGCOD", type = Type.STRING),
            @Column(source = "PROGCOD", type = Type.STRING),
            @Column(source = "PERCOD", type = Type.STRING),
            @Column(source = "PRVNUMERUC", type = Type.STRING),
            @Column(source = "PRVDESCRIP", type = Type.STRING),
            @Column(source = "MOVREFE", type = Type.STRING),
            @Column(source = "MOVINDPRC", type = Type.STRING),
            @Column(source = "USRCODIGO", type = Type.STRING),
            @Column(source = "MOVSITUA", type = Type.STRING),
            @Column(source = "TIP_COMP", type = Type.STRING),
            @Column(source = "TIP_PROC", type = Type.STRING),
            @Column(source = "NUM_PROC", type = Type.STRING),
            @Column(source = "MOVTIPEST", type = Type.STRING),
            @Column(source = "MOVCODSUB", type = Type.STRING),
            @Column(source = "MOVTIPREC", type = Type.STRING),
            @Column(source = "MOTCOD", type = Type.STRING),
            @Column(source = "MOVUSUANUL", type = Type.STRING),
            @Column(source = "ID_PROCESO", type = Type.STRING),
            @Column(source = "DNI_CLIE", type = Type.STRING),
            @Column(source = "DIAGCOD1", type = Type.STRING),
            @Column(source = "DIAGCOD2", type = Type.STRING),
            @Column(source = "MOVENVIO", type = Type.STRING),
            @Column(source = "MOVFECHREC", type = Type.DATE),
            @Column(source = "MOVFECHEMI", type = Type.DATE),
            @Column(source = "MOVFECHREG", type = Type.DATE),
            @Column(source = "MOVFECHULT", type = Type.DATE),
            @Column(source = "MOVFECANUL", type = Type.DATE)
        })
public class Movement extends Model {

    public Movement(List<Value> values) {
        super(values);
    }


    public String getSequencialUniqueValue() {
        return getValueAttribute("MOVNUMERO").getStringValue();
    }
}
