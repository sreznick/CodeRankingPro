package coderank.impl.cppgraph.includeGraph;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class QuotesIncludeParser {
    private static final Pattern includePattern = Pattern.compile("\\s*#\\s*include\\s*\"(.+)\"");

    // TODO Есть ли проблемы с кодировкой?
    // TODO Учесть комментарии вида /*...*/
    // TODO Предупреждение при парсинге `#include "path"    extra tokens at end`?

    /**
     * @param file читаемый файл
     * @return все корректные пути из строк файла file вида <code>#include "path"</code>
     * @throws IOException при проблемах открытия или прочитывания file
     */
    @Nonnull
    static Collection<Path> getAllQuotesIncludePaths(@Nonnull final Path file) throws IOException {
        final HashSet<Path> allIncludesPaths = new HashSet<>();
        try (final BufferedReader fileReader = Files.newBufferedReader(file)) {
            final String parentDirString = file.getParent().normalize().toString() + File.separator;

            String line;
            while ((line = fileReader.readLine()) != null) {
                final Matcher matcher = includePattern.matcher(line);
                if (matcher.find()) try {
                    final Path includePath = Paths.get(parentDirString + matcher.group(1)).normalize();
                    if (parentDirString.equals(includePath.toString() + File.separator)) {
                        // Заматчился какой-нибудь поганый путь типа "//"
                        continue;
                    }
                    if (Files.exists(includePath)) allIncludesPaths.add(includePath);
                } catch (InvalidPathException ignored) { // Путь из include некорректный.
                }
            }
        }
        return allIncludesPaths;
    }
}
