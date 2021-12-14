package coderank.impl.pagerank;

import coderank.impl.analyzer.Analyzer;
import coderank.impl.javagraph.Node;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class PageGraph<T> implements Analyzer<T> {
    private static int index = 0;
    private static final double dampingFactor = 0.15;
    private static double pageSetSize;
    private final HashMap<PageNode, Node<T>> revStorage = new HashMap<>();
    public HashSet<PageNode> nodes = new HashSet<>();

    public PageGraph(HashSet<PageNode> nodes) {
        this.nodes = nodes;
        pageSetSize = nodes.size();
    }

    public PageGraph(HashSet<Node<T>> initStorage, HashMap<Node<T>, List<Node<T>>> edges,
                     HashMap<Node<T>, List<Node<T>>> parents) {
        HashMap<Node<T>, PageNode> storage = new HashMap<>();
        for (Node<T> node : initStorage) {
            PageNode currentNode = new PageNode(index++);
            storage.put(node, currentNode);
            revStorage.put(currentNode, node);
            nodes.add(currentNode);
        }

        for (Node<T> node : edges.keySet()) {
            PageNode pageNode = storage.get(node);
            for (Node<T> edgeNode : edges.get(node)) {
                PageNode pageEdgeNode = storage.get(edgeNode);
                pageNode.neighbours.add(pageEdgeNode);
            }
        }

        for (Node<T> node : parents.keySet()) {
            PageNode pageNode = storage.get(node);
            for (Node<T> edgeNode : parents.get(node)) {
                PageNode pageEdgeNode = storage.get(edgeNode);
                pageNode.parents.add(pageEdgeNode);
            }
        }

        pageSetSize = nodes.size();
    }

    public void launchAnalysis(int iterations) {
        for (int i = 0; i < iterations; i++) {
            pageRankIteration();
        }
    }

    private void pageRankIteration() {
        for (PageNode node : nodes) {
            updatePageRank(node);
        }
    }

    private void updatePageRank(PageNode currentNode) {
        double sum = 0;
        for (PageNode node : currentNode.parents) {
            sum += node.getRank() / node.neighbours.size();
        }
        double randomFactor = dampingFactor / pageSetSize;
        currentNode.setRank(randomFactor + (1 - dampingFactor) * sum);
    }

    @Override
    public HashSet<PageNode> getNodes() {
        return nodes;
    }

    @Override
    public HashMap<PageNode, Node<T>> getRevStorage() {
        return revStorage;
    }
}
