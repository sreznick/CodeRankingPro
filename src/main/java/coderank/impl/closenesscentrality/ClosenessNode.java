package coderank.impl.closenesscentrality;

import coderank.impl.analyzer.AnalyzerNode;

import java.util.LinkedList;
import java.util.List;

public class ClosenessNode extends AnalyzerNode {
    public List<ClosenessNode> neighbours = new LinkedList<>();
    public ClosenessNode(int index) { setIndex(index); }

}
