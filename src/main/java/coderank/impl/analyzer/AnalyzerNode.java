package coderank.impl.analyzer;

public class AnalyzerNode {
    public int index;
    private double rank = 0;
    public double getRank() {
        return rank;
    }
    public void setRank(double rank) {
        this.rank = rank;
    }
    public int getIndex() {
        return index;
    }
}
