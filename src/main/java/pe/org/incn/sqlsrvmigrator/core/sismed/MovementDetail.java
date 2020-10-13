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
        location = "TMOVIMDET",
        destination = "SISMED_TMOVIMDET",
        columns = {
            @Column(source = "MOVCODITIP", type = Type.STRING),
            @Column(source = "MOVNUMERO", type = Type.STRING),
            @Column(source = "MOVNUMEITE", type = Type.STRING),
            @Column(source = "MEDCOD", type = Type.STRING),
            @Column(source = "MEDCODIFAB", type = Type.STRING),
            @Column(source = "MEDREGSAN", type = Type.STRING),
            @Column(source = "MEDLOTE", type = Type.STRING),
            @Column(source = "MOVSITUA", type = Type.STRING),
            @Column(source = "N_PEDIDO", type = Type.STRING),
            @Column(source = "N_PPA", type = Type.STRING),
            @Column(source = "COD_SIGA", type = Type.STRING),
            @Column(source = "MOVINDICON", type = Type.STRING),
            @Column(source = "MOVREF", type = Type.STRING),
            @Column(source = "ID_PROCESO", type = Type.STRING),
            @Column(source = "MOVCANTID", type = Type.INTEGER),
            @Column(source = "MOVPRECIO", type = Type.INTEGER),
            @Column(source = "MOVTOTAL", type = Type.INTEGER),
            @Column(source = "MOVCANTCON", type = Type.INTEGER),
            @Column(source = "CORRELAT", type = Type.INTEGER),
            @Column(source = "MOVCATCON", type = Type.INTEGER),
            @Column(source = "MEDFECHVTO", type = Type.DATE),
            @Column(source = "MOVFECHULT", type = Type.DATE),
            @Column(source = "FECHAVRS", type = Type.DATE)
        })
public class MovementDetail extends Model {

    public MovementDetail(List<Value> values) {
        super(values);
    }

    
    public String getSequencialUniqueValue() {
        return getValueAttribute("MOVNUMERO").getStringValue();
    }
}
