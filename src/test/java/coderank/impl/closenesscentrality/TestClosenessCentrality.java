package coderank.impl.closenesscentrality;

import coderank.impl.analyzer.AnalyzerNode;
import coderank.impl.javagraph.MethodNode;
import coderank.impl.javagraph.Node;
import coderank.impl.closenesscentrality.ClosenessGraph;
import coderank.impl.katzcentrality.KatzCentralityLauncher;
import coderank.impl.closenesscentrality.ClosenessNode;
import org.testng.annotations.Test;


import java.io.OutputStreamWriter;
import java.util.*;

import static org.gradle.internal.impldep.org.junit.Assert.assertEquals;


public class TestClosenessCentrality {
    private ClosenessGraph<ClosenessNode> testGraph;

    private static class Edge {
        public int left;
        public int right;

        Edge(int left, int right) {
            this.left = left;
            this.right = right;
        }

        public int getLeft() {
            return left;
        }

        public int getRight() {
            return right;
        }

        public static Edge create(int left, int right) {
            return new Edge(left, right);
        }
    }

    ;

    private void initializeGraph(int n, List<Edge> edges) {
        HashMap<Integer, ClosenessNode> storage = new HashMap<>();
        for (int i = 0; i < n; i++) {
            storage.put(i + 1, new ClosenessNode(i));
        }

        for (Edge e : edges) {
            storage.get(e.getLeft()).neighbours.add(storage.get(e.getRight()));
        }
        HashSet<ClosenessNode> initNodes = new HashSet<>(storage.values());
        testGraph = new ClosenessGraph<>(initNodes);
    }

    private void initializeFirst() {
        initializeGraph(4, Arrays.asList(Edge.create(1, 2), Edge.create(2, 1),
                Edge.create(1, 3), Edge.create(3, 1), Edge.create(1, 4),
                Edge.create(2, 3), Edge.create(4, 3)));

    }
    private void initializeSecond() {
        initializeGraph(5, Arrays.asList(Edge.create(2, 1), Edge.create(1, 3),
                Edge.create(3, 1), Edge.create(2, 3), Edge.create(3, 2),
                Edge.create(2, 4), Edge.create(2, 5),
                Edge.create(3, 5), Edge.create(5, 3), Edge.create(1, 4),
                Edge.create(4, 1)));

    }
    @Test
    public void testFirst() {
        initializeFirst();
        List<Integer> expectedBfs = Arrays.asList(3, 4, 5, 6);
        List<Integer> actualBfs = new ArrayList<>();
        for (ClosenessNode node : testGraph.getNodes()) {
            actualBfs.add(testGraph.bfs(node));
        }
        Collections.sort(actualBfs);
        assertEquals(expectedBfs, actualBfs);

        testGraph.launchAnalysis(0);
        List<Integer> expectedRanks = Arrays.asList(3, 2, 1, 0);
        List<Integer> actualRanks = new ArrayList<>();
        testGraph.getNodes().stream()
                .sorted(Comparator.comparingDouble(AnalyzerNode::getRank).reversed())
                .forEach(x -> actualRanks.add(x.getIndex()));
        assertEquals(expectedRanks, actualRanks);
    }



    @Test
    public void testSecond() {
        initializeSecond();
        List<Integer> expectedBfs = Arrays.asList(4, 5, 6, 8, 9);
        List<Integer> actualBfs = new ArrayList<>();
        for (ClosenessNode node : testGraph.getNodes()) {
            actualBfs.add(testGraph.bfs(node));
        }
        Collections.sort(actualBfs);
        assertEquals(expectedBfs, actualBfs);

        testGraph.launchAnalysis(0);
        List<Integer> expectedRanks = Arrays.asList(3, 4, 0, 2, 1);
        List<Integer> actualRanks = new ArrayList<>();
        testGraph.getNodes().stream()
                .sorted(Comparator.comparingDouble(AnalyzerNode::getRank).reversed())
                .forEach(x -> actualRanks.add(x.getIndex()));
        assertEquals(expectedRanks, actualRanks);
    }
}