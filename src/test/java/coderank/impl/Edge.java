package coderank.impl;

public  class Edge {
    public int left;
    public int right;

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

