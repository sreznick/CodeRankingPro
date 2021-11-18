package coderank.impl.katzcentrality;

import coderank.impl.analyzer.AnalyzerNode;
import coderank.impl.pagerank.PageNode;

import java.util.LinkedList;
import java.util.List;

public class KatzNode implements AnalyzerNode {
    private final int index;
    private double rank = 0;
    public List<KatzNode> neighbours = new LinkedList<>();

    public KatzNode(int index) {
        this.index = index;
    }

    @Override
    public double getRank() {
        return rank;
    }

    @Override
    public void setRank(double rank) {
        this.rank = rank;
    }

    @Override
    public int getIndex() {
        return index;
    }
}
