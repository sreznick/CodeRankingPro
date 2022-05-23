package coderank.impl.pagerank;

import coderank.impl.analyzer.AnalyzerNode;

import java.util.LinkedList;
import java.util.List;

public class PageNode extends AnalyzerNode {
    public List<PageNode> parents = new LinkedList<>();
    public List<PageNode> neighbours = new LinkedList<>();

    public PageNode(int index) {
        setIndex(index);
        setName("");
    }

    public PageNode(int index, String name) {
        setIndex(index);
        setName(name);
    }

}
