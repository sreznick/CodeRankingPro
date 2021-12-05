package coderank.impl.launchers;

import coderank.impl.staticanalysis.ClassDescriptor;
import coderank.impl.staticanalysis.Configuration;
import coderank.impl.javagraph.Graph;
import coderank.impl.javagraph.MethodNode;
import coderank.impl.graphbuilder.GraphBuilderLoader;
import org.objectweb.asm.ClassReader;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class StaticLauncher {

    public static GraphBuilderLoader<MethodNode> loader;

    public static void launchStatic(String graphBuilderLocation, String graphBuilderClass, JarFile jarFile) throws Exception {
        loader = new GraphBuilderLoader<>(graphBuilderLocation, graphBuilderClass);
        loader.createInstance();
        List<JarEntry> entries =  jarFile.stream().filter(entry -> {
            String name = entry.getName();
            return name.endsWith(".class") && Configuration.processPackage(name);
        }).collect(Collectors.toList());

        for (JarEntry entry: entries) {
            try (InputStream stream = new BufferedInputStream(jarFile.getInputStream(entry), 1024)) {
                ClassReader re = new ClassReader(stream);
                ClassDescriptor cv = new ClassDescriptor(stream);
                re.accept(cv, 0);
            }
        }

        loader.loadGraphBuilder();
        loader.applyParameters();
    }
}
