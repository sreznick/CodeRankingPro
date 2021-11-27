package coderank.impl.katzcentrality;

import coderank.impl.analyzer.Analyzer;
import coderank.impl.analyzer.AnalyzerNode;
import coderank.impl.javagraph.Node;
import org.ejml.dense.row.MatrixFeatures_DDRM;
import org.ejml.simple.SimpleEVD;
import org.ejml.simple.SimpleMatrix;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class KatzCentralityCalculator<T> implements Analyzer<T> {
    private final SimpleMatrix A;
    private HashSet<KatzNode> nodes = new HashSet<>();
    private final HashMap<KatzNode, Node<T>> revStorage = new HashMap<>();
    private final int nodesSize;


    public KatzCentralityCalculator(HashSet<KatzNode> nodes) {
        nodesSize = nodes.size();
        this.A = new SimpleMatrix(nodesSize, nodesSize);
        for (KatzNode i : nodes) {
            for (KatzNode j : i.neighbours) {
                this.A.set(i.getIndex(), j.getIndex(), 1);
            }
        }
        this.nodes = nodes;
    }

    public KatzCentralityCalculator(HashSet<Node<T>> initStorage, HashMap<Node<T>, List<Node<T>>> edges) {
        HashMap<Node<T>, KatzNode> storage = new HashMap<>();
        int index = 0;
        for (Node<T> node : initStorage) {
            KatzNode currentNode = new KatzNode(index++);
            storage.put(node, currentNode);
            revStorage.put(currentNode, node);
            nodes.add(currentNode);
        }

        for (Node<T> node : edges.keySet()) {
            KatzNode katzNode = storage.get(node);
            for (Node<T> edgeNode : edges.get(node)) {
                KatzNode pageEdgeNode = storage.get(edgeNode);
                katzNode.neighbours.add(pageEdgeNode);
            }
        }

        nodesSize = nodes.size();
        this.A = new SimpleMatrix(nodesSize, nodesSize);
        for (KatzNode i : nodes) {
            for (KatzNode j : i.neighbours) {
                this.A.set(i.getIndex(), j.getIndex(), 1);
            }
        }
    }



    public void launchAnalysis(int iterations) {
        SimpleEVD<SimpleMatrix> evd = A.eig();
        double maxMagnitude = evd.getEigenvalue(evd.getIndexMax()).getMagnitude();
        double alpha;
        if (maxMagnitude > 0) {
            alpha = Math.min(0.5, 0.5 / maxMagnitude) ;
            SimpleMatrix I = new SimpleMatrix(nodesSize, 1);
            for (int i = 0; i < nodesSize; i++) {
                I.set(i, 0, 1.0);
            }
            SimpleMatrix katzVec = SimpleMatrix.identity(nodesSize).minus(A.transpose().scale(alpha)).invert()
                    .minus(SimpleMatrix.identity(nodesSize)).mult(I);
            for (KatzNode i : nodes) {
                i.setRank(katzVec.get(i.getIndex(), 0));
            }
        } else {
            alpha = 0.5;
            for (KatzNode i : nodes) {
                SimpleMatrix A_k = A;

                double res = 0;
                for (int k = 1; k <= iterations; k++) {
                    for (KatzNode j : nodes) {
                        res += java.lang.Math.pow(alpha, k) * A_k.get(j.getIndex(), i.getIndex());
                    }

                    A_k = A_k.mult(A);
                    boolean allZeros = MatrixFeatures_DDRM.isZeros(A_k.getDDRM(), 0);
                    if (allZeros) break;
                }
                i.setRank(res);
            }
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
