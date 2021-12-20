package coderank.impl.analytics;

import coderank.impl.analyzer.AnalyzerNode;
import coderank.impl.javagraph.MethodNode;
import coderank.impl.javagraph.Node;
import coderank.impl.katzcentrality.KatzCentralityLauncher;
import coderank.impl.pagerank.PageRankLauncher;
import coderank.impl.closenesscentrality.ClosenessCentralityLauncher;
import coderank.impl.harmoniccentrality.HarmonicCentralityLauncher;
import static org.gradle.internal.impldep.org.junit.Assert.assertEquals;
import static org.gradle.internal.impldep.org.junit.Assert.assertTrue;

import java.io.*;
import java.nio.charset.StandardCharsets;

import java.util.*;

import org.testng.annotations.Test;


public class RailwayStation {

    HashSet<Node<MethodNode>> initStorage = new HashSet<>();
    HashMap<Node<MethodNode>, List<Node<MethodNode>>> edges = new HashMap<>();
    HashMap<Node<MethodNode>, List<Node<MethodNode>>> parents = new HashMap<>();

    private void initializeGraph(){
        Node<MethodNode> node1 = MethodNode.createNode();
        node1.payload = new MethodNode("App.class", "desc");
        Node<MethodNode> node2 = MethodNode.createNode();
        node2.payload = new MethodNode("Event.class", "desc");
        Node<MethodNode> node3 = MethodNode.createNode();
        node3.payload = new MethodNode("Inits.class", "desc");
        Node<MethodNode> node4 = MethodNode.createNode();
        node4.payload = new MethodNode("Platform.class", "desc");
        Node<MethodNode> node5 = MethodNode.createNode();
        node5.payload = new MethodNode("Shedule.class", "desc");
        Node<MethodNode> node6 = MethodNode.createNode();
        node6.payload = new MethodNode("Station.class", "desc");
        Node<MethodNode> node7 = MethodNode.createNode();
        node7.payload = new MethodNode("Timer_Convert.class", "desc");
        Node<MethodNode> node8 = MethodNode.createNode();
        node8.payload = new MethodNode("Track.class", "desc");
        Node<MethodNode> node9 = MethodNode.createNode();
        node9.payload = new MethodNode("Train.class", "desc");

        initStorage.add(node1);
        initStorage.add(node2);
        initStorage.add(node3);
        initStorage.add(node4);
        initStorage.add(node5);
        initStorage.add(node6);
        initStorage.add(node7);
        initStorage.add(node8);
        initStorage.add(node9);

        Vector<Node<MethodNode>> node1Edges = new Vector<>();
        node1Edges.add(node3);
        node1Edges.add(node6);
        Enumeration<Node<MethodNode>> e1 = node1Edges.elements();

        Vector<Node<MethodNode>> node2Edges = new Vector<>();
        node2Edges.add(node8);
        node2Edges.add(node9);
        node2Edges.add(node7);
        Enumeration<Node<MethodNode>> e2 = node2Edges.elements();

        Vector<Node<MethodNode>> node3Edges = new Vector<>();
        node3Edges.add(node6);
        node3Edges.add(node9);
        node3Edges.add(node7);
        Enumeration<Node<MethodNode>> e3 = node3Edges.elements();

        Vector<Node<MethodNode>> node4Edges = new Vector<>();
        node4Edges.add(node8);
        Enumeration<Node<MethodNode>> e4 = node4Edges.elements();

        Vector<Node<MethodNode>> node5Edges = new Vector<>();
        node5Edges.add(node2);
        node5Edges.add(node7);
        Enumeration<Node<MethodNode>> e5 = node5Edges.elements();

        Vector<Node<MethodNode>> node6Edges = new Vector<>();
        node6Edges.add(node4);
        node6Edges.add(node5);
        node6Edges.add(node9);
        Enumeration<Node<MethodNode>> e6 = node6Edges.elements();

        Vector<Node<MethodNode>> node8Edges = new Vector<>();
        node8Edges.add(node4);
        Enumeration<Node<MethodNode>> e8 = node8Edges.elements();

        edges.put(node1, Collections.list(e1));
        edges.put(node2, Collections.list(e2));
        edges.put(node3, Collections.list(e3));
        edges.put(node4, Collections.list(e4));
        edges.put(node5, Collections.list(e5));
        edges.put(node6, Collections.list(e6));
        edges.put(node8, Collections.list(e8));

        Vector<Node<MethodNode>> node2Parents = new Vector<>();
        node2Parents.add(node5);
        Enumeration<Node<MethodNode>> p2 = node2Parents.elements();

        Vector<Node<MethodNode>> node3Parents = new Vector<>();
        node3Parents.add(node1);
        Enumeration<Node<MethodNode>> p3 = node3Parents.elements();

        Vector<Node<MethodNode>> node4Parents = new Vector<>();
        node4Parents.add(node6);
        node4Parents.add(node8);
        Enumeration<Node<MethodNode>> p4 = node4Parents.elements();

        Vector<Node<MethodNode>> node5Parents = new Vector<>();
        node5Parents.add(node6);
        Enumeration<Node<MethodNode>> p5 = node5Parents.elements();

        Vector<Node<MethodNode>> node6Parents = new Vector<>();
        node6Parents.add(node1);
        node6Parents.add(node3);
        Enumeration<Node<MethodNode>> p6 = node6Parents.elements();

        Vector<Node<MethodNode>> node7Parents = new Vector<>();
        node7Parents.add(node2);
        node7Parents.add(node3);
        node7Parents.add(node5);
        Enumeration<Node<MethodNode>> p7 = node7Parents.elements();

        Vector<Node<MethodNode>> node8Parents = new Vector<>();
        node8Parents.add(node2);
        node8Parents.add(node4);
        Enumeration<Node<MethodNode>> p8 = node8Parents.elements();

        Vector<Node<MethodNode>> node9Parents = new Vector<>();
        node9Parents.add(node2);
        node9Parents.add(node3);
        node9Parents.add(node6);
        Enumeration<Node<MethodNode>> p9 = node9Parents.elements();


        parents.put(node2, Collections.list(p2));
        parents.put(node3, Collections.list(p3));
        parents.put(node4, Collections.list(p4));
        parents.put(node5, Collections.list(p5));
        parents.put(node6, Collections.list(p6));
        parents.put(node7, Collections.list(p7));
        parents.put(node8, Collections.list(p8));
        parents.put(node9, Collections.list(p9));
    }

