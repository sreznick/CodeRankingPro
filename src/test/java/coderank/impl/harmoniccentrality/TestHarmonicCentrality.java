package coderank.impl.harmoniccentrality;

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

import coderank.impl.harmoniccentrality.HarmonicGraph;
import coderank.impl.harmoniccentrality.HarmonicNode;
import org.junit.jupiter.api.Test;

public class TestHarmonicCentrality {
    private HarmonicGraph<HarmonicNode> testGraph;

    private void initializeGraph(int n, List<Edge> edges) {
        HashMap<Integer, HarmonicNode> storage = new HashMap<>();
        for (int i = 0; i < n; i++) {
            storage.put(i + 1, new HarmonicNode(i));
        }

        for (Edge e : edges) {
            storage.get(e.getLeft()).neighbours.add(storage.get(e.getRight()));
        }
        HashSet<HarmonicNode> initNodes = new HashSet<>(storage.values());
        testGraph = new HarmonicGraph<>(initNodes);
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
        List<Double> expectedBfs = Arrays.asList(1.833, 2.0, 2.5, 3.0);
        List<Double> actualBfs = new ArrayList<>();
        for (HarmonicNode node : testGraph.getNodes()) {
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
        List<Double> expectedBfs = Arrays.asList(2.166, 2.333, 3.0, 3.5, 4.0);
        List<Double> actualBfs = new ArrayList<>();
        for (HarmonicNode node : testGraph.getNodes()) {
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