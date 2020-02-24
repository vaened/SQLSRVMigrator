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

import java.util.Date;
import pe.org.incn.sqlsrvmigrator.database.Column;
import pe.org.incn.sqlsrvmigrator.database.Table;
import pe.org.incn.sqlsrvmigrator.database.Type;
import pe.org.incn.sqlsrvmigrator.database.Model;

@Table(connection = "sismed", location = "TMOVIMDET", destination = "SISMED_TMOVIMDET")
public class MovementDetail extends Model {

    @Column(name = "MOVCODITIP", type = Type.STRING)
    String MOVCODITIP;
    @Column(name = "MOVNUMERO", type = Type.STRING)
    String MOVNUMERO;
    @Column(name = "MOVNUMEITE", type = Type.STRING)
    String MOVNUMEITE;
    @Column(name = "MEDCOD", type = Type.STRING)
    String MEDCOD;
    @Column(name = "MEDCODIFAB", type = Type.STRING)
    String MEDCODIFAB;
    @Column(name = "MEDREGSAN", type = Type.STRING)
    String MEDREGSAN;
    @Column(name = "MEDLOTE", type = Type.STRING)
    String MEDLOTE;
    @Column(name = "MOVSITUA", type = Type.STRING)
    String MOVSITUA;
    @Column(name = "N_PEDIDO", type = Type.STRING)
    String N_PEDIDO;
    @Column(name = "N_PPA", type = Type.STRING)
    String N_PPA;
    @Column(name = "COD_SIGA", type = Type.STRING)
    String COD_SIGA;
    @Column(name = "MOVINDICON", type = Type.STRING)
    String MOVINDICON;
    @Column(name = "MOVREF", type = Type.STRING)
    String MOVREF;
    @Column(name = "ID_PROCESO", type = Type.STRING)
    String ID_PROCESO;

    @Column(name = "MOVCANTID", type = Type.INTEGER)
    int MOVCANTID;
    @Column(name = "MOVPRECIO", type = Type.INTEGER)
    int MOVPRECIO;
    @Column(name = "MOVTOTAL", type = Type.INTEGER)
    int MOVTOTAL;
    @Column(name = "MOVCANTCON", type = Type.INTEGER)
    int MOVCANTCON;
    @Column(name = "CORRELAT", type = Type.INTEGER)
    int CORRELAT;
    @Column(name = "MOVCATCON", type = Type.INTEGER)
    int MOVCATCON;

    @Column(name = "MEDFECHVTO", type = Type.DATE)
    Date MEDFECHVTO;
    @Column(name = "MOVFECHULT", type = Type.DATE)
    Date MOVFECHULT;
    @Column(name = "FECHAVRS", type = Type.DATE)
    Date FECHAVRS;
}
