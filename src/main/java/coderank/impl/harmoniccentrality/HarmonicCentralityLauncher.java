package coderank.impl.harmoniccentrality;

import coderank.impl.analyzer.AnalyzerLauncher;
import coderank.impl.javagraph.Node;

import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class HarmonicCentralityLauncher<T> implements AnalyzerLauncher<T> {
    public void launch(HashSet<Node<T>> initStorage, HashMap<Node<T>, List<Node<T>>> edges,
                       HashMap<Node<T>, List<Node<T>>> parents, String mode,
                       OutputStreamWriter distinctStatsOutputStream) {
        HarmonicGraph<T> harmonicGraph = new HarmonicGraph<>(initStorage, edges);
        harmonicGraph.launchAnalysis(0);
        writeResult(harmonicGraph, mode, distinctStatsOutputStream);
    }

}
