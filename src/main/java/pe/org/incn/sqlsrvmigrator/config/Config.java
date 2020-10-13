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
package pe.org.incn.sqlsrvmigrator.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.yaml.snakeyaml.Yaml;
import pe.org.incn.sqlsrvmigrator.Server;

public final class Config {

    private final Wrapper wrapper;

    public Config(Yaml loader) {
        this.wrapper = this.loadConfigurationFile(loader);
    }

    public List<Database> databases() {
        return this.wrapper.getDatabases();
    }

    public Database getDatabase(Source source) {
        return this.wrapper.getDatabases().stream().filter(database -> database.getName().equals(source.getDatabase())).findFirst().get();
    }

    public Source getSource(String name) {
        return this.wrapper.getSources().stream().filter(conenction -> conenction.getName().equals(name)).findFirst().get();
    }

    public String table() {
        return this.wrapper.getTable();
    }

    protected Wrapper loadConfigurationFile(Yaml loader) {
        try {
            InputStream input = new FileInputStream(this.getConfigFile());
            return loader.loadAs(input, Wrapper.class);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    protected File getConfigFile() {
        return new File("G:\\web\\java\\SQLSRVMigrator\\src\\main\\java\\pe\\org\\incn\\sqlsrvmigrator\\config\\environment.yml").getAbsoluteFile();
    }
}
