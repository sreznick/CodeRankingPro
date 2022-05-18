package coderank.impl.launchers;

import coderank.impl.staticanalysis.Configuration;

import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.typesafe.config.ConfigFactory;
import com.typesafe.config.Config;
import java.util.Map;


public class Launcher {

    /**
     * @param args args[0] - inputJarFileName
     *             args[1] - graphBuilderLocation
     *             args[2] - graphBuilderName
     *             args[3] - classFilesLocation
     *             args[4] - mode
     *             args[5] - propertiesFileName
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Config conf = ConfigFactory.load();

        String inputJarPath = conf.getString("path.input-jar");
        String graphBuilderPath = conf.getString("path.graph-builder");
        String graphBuilderClassName = conf.getString("graph-builder");
        String rankingMode = conf.getString("ranking.mode");

        long time = System.currentTimeMillis();
        JarFile jarFile = new JarFile(inputJarPath);
        Enumeration<JarEntry> entries = jarFile.entries();

        // to launch without plugin installation

        Configuration.initialize(conf.getString("ranking-properties"));

        switch (rankingMode) {
            case "static":
                StaticLauncher.launchStatic(graphBuilderPath, graphBuilderClassName, jarFile);
                break;
            case "instrument_dynamic":
                DynamicLauncher.launchDynamic(conf, entries);
                break;
            case "analyze_dynamic":
                DynamicLauncher.analyseDynamic(args, entries);
                break;
            default:
                throw new RuntimeException("Incorrect mode provided.");
        }

        long usedBytes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.print("FINAL TIME: ");
        System.out.println(System.currentTimeMillis() - time);
    }
}

