package coderank.impl.cppgraph.includeGraph;

import coderank.impl.javagraph.Graph;
import coderank.impl.javagraph.Node;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public final class QuoteIncludeGraph {
    /**
     * @param parsedDir каталог, в котором рекурсивно просматриваются все файлы и считываются директивы
     *                  <code>#include "path"</code> из прочитываемых файлов
     * @return граф (вершины -- файлы, содержащие директиву <code>#include "path"</code> или присутствующие в директиве
     * include какого-нибудь файла; ребро из А в Б -- файл А подключает в себя файл Б)
     */
    public static @Nonnull Graph<Path> constructGraph(@Nonnull final Path parsedDir) throws IOException {
        final Graph<Path> graph = new Graph<>();
        try (final Stream<Path> stream = Files.walk(parsedDir)) {
            final HashSet<Node<Path>> graphStorage = graph.getGraphStorage();
            stream.forEach(path -> {
                if (path.toFile().isFile()) try {
                    final QuotesIncludeGraphNode newNode = new QuotesIncludeGraphNode(path);
                    final List<Node<Path>> children = newNode.getChildren();

                    final Collection<Path> dependencies = QuotesIncludeParser.getAllQuotesIncludePaths(path);
                    for (final Path p : dependencies)
                        children.add(new QuotesIncludeGraphNode(p));
                    graphStorage.add(newNode);
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            });
        }
        graph.constructGraph();
        return graph;
    }
}
