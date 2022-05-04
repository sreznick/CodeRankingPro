package coderank.impl.kotlingraph;

import coderank.impl.graphbuilder.GraphBuilder;
import coderank.impl.javagraph.Node;
import jdk.internal.net.http.common.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class KotlinGraphBuilder<T> implements GraphBuilder<T> {

    private final HashSet<Node<T>> storage = new HashSet<>();

    private final HashSet<Pair<Node<T>, Node<T>>> methodRefs = new HashSet<>();

    private final HashMap<Node<T>, List<Node<T>>> edges = new HashMap<>();

    private final HashMap<Node<T>, List<Node<T>>> parents = new HashMap<>();

    private final HashMap<Node<T>, Node<T>> methodSources = new HashMap<>();

    @Override
    public HashSet<Node<T>> getGraphStorage() {
        return storage;
    }

    @Override
    public HashSet<Pair<Node<T>, Node<T>>> getGraphMethodRefs() {
        return methodRefs;
    }

    @Override
    public HashMap<Node<T>, List<Node<T>>> getGraphEdges() {
        return edges;
    }

    @Override
    public HashMap<Node<T>, List<Node<T>>> getGraphParents() {
        return parents;
    }

    @Override
    public HashMap<Node<T>, Node<T>> getMethodSources() {
        return methodSources;
    }

    @Override
    public HashSet<Node<T>> constructGraph() {
        storage.addAll(methodSources.values());
        for (Node<T> entry : storage) {
            edges.put(entry, new LinkedList<>());
            parents.put(entry, new LinkedList<>());
        }
        for (Pair<Node<T>, Node<T>> methodRef : methodRefs) {
            Node<T> parentMethodNode = methodRef.first;
            Node<T> childMethodNode = methodRef.second;
            if (methodSources.containsKey(parentMethodNode) && methodSources.containsKey(childMethodNode)) {
                addEdge(methodSources.get(parentMethodNode), methodSources.get(childMethodNode));
                addParent(methodSources.get(parentMethodNode), methodSources.get(childMethodNode));
            }
        }
        return storage;
    }

    private void addEdge(Node<T> parent, Node<T> child) {
        edges.get(parent).add(child);
    }

    private void addParent(Node<T> parent, Node<T> child) {
        parents.get(child).add(parent);
    }
}
