package coderank.impl.cppgraph;

import coderank.impl.javagraph.Graph;
import coderank.impl.javagraph.MethodNode;
import coderank.impl.javagraph.Node;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Paths;
import java.util.*;

public class CppGraphBuilder {
    private final String _pathToGraph;
    private final HashMap<String, HashSet<String>> _edges = new HashMap<>();
    private final HashMap<String, MethodNode> _storage = new HashMap<>();
    private final String _defaultFileName;

    public CppGraphBuilder(String pathToGraph, String defaultFileName) {
        _pathToGraph = pathToGraph;
        _defaultFileName = defaultFileName;
    }

    public Graph<MethodNode> getCppGraph() {
        Graph<MethodNode> graph = new Graph<>();
        HashMap<String, Node<MethodNode>> storage = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();

            Map<?, ?> map = mapper.readValue(Paths.get(_pathToGraph).toFile(), Map.class);
            dfs(map, null, _defaultFileName);
            for (String id : _storage.keySet()) {
                Node<MethodNode> node = MethodNode.createNode();
                node.payload = _storage.get(id);
                storage.put(id, node);
            }
            for (String parId : _edges.keySet()) {
                Node<MethodNode> parent = storage.get(parId);
                for (String childId : _edges.get(parId)) {
                    if (storage.containsKey(childId)) {
                        parent.getChildren().add(storage.get(childId));
                    }
                }
                graph.getGraphStorage().add(parent);
            }

            graph.constructGraph();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return graph;
    }

    private void dfs(Map<?, ?> node, String parent, String fileName) {
        String new_parent;
        String new_filename;
        if (Objects.equals(node.get("kind"), "FunctionDecl") ||
                Objects.equals(node.get("kind"), "CXXMethodDecl")) {
            new_parent = (String) node.get("id");

            String line, col;
            if (node.containsKey("loc")) {
                Map<?, ?> loc = (Map<?, ?>) node.get("loc");
                line = (loc.containsKey("line")) ? loc.get("line").toString() : "?";
                col = (loc.containsKey("col")) ? loc.get("col").toString() : "?";
                new_filename = (loc.containsKey("file")) ? (String) loc.get("file") : fileName;
            } else {
                line = "?";
                col = "?";
                new_filename = "?";
            }
            _storage.put(new_parent, new MethodNode((String) node.get("name"), new_filename +
                    ", line: " + line + ", col: " + col));
            _edges.put(new_parent, new HashSet<>());
        } else {
            new_parent = parent;
            new_filename = fileName;
        }

        if (parent != null && node.containsKey("referencedDecl")) {
            Map<?, ?> referencedDecl = (Map<?, ?>) node.get("referencedDecl");
            if (Objects.equals(referencedDecl.get("kind"), "FunctionDecl")) {
                _edges.get(parent).add((String) referencedDecl.get("id"));
            }
        }

        if (parent != null && Objects.equals(node.get("kind"), "MemberExpr")) {
            _edges.get(parent).add((String) node.get("referencedMemberDecl"));
        }

        for (Map.Entry<?, ?> entry : node.entrySet()) {
            if (entry.getValue() instanceof java.util.ArrayList) {
                for (int i = 0; i < ((ArrayList<?>) entry.getValue()).size(); i++) {
                    Object item = ((ArrayList<?>) entry.getValue()).get(i);
                    if (item instanceof Map) {
                        dfs((Map<?, ?>) item, new_parent, new_filename);
                    }
                }
            }
        }
    }
}
