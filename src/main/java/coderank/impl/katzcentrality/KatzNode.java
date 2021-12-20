package coderank.impl.katzcentrality;

import coderank.impl.analyzer.AnalyzerNode;

import java.util.LinkedList;
import java.util.List;

public class KatzNode extends AnalyzerNode {
    public List<KatzNode> neighbours = new LinkedList<>();
    public KatzNode(int index) { setIndex(index); }

}
