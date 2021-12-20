package coderank.impl.katzcentrality;

import coderank.impl.analyzer.AnalyzerLauncher;
import coderank.impl.javagraph.Node;

import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class KatzCentralityLauncher<T> implements AnalyzerLauncher<T> {
    public void launch(HashSet<Node<T>> initStorage, HashMap<Node<T>, List<Node<T>>> edges,
                       HashMap<Node<T>, List<Node<T>>> parents, String mode, OutputStreamWriter distinctStatsOutputStream) {
        KatzCentralityCalculator<T> katz = new KatzCentralityCalculator<T>(initStorage, edges);
        katz.launchAnalysis(5);
        writeResult(katz, mode, distinctStatsOutputStream);
    }
}


