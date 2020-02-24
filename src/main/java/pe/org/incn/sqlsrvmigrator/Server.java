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
import org.yaml.snakeyaml.Yaml;
import pe.org.incn.sqlsrvmigrator.config.Config;
import pe.org.incn.sqlsrvmigrator.connection.Connector;
import pe.org.incn.sqlsrvmigrator.connection.Follower;
import pe.org.incn.sqlsrvmigrator.connection.Dumper;
import pe.org.incn.sqlsrvmigrator.connection.Reader;
import pe.org.incn.sqlsrvmigrator.connection.Truncator;
import pe.org.incn.sqlsrvmigrator.connection.statements.Filler;
import pe.org.incn.sqlsrvmigrator.connection.statements.InsertQueryBuilder;

/**
 *
 * @author enea dhack
 */
public class Server {

	public static void main(String[] args)
			throws InstantiationException, IllegalAccessException, FileNotFoundException {
		Config config = new Config(new Yaml());
		BuilderFactory factory = new BuilderFactory(config);
		Presenter presenter = new Presenter();
		Connector connector = new Connector();
		Reader reader = new Reader();
		Follower follower = new Follower(config, reader);
		Dumper dumper = new Dumper(new InsertQueryBuilder(), follower, new Filler());
		Truncator truncator = new Truncator();
		Management management = new Management(config, factory, connector, dumper, presenter, reader, truncator);
		Binding binding = new Binding();

		management.migrate(binding.getClasses());
		
		System.exit(0);

	}
}
