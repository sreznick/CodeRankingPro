package coderank.impl.cpp.cppgraphbuilder;

import coderank.impl.cppgraph.CppGraphBuilder;
import coderank.impl.javagraph.Graph;
import coderank.impl.javagraph.MethodNode;
import coderank.impl.pagerank.PageRankLauncher;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class TestCppGraphBuilder {
    private void testGraphBuild(String pathToJson, String srcFileName) {
        Graph<MethodNode> graph = CppGraphBuilder.loadCppGraph(pathToJson, srcFileName);
        //KatzCentralityLauncher<MethodNode> launcher = new KatzCentralityLauncher<>();
        PageRankLauncher<MethodNode> launcher = new PageRankLauncher<>();
        Exception e = new Exception();
        try {
            launcher.launch(graph.getGraphStorage(), graph.getGraphEdges(), graph.getGraphParents(), "dynamic",
                    new OutputStreamWriter(new FileOutputStream("DynamicCodeRankingStat.txt"),
                            StandardCharsets.UTF_8));
        } catch (Exception ex) {
            e.addSuppressed(ex);
            e.printStackTrace();
        }
    }


    @Test
    public void testSmallFile() {
        testGraphBuild("src/test/java/coderank/impl/cpp/cppgraphbuilder/testfiles/small.json",
                "main_small.cpp");
    }

    // There is the same file as in the previous test, but with #include <iostream> line
    @Test
    public void testBigFile() {
        testGraphBuild("src/test/java/coderank/impl/cpp/cppgraphbuilder/testfiles/big.json",
                "main_big.cpp");

    }

}
