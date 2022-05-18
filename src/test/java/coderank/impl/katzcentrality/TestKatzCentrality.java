package coderank.impl.katzcentrality;

import coderank.impl.analyzer.AnalyzerNode;
import coderank.impl.javagraph.Graph;
import coderank.impl.javagraph.MethodNode;
import coderank.impl.javagraph.Node;

import coderank.impl.Edge;
import static org.gradle.internal.impldep.org.junit.Assert.assertEquals;
import static org.gradle.internal.impldep.org.junit.Assert.assertTrue;

import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.Test;


public class TestKatzCentrality {
    private KatzCentralityCalculator<KatzNode> testGraph;

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
        testGraph.getNodes().stream()
                .sorted(Comparator.comparingDouble(AnalyzerNode::getRank).reversed())
                .forEach(x -> actual.add(x.getIndex()));
        assertEquals(expected, actual);
    }


    @Test
    public void testKatzCentralityCycle() {
        initializeGraph(3, Arrays.asList(Edge.create(1, 2), Edge.create(2, 3),
                Edge.create(3, 1) ));
        testGraph.launchAnalysis(50);
        double eps = 1e-9;
        double expected = 1;
        for (AnalyzerNode node : testGraph.getNodes()) {
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
        List<Integer> expected = Arrays.asList(0, 1, 2, 4, 3, 6, 5);
        List<Integer> actual = new ArrayList<>();
        testGraph.getNodes().stream()
                .sorted(Comparator.comparingDouble(AnalyzerNode::getRank).reversed())
                .forEach(x -> actual.add(x.getIndex()));
        assertEquals(expected, actual);
    }

    @Test
    public void testLaunch() {
        Node<MethodNode> node1 = MethodNode.createNode();
        node1.payload = new MethodNode("A.class", "desc");
        Node<MethodNode> node2 = MethodNode.createNode();
        node2.payload = new MethodNode("B.class", "desc");
        HashSet<Node<MethodNode>> initStorage = new HashSet<>();
        node1.setUsed();
        node2.setUsed();
        initStorage.add(node1);
        initStorage.add(node2);

        HashMap<Node<MethodNode>, List<Node<MethodNode>>> edges = new HashMap<>();
        edges.put(node1, Collections.singletonList(node2));

        HashMap<Node<MethodNode>, List<Node<MethodNode>>> parents = new HashMap<>();
        parents.put(node2, Collections.singletonList(node1));

        KatzCentralityLauncher<MethodNode> launcher = new KatzCentralityLauncher<>();
        launcher.launch(initStorage, edges, parents, "static_classes", new OutputStreamWriter(System.out));
        Graph<MethodNode> g = new Graph<>();

    }
}
