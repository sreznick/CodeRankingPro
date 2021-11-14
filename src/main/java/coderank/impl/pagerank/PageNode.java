package coderank.impl.pagerank;

import coderank.impl.analyzer.AnalyzerNode;

import java.util.LinkedList;
import java.util.List;

public class PageNode implements AnalyzerNode {

    public int index;
    private double rank = 1;
    public List<PageNode> parents = new LinkedList<>();
    public List<PageNode> neighbours = new LinkedList<>();

    public PageNode(int index) {
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
