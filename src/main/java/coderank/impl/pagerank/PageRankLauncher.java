package coderank.impl.pagerank;

import coderank.impl.analyzer.AnalyzerLauncher;
import coderank.impl.javagraph.Node;

import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class PageRankLauncher<T> implements AnalyzerLauncher<T> {

    public void launch(HashSet<Node<T>> initStorage, HashMap<Node<T>, List<Node<T>>> edges,
                       HashMap<Node<T>, List<Node<T>>> parents, String mode,
                       OutputStreamWriter distinctStatsOutputStream) {
        PageGraph<T> pageGraph = new PageGraph<>(initStorage, edges, parents);
        pageGraph.launchAnalysis(30);
        writeResult(pageGraph, mode, distinctStatsOutputStream);
    }

}
