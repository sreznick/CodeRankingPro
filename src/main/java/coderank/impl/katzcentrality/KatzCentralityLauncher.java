package coderank.impl.katzcentrality;

import coderank.impl.analyzer.AnalyzerLauncher;
import coderank.impl.analyzer.AnalyzerNode;
import coderank.impl.javagraph.MethodNode;
import coderank.impl.javagraph.Node;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class KatzCentralityLauncher<T> implements AnalyzerLauncher<T> {
    public void launch(HashSet<Node<T>> initStorage, HashMap<Node<T>, List<Node<T>>> edges,
                       HashMap<Node<T>, List<Node<T>>> parents, String mode, OutputStreamWriter distinctStatsOutputStream) {
        KatzCentralityCalculator<T> katz = new KatzCentralityCalculator<T>(initStorage, edges);
        katz.launchAnalysis(50);
        writeResult(katz, mode, distinctStatsOutputStream);
    }
}


