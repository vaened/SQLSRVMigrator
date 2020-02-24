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

/**
 *
 * @author enea dhack
 */
@Table(connection = "sismed", location = "MMEDICAM", destination = "SISMED_MMEDICAM")
public class Medicine extends Model {

    @Column(name = "MEDCOD", type = Type.STRING)
    private String medicineID;

    @Column(name = "MEDNOM", type = Type.STRING)
    private String name;

    @Column(name = "MEDPRES", type = Type.STRING)
    private String presentation;

    @Column(name = "MEDCNC", type = Type.STRING)
    private String medicineCNC;

    @Column(name = "MEDTIP", type = Type.STRING)
    private String type;

    @Column(name = "MEDPET", type = Type.STRING)
    private String medicinePET;

    @Column(name = "MEDFF", type = Type.STRING)
    private String medicineFF;

    @Column(name = "MEDEST", type = Type.STRING)
    private String medicineEST;

    @Column(name = "MEDACT", type = Type.STRING)
    private String medicineACT;

    @Column(name = "MEDINDVCTO", type = Type.STRING)
    private String medicineINDVCTO;

    @Column(name = "FECHAUPD", type = Type.DATE)
    private Date dateUPD;

    @Column(name = "MEDESTVTA", type = Type.STRING)
    private String medicineESTVTA;

    @Column(name = "CODIGO_SIG", type = Type.STRING)
    private String CodeSIG;

    public String getMedicineID() {
        return medicineID;
    }

    public String getName() {
        return name;
    }

    public String getPresentation() {
        return presentation;
    }

    public String getMedicineCNC() {
        return medicineCNC;
    }

    public String getType() {
        return type;
    }

    public String getMedicinePET() {
        return medicinePET;
    }

    public String getMedicineFF() {
        return medicineFF;
    }

    public String getMedicineEST() {
        return medicineEST;
    }

    public String getMedicineACT() {
        return medicineACT;
    }

    public String getMedicineINDVCTO() {
        return medicineINDVCTO;
    }

    public Date getDateUPD() {
        return dateUPD;
    }

    public String getMedicineESTVTA() {
        return medicineESTVTA;
    }

    public String getCodeSIG() {
        return CodeSIG;
    }

}
