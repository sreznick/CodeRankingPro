package coderank.impl.katzcentrality;

import coderank.impl.analyzer.Analyzer;
import coderank.impl.analyzer.AnalyzerNode;
import coderank.impl.javagraph.Node;
import org.ejml.dense.row.MatrixFeatures_DDRM;
import org.ejml.simple.SimpleEVD;
import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class KatzCentralityCalculator<T> implements Analyzer<T> {
    private final SimpleMatrix A;
    private HashSet<KatzNode> nodes = new HashSet<>();
    private HashSet<KatzNode> usedNodes = new HashSet<>();
    private final HashMap<KatzNode, Node<T>> revStorage = new HashMap<>();
    private final int nodesSize;
    private final double alpha = 0.5;



    public KatzCentralityCalculator(HashSet<KatzNode> nodes) {
        nodesSize = nodes.size();
        this.A = new SimpleMatrix(nodesSize, nodesSize);
        for (KatzNode i : nodes) {
            for (KatzNode j : i.neighbours) {
                this.A.set(i.getIndex(), j.getIndex(), 1);
            }
        }
        this.nodes = nodes;
        this.usedNodes = nodes;
    }

    public KatzCentralityCalculator(HashSet<Node<T>> initStorage, HashMap<Node<T>, List<Node<T>>> edges) {
        HashMap<Node<T>, KatzNode> storage = new HashMap<>();
        int index = 0;
        for (Node<T> node : initStorage) {
            if (node.isUsed()) {
                KatzNode currentNode = new KatzNode(index++);
                storage.put(node, currentNode);
                revStorage.put(currentNode, node);
                nodes.add(currentNode);
                usedNodes.add(currentNode);
            }
        }

        for (Node<T> node : initStorage) {
            if (!node.isUsed()) {
                KatzNode currentNode = new KatzNode(index++);
                storage.put(node, currentNode);
                revStorage.put(currentNode, node);
                nodes.add(currentNode);
            }
        }

        for (Node<T> node : edges.keySet()) {
            KatzNode katzNode = storage.get(node);
            for (Node<T> edgeNode : edges.get(node)) {
                KatzNode pageEdgeNode = storage.get(edgeNode);
                katzNode.neighbours.add(pageEdgeNode);
            }
        }

        nodesSize = usedNodes.size();
        this.A = new SimpleMatrix(nodesSize, nodesSize);
        for (KatzNode i : usedNodes) {
            for (KatzNode j : i.neighbours) {
                this.A.set(i.getIndex(), j.getIndex(), 1);
            }
        }
    }


    public void launchAnalysis(int iterations) {
        ArrayList<SimpleMatrix> As = new ArrayList<>();
        SimpleMatrix A_k = A;
        As.add(A_k);
        for (int k = 1; k < iterations; k++) {
            A_k = A_k.mult(A);
            As.add(A_k);
        }
        for (KatzNode i : usedNodes) {
            double res = 0;
            for (int k = 0; k < iterations; k++) {
                boolean allZeros = MatrixFeatures_DDRM.isZeros(As.get(k).getDDRM(), 0);
                if (allZeros) break;
                for (KatzNode j : usedNodes) {
                    res += java.lang.Math.pow(alpha, k + 1) * As.get(k).get(j.getIndex(), i.getIndex());
                }
            }
            i.setRank(res);
        }

    }


    @Override
    public HashSet<? extends AnalyzerNode> getNodes() {
        return nodes;
    }

    @Override
    public HashMap<? extends AnalyzerNode, Node<T>> getRevStorage() {
        return revStorage;
    }
}
