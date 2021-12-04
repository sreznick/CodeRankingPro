package coderank.impl.closenesscentrality;

import coderank.impl.analyzer.Analyzer;
import coderank.impl.javagraph.Node;
import coderank.impl.closenesscentrality.ClosenessNode;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayDeque;

public class ClosenessGraph<T> implements Analyzer<T> {
    private static int index = 0;
    private final HashMap<ClosenessNode, Node<T>> revStorage = new HashMap<>();
    public HashSet<ClosenessNode> nodes = new HashSet<>();

    public ClosenessGraph(HashSet<ClosenessNode> nodes) {
        this.nodes = nodes;
    }


    public ClosenessGraph(HashSet<Node<T>> initStorage, HashMap<Node<T>, List<Node<T>>> edges) {
        HashMap<Node<T>, ClosenessNode> storage = new HashMap<>();
        for (Node<T> node : initStorage) {
            ClosenessNode currentNode = new ClosenessNode(index++);
            storage.put(node, currentNode);
            revStorage.put(currentNode, node);
            nodes.add(currentNode);
        }

        for (Node<T> node : edges.keySet()) {
            ClosenessNode closenessNode = storage.get(node);
            for (Node<T> edgeNode : edges.get(node)) {
                ClosenessNode pageEdgeNode = storage.get(edgeNode);
                closenessNode.neighbours.add(pageEdgeNode);
            }
        }
    }


    public Integer bfs(ClosenessNode node) {
        List<ClosenessNode> visited = new LinkedList<>();
        ArrayDeque<ClosenessNode> queue = new ArrayDeque<>();
        HashMap<ClosenessNode, Integer> dist = new HashMap<>();
        int sumShortDistances = 0;

        dist.put(node, 0);
        queue.push(node);
        visited.add(node);

        while (queue.peekFirst() != null) {
            ClosenessNode u = queue.pollFirst();
            for (ClosenessNode v : u.neighbours) {
               if (!visited.contains(v)){
                   visited.add(v);
                   queue.addLast(v);
                   dist.put(v, dist.get(u) + 1);
                   sumShortDistances += dist.get(v);
               }
           }

        }
        return sumShortDistances;

    }


    public void launchAnalysis(int iterations) {
        for (ClosenessNode node : nodes) {
            node.setRank(bfs(node));
        }
    }


    @Override
    public HashSet<ClosenessNode> getNodes() {
        return nodes;
    }

    @Override
    public HashMap<ClosenessNode, Node<T>> getRevStorage() {
        return revStorage;
    }
    
}
