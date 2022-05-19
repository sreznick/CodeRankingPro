package coderank.impl.cppgraph.includeGraph;

import coderank.impl.javagraph.Node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class QuotesIncludeGraphNode extends Node<Path> {
    private final List<Node<Path>> dependencies = new ArrayList<>();
    private boolean used = false;

    public QuotesIncludeGraphNode(@Nonnull final Path file) {
        payload = file;
    }

    @Override
    public @Nonnull List<Node<Path>> getChildren() {
        return dependencies;
    }

    @Override
    public boolean nodeEquals(@Nonnull final Node<Path> other) {
        return payload.equals(other.payload);
    }

    @Override
    public boolean isUsed() {
        return used;
    }

    @Override
    public void setUsed() {
        used = true;
    }

    @Override
    public @Nonnull String getName() {
        return payload.toString();
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (!(o instanceof QuotesIncludeGraphNode)) return false;
        if (o == this) return true;
        return nodeEquals((QuotesIncludeGraphNode) o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(payload, dependencies);
    }
}
