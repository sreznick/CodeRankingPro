package coderank.impl.closenesscentrality;

import coderank.impl.analyzer.AnalyzerNode;
import coderank.impl.Edge;

import static org.gradle.internal.impldep.org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import org.testng.annotations.Test;

public class TestClosenessCentrality {
    private ClosenessGraph<ClosenessNode> testGraph;

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
        List<Integer> expectedRanks = Arrays.asList(0, 1, 2, 3);
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
        List<Integer> expectedRanks = Arrays.asList(1, 2, 0, 4, 3);
        List<Integer> actualRanks = new ArrayList<>();
        testGraph.getNodes().stream()
                .sorted(Comparator.comparingDouble(AnalyzerNode::getRank).reversed())
                .forEach(x -> actualRanks.add(x.getIndex()));
        assertEquals(expectedRanks, actualRanks);
    }
}