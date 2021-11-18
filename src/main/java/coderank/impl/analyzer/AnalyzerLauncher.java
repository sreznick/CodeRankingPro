package coderank.impl.analyzer;

import coderank.impl.javagraph.Node;

import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public interface AnalyzerLauncher<T> {
    void launch(HashSet<Node<T>> initStorage, HashMap<Node<T>, List<Node<T>>> edges,
                HashMap<Node<T>, List<Node<T>>> parents, String mode, OutputStreamWriter distinctStatsOutputStream);

    default void writeResult(Analyzer<T> analyzer, String mode, OutputStreamWriter distinctStatsOutputStream) {
        if (mode.equals("static_classes")) {
            Map<Double, ? extends List<? extends AnalyzerNode>> distinctNodes = analyzer.getDistinctStats();
            analyzer.writeDistinctStats(distinctNodes, distinctStatsOutputStream);
            analyzer.rankClasses();
        } else if (mode.equals("dynamic")) {
            Map<Double, ? extends List<? extends AnalyzerNode>> distinctNodes = analyzer.getDistinctStats();
            analyzer.writeDistinctStats(distinctNodes, distinctStatsOutputStream);
        }
    }
}
