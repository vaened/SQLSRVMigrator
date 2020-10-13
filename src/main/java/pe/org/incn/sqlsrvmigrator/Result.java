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
package pe.org.incn.sqlsrvmigrator;

import pe.org.incn.sqlsrvmigrator.database.components.Table;

public class Result {

    private final Table table;
    private final int totalRecordsAffected;
    private final int toMigrate;
    private final String message;

    public Result(Table table, int totalRecordsAffected, int toMigrate, String message) {
        this.table = table;
        this.totalRecordsAffected = totalRecordsAffected;
        this.toMigrate = toMigrate;
        this.message = message;
    }

    public static Result migrated(Table table, int totalRecordsAffected, int toMigrate) {
        return new Result(table, totalRecordsAffected, toMigrate, null);
    }

    public static Result disabled(Table table, int toMigrate) {
        return new Result(table, 0, toMigrate, String.format("Migration for source [%s.%s] was disabled", table.connection(), table.location()));
    }

    public static Result error(Table table, int toMigrate, String message) {
        return new Result(table, 0, toMigrate, message);
    }

    public Table getTable() {
        return table;
    }

    public int getTotalRecordsAffected() {
        return totalRecordsAffected;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return totalRecordsAffected == toMigrate;
    }
}
