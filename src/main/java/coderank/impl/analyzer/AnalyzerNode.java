package coderank.impl.analyzer;

public class AnalyzerNode {
    private int index;

    private String name;

    private double rank = 0;
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRank() {
        return rank;
    }
    public void setRank(double rank) {
        this.rank = rank;
    }
}