    @Test
    public void testLaunch() {
        //Katz fails with Eigenvalue Decomposition error - bad matrices somewhere

        Exception e = new Exception();
        initializeGraph();

        try (Writer fileWriter = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("src\\test\\analytics\\railwayStationAnalytics\\PageRank.txt"), StandardCharsets.UTF_8))) {
            long time = System.currentTimeMillis();
            PageRankLauncher<MethodNode> launcher = new PageRankLauncher<>();
            launcher.launch(initStorage, edges, parents, "static_classes", new OutputStreamWriter(System.out));
            fileWriter.write("FINAL TIME: " + (System.currentTimeMillis() - time)  + '\n');
            long usedBytes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            fileWriter.write("MB USED: " + Math.round((double)(usedBytes / 1000000) * 1000.0)/1000.0);

        }
        catch (IOException ex) {
            e.addSuppressed(ex);
            e.printStackTrace();
        }
        try (Writer fileWriter = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(
                        "src\\test\\analytics\\railwayStationAnalytics\\Closeness.txt"
                ), StandardCharsets.UTF_8))) {
            long time = System.currentTimeMillis();
            ClosenessCentralityLauncher<MethodNode> launcher = new ClosenessCentralityLauncher<>();
            launcher.launch(initStorage, edges, parents, "static_classes", new OutputStreamWriter(System.out));
            fileWriter.write("FINAL TIME: " + (System.currentTimeMillis() - time)  + '\n');
            long usedBytes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            fileWriter.write("MB USED: " + Math.round((double)(usedBytes / 1000000) * 1000.0)/1000.0);
        }
        catch (IOException ex) {
            e.addSuppressed(ex);
            e.printStackTrace();
        }
        try (Writer fileWriter = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(
                        "src\\test\\analytics\\railwayStationAnalytics\\Harmonic.txt"
                ), StandardCharsets.UTF_8))) {
            long time = System.currentTimeMillis();
            HarmonicCentralityLauncher<MethodNode> launcher = new HarmonicCentralityLauncher<>();
            launcher.launch(initStorage, edges, parents, "static_classes", new OutputStreamWriter(System.out));
            fileWriter.write("FINAL TIME: " + (System.currentTimeMillis() - time)  + '\n');
            long usedBytes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            fileWriter.write("MB USED: " + Math.round((double)(usedBytes / 1000000) * 1000.0)/1000.0);
        }
        catch (IOException ex) {
            e.addSuppressed(ex);
            e.printStackTrace();
        }
    }

}
