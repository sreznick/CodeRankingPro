package coderank.impl.cpp.quotesInclude;

import coderank.impl.cppgraph.includeGraph.QuoteIncludeGraph;
import coderank.impl.cppgraph.includeGraph.QuotesIncludeGraphNode;
import coderank.impl.javagraph.Graph;
import coderank.impl.javagraph.Node;
import coderank.impl.pagerank.PageRankLauncher;
import org.junit.Before;
import org.junit.Test;
import org.testng.Assert;
import org.testng.collections.Sets;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public final class TestQuotesInclude {
    private static final Comparator<Node<Path>> comparator = Comparator.comparing(Node<Path>::getName);

    private final String testFilesPath =
            Paths.get("src/test/java/coderank/impl/cpp/quotesInclude/testFiles/test1").normalize().toString() +
                    File.separator;
    private List<Node<Path>> expectedStorage;

    private static void addChildren(@Nonnull final Node<Path> parent, @Nonnull final Node<Path>[] children) {
        final List<Node<Path>> parentsChildren = parent.getChildren();
        Collections.addAll(parentsChildren, children);
    }

    /**
     * @param deep Надо ли сравнивать детей.
     */
    private static void assertEqualsNodeLists(@Nonnull final List<Node<Path>> lhs, @Nonnull final List<Node<Path>> rhs,
                                             boolean deep) {
        Assert.assertEquals(lhs.size(), rhs.size());
        lhs.sort(comparator);
        rhs.sort(comparator);
        for (int i = 0; i < lhs.size(); ++i) {
            final Node<Path> lNode = lhs.get(i);
            final Node<Path> rNode = rhs.get(i);
            Assert.assertEquals(lNode.getName(), rNode.getName());
            if (deep)
                assertEqualsNodeLists(lNode.getChildren(), rNode.getChildren(), false);
        }
    }

    @Before
    public void buildExpectedSet() {
        final QuotesIncludeGraphNode bcpp  = new QuotesIncludeGraphNode(Paths.get(testFilesPath + "dir/B.cpp"));
        final QuotesIncludeGraphNode bh    = new QuotesIncludeGraphNode(Paths.get(testFilesPath + "dir/B.h"));
        final QuotesIncludeGraphNode acpp  = new QuotesIncludeGraphNode(Paths.get(testFilesPath + "A.cpp"));
        final QuotesIncludeGraphNode ah    = new QuotesIncludeGraphNode(Paths.get(testFilesPath + "A.h"));
        final QuotesIncludeGraphNode bad   = new QuotesIncludeGraphNode(Paths.get(testFilesPath + "Bad.h"));
        final QuotesIncludeGraphNode empty = new QuotesIncludeGraphNode(Paths.get(testFilesPath + "Empty.h"));

        addChildren(bcpp, new QuotesIncludeGraphNode[]{bh});
        addChildren(bh,   new QuotesIncludeGraphNode[]{bad});
        addChildren(acpp, new QuotesIncludeGraphNode[]{ah, bh});
        addChildren(ah,   new QuotesIncludeGraphNode[]{empty});

        expectedStorage = new ArrayList<>(Sets.newHashSet(bcpp, bh, acpp, ah, bad, empty));
    }

    @Test
    public void test1() throws IOException {
        final ArrayList<Node<Path>> currentStorage =
                new ArrayList<>(QuoteIncludeGraph.constructGraph(Paths.get(testFilesPath)).getGraphStorage());
        assertEqualsNodeLists(expectedStorage, currentStorage, true);

        /* Вывод ранжирования.
        //KatzCentralityLauncher<MethodNode> launcher = new KatzCentralityLauncher<>();
        PageRankLauncher<Path> launcher = new PageRankLauncher<>();
        Exception e = new Exception();
        try {
            launcher.launch(currentGraph.getGraphStorage(), currentGraph.getGraphEdges(),
            currentGraph.getGraphParents(), "dynamic",
            new OutputStreamWriter(new FileOutputStream("CppQuotesIncludeFilesRankingStat.txt"), StandardCharsets.UTF_8));
        } catch (Exception ex) {
            e.addSuppressed(ex);
            e.printStackTrace();
        }*/
    }
}
