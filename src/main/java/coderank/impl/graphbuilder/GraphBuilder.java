package coderank.impl.graphbuilder;

import coderank.impl.javagraph.Node;
import jdk.internal.net.http.common.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public interface GraphBuilder<T> {

    HashSet<Node<T>> getGraphStorage();

    HashSet<Pair<Node<T>, Node<T>>> getGraphMethodRefs();

    HashMap<Node<T>, List<Node<T>>> getGraphEdges();

    HashMap<Node<T>, List<Node<T>>> getGraphParents();

    HashMap<Node<T>, String> getMethodSources();

    HashSet<Node<T>> constructGraph();
}
