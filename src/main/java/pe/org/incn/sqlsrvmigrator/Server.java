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

import java.io.FileNotFoundException;
import java.text.ParseException;
import org.yaml.snakeyaml.Yaml;
import pe.org.incn.sqlsrvmigrator.config.Config;
import pe.org.incn.sqlsrvmigrator.connection.Connector;
import pe.org.incn.sqlsrvmigrator.connection.Follower;
import pe.org.incn.sqlsrvmigrator.connection.Dumper;
import pe.org.incn.sqlsrvmigrator.connection.Reader;
import pe.org.incn.sqlsrvmigrator.connection.Truncator;
import pe.org.incn.sqlsrvmigrator.connection.statements.Filler;
import pe.org.incn.sqlsrvmigrator.connection.statements.InsertQueryBuilder;
import pe.org.incn.sqlsrvmigrator.database.Compiler;
import pe.org.incn.sqlsrvmigrator.groups.Queue;

/**
 *
 * @author enea dhack
 */
public class Server {

    public static void main(String[] args)
            throws InstantiationException, IllegalAccessException, FileNotFoundException, ParseException {

        Container.setConfig(new Config(new Yaml()));
        Container.setConnector(new Connector());

        Queue queue = new Queue();
        Canvas canvas = new Canvas();
        Reader reader = new Reader();
        Dumper dumper = new Dumper(new InsertQueryBuilder(), new Follower(reader), new Filler());
        Truncator truncator = new Truncator();
        Manager manager = new Manager(dumper, reader, truncator);

        canvas.target(queue);
        queue.getQueue().forEach((Compiler compiler) -> manager.migrate(compiler));

        System.exit(0);
    }
}
