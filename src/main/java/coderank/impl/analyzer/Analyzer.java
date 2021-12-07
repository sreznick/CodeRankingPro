package coderank.impl.analyzer;

import coderank.impl.javagraph.Node;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Analyzer<T> {
    void launchAnalysis(int iterations);
    HashSet<? extends AnalyzerNode> getNodes();
    HashMap<? extends AnalyzerNode, Node<T>> getRevStorage();

    default Map<Double, ? extends List<? extends AnalyzerNode>> getDistinctStats() {
        return getNodes().stream()
                        .sorted(Comparator.comparingDouble(AnalyzerNode::getRank).reversed())
                        .collect(Collectors.groupingBy(AnalyzerNode::getRank));
    }

    default void writeDistinctStats(Map<Double, ? extends List<? extends AnalyzerNode>> distinctStats,
                                    OutputStreamWriter outputStream) {
        List<Double> values =
                distinctStats.keySet().stream()
                        .sorted(Comparator.reverseOrder())
                        .collect(Collectors.toList());


        Exception e = new Exception();
        try (Writer fileWriter = new BufferedWriter(outputStream)) {
            for (Double rank : values) {
                String stringRank = String.format("%6.3e", rank);

                fileWriter.write("Rank: " + stringRank + '\n');
                for (AnalyzerNode node : distinctStats.get(rank)) {
                    fileWriter.write(getRevStorage().get(node).getName());
                    fileWriter.write("\n");
                }
                fileWriter.write('\n');
            }
        } catch (IOException ex) {
            e.addSuppressed(ex);
            e.printStackTrace();
        }
    }

    default void rankClasses() {
        Map<Object, ? extends List<? extends AnalyzerNode>> classNameMap =
                getNodes().stream()
                        .collect(Collectors.groupingBy(x -> {
                            String name = getRevStorage().get(x).getName();
                            int idx = name.lastIndexOf('.');
                            return name.subSequence(0, idx);
                        }));

        Map<String, Double> classRanks = new HashMap<>();
        for (Object name : classNameMap.keySet()) {
            double value = 0.0;
            for (AnalyzerNode node : classNameMap.get(name)) {
                value += node.getRank();
            }
            classRanks.put((String) name, value);
        }

        List<String> sortedClassNames = new ArrayList<>(classRanks.keySet());
        sortedClassNames = sortedClassNames
                .stream()
                .sorted(Comparator.comparingDouble(classRanks::get).reversed())
                .collect(Collectors.toList());


        Exception e = new Exception();
        try (Writer fileWriter = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("StaticCodeRankingStat.txt"), StandardCharsets.UTF_8))) {
            double currentRank = classRanks.get(sortedClassNames.get(0)) != null ? classRanks.get(sortedClassNames.get(0)) : 0;
            double diff = 0.002;
            boolean flag = true;
            for (String className : sortedClassNames) {
                if (classRanks.get(className) < currentRank - diff || flag) {
                    String rank = classRanks.get(className).toString();
                    if (rank.length() >= 7) {
                        rank = (String) rank.subSequence(0, 7);
                    }
                    fileWriter.write("Class rank: " + rank + '\n');
                    currentRank = classRanks.get(className);
                    flag = false;
                }
                fileWriter.write(className + "\n\n");
            }
        } catch (IOException ex) {
            e.addSuppressed(ex);
            e.printStackTrace();
        }
    }
}
