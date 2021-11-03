package coderank.impl.pagerank;

import coderank.impl.katzcentrality.KatzCentralityCalculator;
import coderank.impl.katzcentrality.KatzNode;
import org.testng.annotations.Test;

import java.util.*;

import static org.gradle.internal.impldep.org.junit.Assert.assertEquals;
import static org.gradle.internal.impldep.org.junit.Assert.assertTrue;

public class TestKatzCentrality {
    private KatzCentralityCalculator<KatzNode> testGraph;

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

        public static Edge create(int left, int right)  {
            return new Edge(left, right);
        }
    };

    private void initializeGraph(int n, List<Edge> edges) {
        HashMap<Integer, KatzNode> storage =  new HashMap<>();
        for (int i = 0; i < n; i ++) {
            storage.put(i + 1, new KatzNode(i));
        }

        for(Edge e : edges) {
            storage.get(e.getLeft()).neighbours.add(storage.get(e.getRight()));
        }
        HashSet<KatzNode> initNodes = new HashSet<>(storage.values());
        testGraph = new KatzCentralityCalculator<>(initNodes);
    }


    @Test
    public void testKatzCentralitySquare() {
        initializeGraph(5, Arrays.asList(Edge.create(1, 2), Edge.create(1, 3),
                Edge.create(2, 4), Edge.create(3, 4),
                Edge.create(5, 1), Edge.create(5, 2)));

        testGraph.launchAnalysis(50);
        List<Integer> expected = Arrays.asList(3, 1, 2, 0, 4);
        List<Integer> actual = new ArrayList<>();
        testGraph.nodes.stream()
                .sorted(Comparator.comparingDouble(KatzNode::getRank).reversed())
                .forEach(x -> actual.add(x.index));
        assertEquals(expected, actual);
    }


    @Test
    public void testKatzCentralityCycle() {
        initializeGraph(3, Arrays.asList(Edge.create(1, 2), Edge.create(2, 3),
                Edge.create(3, 1) ));
        testGraph.launchAnalysis(50);
        double eps = 1e-9;
        double expected = 1;
        for (KatzNode node : testGraph.nodes) {
            assertTrue(Math.abs(node.getRank() - expected) < eps);
        }
    }

    @Test
    public void testKatzCentralityBasic() {
        initializeGraph(7, Arrays.asList(Edge.create(1, 2), Edge.create(1, 3),
                Edge.create(1, 4), Edge.create(1, 5),
                Edge.create(1, 7), Edge.create(2, 1),
                Edge.create(3, 1), Edge.create(3, 2),
                Edge.create(4, 2), Edge.create(4, 3),
                Edge.create(4, 5), Edge.create(5, 1),
                Edge.create(5, 3), Edge.create(5, 6),
                Edge.create(5, 4), Edge.create(6, 1),
                Edge.create(6, 5), Edge.create(7, 5)));

        testGraph.launchAnalysis(100);
        List<Integer> expected = Arrays.asList(0, 4, 2, 1, 3, 6, 5);
        List<Integer> actual = new ArrayList<>();
        testGraph.nodes.stream()
                .sorted(Comparator.comparingDouble(KatzNode::getRank).reversed())
                .forEach(x -> actual.add(x.index));
        assertEquals(expected, actual);
    }
}
