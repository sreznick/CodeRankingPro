package coderank.impl.launchers;

import coderank.impl.staticanalysis.Configuration;

import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
        long time = System.currentTimeMillis();
        String jarPath = args[0];
        JarFile jarFile = new JarFile(jarPath);
        Enumeration<JarEntry> entries = jarFile.entries();

        // to launch without plugin installation

        Configuration.initialize(args[5]);

        String mode = args[4];
        switch (mode) {
            case "static":
                StaticLauncher.launchStatic(args[1], args[2], jarFile);
                break;
            case "instrument_dynamic":
                DynamicLauncher.launchDynamic(args, entries);
                break;
            case "analyse_dynamic":
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

