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
package pe.org.incn.sqlsrvmigrator.database.document;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import pe.org.incn.sqlsrvmigrator.config.Config;
import pe.org.incn.sqlsrvmigrator.config.Source;
import pe.org.incn.sqlsrvmigrator.database.FileConnection;
import pe.org.incn.sqlsrvmigrator.database.Table;

public class FolderConnection extends FileConnection {

    public FolderConnection(Config config) {
        super(config);
    }

    public List<Line> getTableFrom(Table table) {
        Source source = getSourceFrom(table);
        List<Path> paths = loadFilesFrom(source);
        return paths.stream().map(path -> createLineFor(path, table))
                .filter(Objects::nonNull).collect(Collectors.toList());
    }

    
    
    private Line createLineFor(Path path, Table table) {
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(table.location())) {
                    return new Line(line);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(FolderConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private List<Path> loadFilesFrom(Source source) {
        try {
            Path path = Paths.get(source.getPath());
            return Files.walk(path).filter(file -> file.getFileName().toString().endsWith(".res")).collect(Collectors.toList());
        } catch (IOException ex) {
            Logger.getLogger(FolderConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
