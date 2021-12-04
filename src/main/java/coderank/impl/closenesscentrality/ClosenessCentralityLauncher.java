package coderank.impl.closenesscentrality;

import coderank.impl.analyzer.AnalyzerLauncher;
import coderank.impl.analyzer.AnalyzerNode;
import coderank.impl.javagraph.MethodNode;
import coderank.impl.javagraph.Node;

import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class ClosenessCentralityLauncher<T> implements AnalyzerLauncher<T> {

    public void launch(HashSet<Node<T>> initStorage, HashMap<Node<T>, List<Node<T>>> edges,
                       HashMap<Node<T>, List<Node<T>>> parents, String mode,
                       OutputStreamWriter distinctStatsOutputStream) {
        ClosenessGraph<T> closenessGraph = new ClosenessGraph<>(initStorage, edges);
        closenessGraph.launchAnalysis(0);
        writeResult(closenessGraph, mode, distinctStatsOutputStream);
    }

}
