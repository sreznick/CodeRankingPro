package coderank.impl.javagraph;

import coderank.impl.graphbuilder.GraphBuilder;
import jdk.internal.net.http.common.Pair;

import java.util.*;

public class Graph<T> implements GraphBuilder<T> {

    private final HashSet<Node<T>> storage = new HashSet<>();

    private final HashSet<Pair<Node<T>, Node<T>>> edges = new HashSet<>();

    private final HashMap<Node<T>, List<Node<T>>> adjacent = new HashMap<>();

    private final HashMap<Node<T>, List<Node<T>>> parents = new HashMap<>();

    @Override
    public HashSet<Node<T>> getGraphStorage() {
        return storage;
    }

    @Override
    public HashSet<Pair<Node<T>, Node<T>>> getGraphEdges() {
        return edges;
    }

    @Override
    public HashMap<Node<T>, List<Node<T>>> getGraphAdjacent() {
        return adjacent;
    }

    @Override
    public HashMap<Node<T>, List<Node<T>>> getGraphParents() {
        return parents;
    }

    @Override
    public HashSet<Node<T>> constructGraph() {
        for (Node<T> entry : storage) {
            adjacent.put(entry, new LinkedList<>());
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
                    addAdjacent(parent, current);
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
        edges.add(new Pair<>(parent, child));
    }

    private void addAdjacent(Node<T> parent, Node<T> child) {
        adjacent.get(parent).add(child);
    }

    private void addParent(Node<T> parent, Node<T> child) {
        parents.get(child).add(parent);
    }
}
