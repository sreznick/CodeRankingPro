package coderank.impl.launchers;

import coderank.impl.staticanalysis.Configuration;
import coderank.impl.dynamicanalysis.DynamicAnalysisException;
import coderank.impl.dynamicanalysis.InformationCollector;
import coderank.impl.dynamicanalysis.ResultsHandler;
import coderank.impl.javagraph.Graph;
import coderank.impl.javagraph.MethodNode;
import coderank.impl.graphbuilder.GraphBuilderException;
import coderank.impl.graphbuilder.GraphBuilderLoader;

import javassist.CannotCompileException;
import javassist.CtClass;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;

import com.typesafe.config.Config;

public class DynamicLauncher {

    public static Graph<MethodNode> graph = new Graph<>();
    public static GraphBuilderLoader<MethodNode> loader;

    public static void launchDynamic(Config config, Enumeration<JarEntry> entries) throws DynamicAnalysisException, GraphBuilderException {
        loader = new GraphBuilderLoader<>(config.getString("path.graph-builder"),
                                          config.getString("graph-builder"));
        loader.createInstance();

        ArrayList<String> names = new ArrayList<>();

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();

            if (name.endsWith(".class") && Configuration.processPackage(name)) {
                names.add(name);

                String className = (name.substring(0, name.length() - ".class".length())).replace('/', '.');

System.out.println("launchDynamic-7: " + className);

                CtClass processedClass = InformationCollector.processClass(className,
                                                                           config.getString("path.input-classes"));
System.out.println("launchDynamic-8: " + processedClass);

                if (processedClass != null) {
                    try {
                        processedClass.toClass();
                    } catch (CannotCompileException e) {
                        throw new DynamicAnalysisException("Unable to perform toClass operation on the class " + name);
                    }
                }
            }
        }
    }

    public static void analyseDynamic(String[] args, Enumeration<JarEntry> entries) throws DynamicAnalysisException {
        ResultsHandler handler = new ResultsHandler();
        handler.getData();
        handler.createStorage();
    }
}
