package coderank.impl.closenesscentrality;

import coderank.impl.analyzer.AnalyzerNode;
import coderank.impl.katzcentrality.KatzNode;

import java.util.LinkedList;
import java.util.List;

public class ClosenessNode implements AnalyzerNode {
    private final int index;
    private double rank = 0;
    public List<ClosenessNode> neighbours = new LinkedList<>();

    public ClosenessNode(int index) {
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
