package coderank.impl.harmoniccentrality;

import coderank.impl.analyzer.AnalyzerNode;

import java.util.LinkedList;
import java.util.List;

public class HarmonicNode extends AnalyzerNode {
    public List<HarmonicNode> neighbours = new LinkedList<>();
    public HarmonicNode(int index) {
        this.index = index;
    }

}
