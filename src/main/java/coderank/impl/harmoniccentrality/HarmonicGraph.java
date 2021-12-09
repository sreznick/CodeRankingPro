package coderank.impl.harmoniccentrality;

import coderank.impl.analyzer.Analyzer;
import coderank.impl.javagraph.Node;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayDeque;


public class HarmonicGraph<T> implements Analyzer<T> {
    private static int index = 0;
    private final HashMap<HarmonicNode, Node<T>> revStorage = new HashMap<>();
    public HashSet<HarmonicNode> nodes = new HashSet<>();

    public HarmonicGraph(HashSet<HarmonicNode> nodes) {
        this.nodes = nodes;
    }


    public HarmonicGraph(HashSet<Node<T>> initStorage, HashMap<Node<T>, List<Node<T>>> edges) {
        HashMap<Node<T>, HarmonicNode> storage = new HashMap<>();
        for (Node<T> node : initStorage) {
            HarmonicNode currentNode = new HarmonicNode(index++);
            storage.put(node, currentNode);
            revStorage.put(currentNode, node);
            nodes.add(currentNode);
        }

        for (Node<T> node : edges.keySet()) {
            HarmonicNode harmonicNode = storage.get(node);
            for (Node<T> edgeNode : edges.get(node)) {
                HarmonicNode pageEdgeNode = storage.get(edgeNode);
                harmonicNode.neighbours.add(pageEdgeNode);
            }
        }
    }


    public Double bfs(HarmonicNode node) {
        List<HarmonicNode> visited = new LinkedList<>();
        ArrayDeque<HarmonicNode> queue = new ArrayDeque<>();
        HashMap<HarmonicNode, Integer> dist = new HashMap<>();
        double sumShortDistances = 0;

        dist.put(node, 0);
        queue.push(node);
        visited.add(node);

        while (queue.peekFirst() != null) {
            HarmonicNode u = queue.pollFirst();
            for (HarmonicNode v : u.neighbours) {
                if (!visited.contains(v)){
                    visited.add(v);
                    queue.addLast(v);
                    dist.put(v, dist.get(u) + 1);
                    double cur_dist = 1 / (double)(dist.get(v));
                    sumShortDistances += Math.round(cur_dist * 1000.0)/1000.0;
                }
            }

        }
        return sumShortDistances;

    }


    public void launchAnalysis(int iterations) {
        for (HarmonicNode node : nodes) {
            node.setRank(bfs(node) / (nodes.size() - 1));
        }
    }


    @Override
    public HashSet<HarmonicNode> getNodes() {
        return nodes;
    }

    @Override
    public HashMap<HarmonicNode, Node<T>> getRevStorage() {
        return revStorage;
    }

}