package coderank.impl.katzcentrality;

import coderank.impl.analyzer.AnalyzerLauncher;
import coderank.impl.javagraph.MethodNode;
import coderank.impl.javagraph.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class KatzCentralityLauncher<T> implements AnalyzerLauncher<T> {
    public void launch(HashSet<Node<T>> initStorage, HashMap<Node<T>, List<Node<T>>> edges,
                       HashMap<Node<T>, List<Node<T>>> parents, String mode) {
        KatzCentralityCalculator<T> katz = new KatzCentralityCalculator<T>(initStorage, edges);
        katz.launchAnalysis(50);
        if (mode.equals("static_classes")) {
            katz.getDistinctStats();
            katz.rankClasses();
        } else if (mode.equals("dynamic")) {
            katz.getDistinctStats();
        }
    }
}


