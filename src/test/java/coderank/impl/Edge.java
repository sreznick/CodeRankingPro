package coderank.impl;

public final class Edge {
    private int left;
    private int right;

    Edge(int left, int right) {
        this.left = left;
        this.right = right;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public static Edge create(int left, int right) {
        return new Edge(left, right);
    }
}

