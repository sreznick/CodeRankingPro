package coderank.impl.analyzer;

import coderank.impl.javagraph.Node;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public interface AnalyzerLauncher<T> {
    void launch(HashSet<Node<T>> initStorage, HashMap<Node<T>, List<Node<T>>> edges,
                HashMap<Node<T>, List<Node<T>>> parents, String mode);
}
