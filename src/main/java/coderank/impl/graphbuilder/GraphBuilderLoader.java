package coderank.impl.graphbuilder;

import coderank.impl.javagraph.Node;
import coderank.impl.pagerank.PageRankLauncher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class GraphBuilderLoader<T> {
    private final String graphBuilderLocation;
    private final String graphBuilderName;
    private Class<?> customGraphBuilder;
    public Object instance;

    public GraphBuilderLoader(String graphBuilderLocation, String graphBuilderName) {
        this.graphBuilderLocation = graphBuilderLocation;
        this.graphBuilderName = graphBuilderName;
    }

    public void createInstance() throws GraphBuilderException {
        try {
            File inputDirectory = new File(graphBuilderLocation);
            ClassLoader classLoader = new URLClassLoader(
                    new URL[]{inputDirectory.toURI().toURL()},
                    this.getClass().getClassLoader()
            );
            customGraphBuilder = classLoader.loadClass(graphBuilderName);
            instance = customGraphBuilder.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new GraphBuilderException("Unable to create instance.");
        }
    }

    public void loadGraphBuilder() throws GraphBuilderException {
        try {
            Method constructGraph = customGraphBuilder.getMethod("constructGraph");
            constructGraph.invoke(instance);
        } catch (Exception e) {
            throw new GraphBuilderException("Unable to load graph builder.");
        }
    }

    @SuppressWarnings("unchecked")
    public void applyParameters() throws GraphBuilderException {
        try {
            Method getStorage = customGraphBuilder.getMethod("getGraphStorage");
            Object objectStorage = getStorage.invoke(instance);
            HashSet<Node<T>> builderStorage = (HashSet<Node<T>>) objectStorage;

            Method getGraphEdges = customGraphBuilder.getMethod("getGraphEdges");
            Object objectEdges = getGraphEdges.invoke(instance);
            HashMap<Node<T>, List<Node<T>>> builderEdges = (HashMap<Node<T>, List<Node<T>>>) objectEdges;

            Method getGraphParents = customGraphBuilder.getMethod("getGraphParents");
            HashMap<Node<T>, List<Node<T>>> builderParents = (HashMap<Node<T>, List<Node<T>>>) getGraphParents.invoke(instance);

            PageRankLauncher<T> launcher = new PageRankLauncher<>();
            launcher.launch(builderStorage, builderEdges, builderParents, "static_classes",
                    new OutputStreamWriter(new FileOutputStream("DynamicCodeRankingStat.txt"),
                            StandardCharsets.UTF_8));

        } catch (Exception e) {
            throw new GraphBuilderException("Unable to apply methods: ", e);
        }
    }

    @SuppressWarnings("unchecked")
    public HashSet<Node<T>> applyGetStorage() throws GraphBuilderException {
        try {
            Method getStorage = customGraphBuilder.getMethod("getGraphStorage");
            return (HashSet<Node<T>>) getStorage.invoke(instance);
        } catch (Exception e) {
            throw new GraphBuilderException("Unable to get graph storage.");
        }
    }
}
