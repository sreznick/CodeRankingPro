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

    private final HashMap<Node<T>, String> methodSources = new HashMap<>();

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
    public HashMap<Node<T>, String> getMethodSources() {
        return methodSources;
    }

    @Override
    public HashSet<Node<T>> constructGraph() {
        for (Pair<Node<T>, Node<T>> entry : methodRefs) {
            storage.add(entry.first);
            storage.add(entry.second);
        }
        for (Node<T> entry : storage) {
            if (methodSources.containsKey(entry)) entry.setDesc(methodSources.get(entry));
            edges.put(entry, new LinkedList<>());
            parents.put(entry, new LinkedList<>());
        }
        for (Node<T> entry : storage) {
            if (!entry.isUsed()) {
                traverseChildren(entry, null);
            }
        }
        return storage;
    }

    private void traverseChildren(Node<T> current, Node<T> parent) {
        for (Node<T> elem : storage) {
            if (current.nodeEquals(elem)) {
                current = elem;
                if (parent != null && !current.nodeEquals(parent)) {
                    addEdge(parent, current);
                    addParent(parent, current);
                }
                break;
            }
        }
        if (current.isUsed()) {
            return;
        }
        current.setUsed();

        for (int i = 0; i < current.getChildren().size(); i++) {
            traverseChildren(current.getChildren().get(i), current);
        }
    }

    private void addEdge(Node<T> parent, Node<T> child) {
        edges.get(parent).add(child);
    }

    private void addParent(Node<T> parent, Node<T> child) {
        parents.get(child).add(parent);
    }
}
