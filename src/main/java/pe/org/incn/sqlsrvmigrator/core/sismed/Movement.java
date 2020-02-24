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
import pe.org.incn.sqlsrvmigrator.database.Model;
import pe.org.incn.sqlsrvmigrator.database.Table;
import pe.org.incn.sqlsrvmigrator.database.Type;

@Table(connection = "sismed", location = "tmovim", destination = "SISMED_TMOVIM")
public class Movement extends Model {

    @Column(name = "MOVNUMEPED", type = Type.INTEGER)
    int MOVNUMEPED;
    @Column(name = "MOVTOT", type = Type.INTEGER)
    int MOVTOT;

    @Column(name = "MOVCODITIP", type = Type.STRING)
    String MOVCODITIP;
    @Column(name = "MOVNUMERO", type = Type.STRING)
    String MOVNUMERO;
    @Column(name = "ALMCODIORG", type = Type.STRING)
    String ALMCODIORG;
    @Column(name = "ALMORGVIR", type = Type.STRING)
    String ALMORGVIR;
    @Column(name = "ALMCODIDST", type = Type.STRING)
    String ALMCODIDST;
    @Column(name = "ALMDSTVIR", type = Type.STRING)
    String ALMDSTVIR;
    @Column(name = "MOVTIPODCI", type = Type.STRING)
    String MOVTIPODCI;
    @Column(name = "MOVNUMEDCI", type = Type.STRING)
    String MOVNUMEDCI;
    @Column(name = "MOVTIPODCO", type = Type.STRING)
    String MOVTIPODCO;
    @Column(name = "MOVNUMEDCO", type = Type.STRING)
    String MOVNUMEDCO;
    @Column(name = "CCTCODIGO", type = Type.STRING)
    String CCTCODIGO;
    @Column(name = "CLICOD", type = Type.STRING)
    String CLICOD;
    @Column(name = "PCTCOD", type = Type.STRING)
    String PCTCOD;
    @Column(name = "DIAGCOD", type = Type.STRING)
    String DIAGCOD;
    @Column(name = "PROGCOD", type = Type.STRING)
    String PROGCOD;
    @Column(name = "PERCOD", type = Type.STRING)
    String PERCOD;
    @Column(name = "PRVNUMERUC", type = Type.STRING)
    String PRVNUMERUC;
    @Column(name = "PRVDESCRIP", type = Type.STRING)
    String PRVDESCRIP;
    @Column(name = "MOVREFE", type = Type.STRING)
    String MOVREFE;
    @Column(name = "MOVINDPRC", type = Type.STRING)
    String MOVINDPRC;
    @Column(name = "USRCODIGO", type = Type.STRING)
    String USRCODIGO;
    @Column(name = "MOVSITUA", type = Type.STRING)
    String MOVSITUA;
    @Column(name = "TIP_COMP", type = Type.STRING)
    String TIP_COMP;
    @Column(name = "TIP_PROC", type = Type.STRING)
    String TIP_PROC;
    @Column(name = "NUM_PROC", type = Type.STRING)
    String NUM_PROC;
    @Column(name = "MOVTIPEST", type = Type.STRING)
    String MOVTIPEST;
    @Column(name = "MOVCODSUB", type = Type.STRING)
    String MOVCODSUB;
    @Column(name = "MOVTIPREC", type = Type.STRING)
    String MOVTIPREC;
    @Column(name = "MOTCOD", type = Type.STRING)
    String MOTCOD;
    @Column(name = "MOVUSUANUL", type = Type.STRING)
    String MOVUSUANUL;
    @Column(name = "ID_PROCESO", type = Type.STRING)
    String ID_PROCESO;
    @Column(name = "DNI_CLIE", type = Type.STRING)
    String DNI_CLIE;
    @Column(name = "DIAGCOD1", type = Type.STRING)
    String DIAGCOD1;
    @Column(name = "DIAGCOD2", type = Type.STRING)
    String DIAGCOD2;
    @Column(name = "MOVENVIO", type = Type.STRING)
    String MOVENVIO;
    
    @Column(name = "MOVFECHREC", type = Type.DATE)
    Date MOVFECHREC;
    @Column(name = "MOVFECHEMI", type = Type.DATE)
    Date MOVFECHEMI;
    @Column(name = "MOVFECHREG", type = Type.DATE)
    Date MOVFECHREG;
    @Column(name = "MOVFECHULT", type = Type.DATE)
    Date MOVFECHULT;
    @Column(name = "MOVFECANUL", type = Type.DATE)
    Date MOVFECANUL;
}
