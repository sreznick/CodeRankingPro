package coderank.impl.cppgraph;

import coderank.impl.javagraph.Graph;
import coderank.impl.javagraph.MethodNode;
import coderank.impl.javagraph.Node;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Paths;
import java.util.*;

public class CppGraphBuilder {
    private static final HashMap<String, HashSet<String>> _edges = new HashMap<>();
    private static final HashMap<String, MethodNode> _storage = new HashMap<>();
    private static final String kind_tag = "kind";
    private static final String id_tag = "id";
    private static final String location_tag = "loc";
    private static final String line_tag = "line";
    private static final String col_tag = "col";
    private static final String file_tag = "file";
    private static final String name_tag = "name";
    private static final String referenced_decl_tag = "referencedDecl";
    private static final String function_decl_tag = "FunctionDecl";
    private static final String cxx_method_decl_tag = "CXXMethodDecl";
    private static final String member_expr_tag = "MemberExpr";
    private static final String referenced_member_decl_tag = "referencedMemberDecl";
    private static final String unknown = "?";
    private static final Set<String> function_tags = new HashSet<>(Arrays.asList(function_decl_tag, cxx_method_decl_tag));


    private CppGraphBuilder() {}

    public static Graph<MethodNode> loadCppGraph(String pathToGraph, String defaultFileName) {
        _edges.clear();
        _storage.clear();
        Graph<MethodNode> graph = new Graph<>();
        HashMap<String, Node<MethodNode>> storage = new HashMap<>();
        try {
            ObjectMapper mapper = new ObjectMapper();

            Map<?, ?> map = mapper.readValue(Paths.get(pathToGraph).toFile(), Map.class);
            dfs(map, null, defaultFileName);
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

    private static void dfs(Map<?, ?> node, String parent, String fileName) {
        String new_parent;
        String new_filename;
        if (function_tags.contains((String) node.get(kind_tag))) {
            new_parent = (String) node.get(id_tag);

            String line, col;
            if (node.containsKey(location_tag)) {
                Map<?, ?> loc = (Map<?, ?>) node.get(location_tag);
                line = (loc.containsKey(line_tag)) ? loc.get(line_tag).toString() : unknown;
                col = (loc.containsKey(col_tag)) ? loc.get(col_tag).toString() : unknown;
                new_filename = (loc.containsKey(file_tag)) ? (String) loc.get(file_tag) : fileName;
            } else {
                line = unknown;
                col = unknown;
                new_filename = unknown;
            }
            _storage.put(new_parent, new MethodNode((String) node.get(name_tag), new_filename +
                    ", line: " + line + ", col: " + col));
            _edges.put(new_parent, new HashSet<>());
        } else {
            new_parent = parent;
            new_filename = fileName;
        }

        if (parent != null && node.containsKey(referenced_decl_tag)) {
            Map<?, ?> referencedDecl = (Map<?, ?>) node.get(referenced_decl_tag);
            if (Objects.equals(referencedDecl.get(kind_tag), function_decl_tag)) {
                _edges.get(parent).add((String) referencedDecl.get(id_tag));
            }
        }

        if (parent != null && Objects.equals(node.get(kind_tag), member_expr_tag)) {
            _edges.get(parent).add((String) node.get(referenced_member_decl_tag));
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
