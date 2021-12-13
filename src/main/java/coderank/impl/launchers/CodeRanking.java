package coderank.impl.launchers;

import coderank.impl.staticanalysis.Configuration;
import org.gradle.internal.impldep.org.apache.commons.lang.NotImplementedException;
import picocli.CommandLine;
import picocli.CommandLine.Option;

import java.util.jar.JarFile;

@CommandLine.Command(name = "CodeRanking", version = "0.2", mixinStandardHelpOptions = true)
public class CodeRanking implements Runnable {
    @Option(names = { "--project-classpath" }, required = true, description = "Classpath to stuff to be analyzed")
    String projectClassPath = null;

    @Option(names = { "--project-properties" }, required = true, description = "Name of project properties file")
    String propertiesFile = null;

    @Option(names = { "--graph-builder-jar" }, required = false, description = "Jar file where graph builder is located")
    String graphBuilderJar = null;

    @Option(names = { "--graph-builder-class" }, required = false, description = "Jar file where graph builder is located")
    String graphBuilderClassName = null;

    @Option(names = { "--mode" }, defaultValue = "static", description = "One of static, instrument_dynamic, analyze_dynamic")
    String mode;

    @Override
    public void run() {
        try {
            Configuration.initialize(propertiesFile);

            switch (mode) {
                case "static":
                    StaticLauncher.launchStatic(graphBuilderJar, graphBuilderClassName, new JarFile(projectClassPath));
                    break;
                case "instrument_dynamic":
                case "analyze_dynamic":;
                    throw new NotImplementedException("");
                default:
                    throw new IllegalArgumentException("strange mode: " + mode);
            }
        } catch (Exception e) {
            System.err.println("failed to run: " + e);
            e.getStackTrace();
        }
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new CodeRanking()).execute(args);
        System.exit(exitCode);
    }
}
