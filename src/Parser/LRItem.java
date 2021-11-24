package Parser;

import java.util.Objects;
import java.util.stream.Stream;

public class LRItem {
    public final Production production;

    // dot is before this position
    public final int dotPosition;

    public LRItem(Production production, int dotPosition) {
        this.production = production;
        this.dotPosition = dotPosition;
    }

    public Element firstAfterDot() {
        return dotPosition >= production.resultElements.size() ?
                null :
                production.resultElements.get(dotPosition);
    }

    @Override
    public String toString() {
        return production.sourceElements.stream()
                .map(Objects::toString)
                .reduce("", (lhs, rhs) -> lhs + " " + rhs)
                .stripLeading()
                + " -> " +
                Stream.of(
                    production.resultElements.stream()
                            .limit(dotPosition),
                    Stream.of(new NonTerminal(".")),
                    production.resultElements.stream()
                            .skip(dotPosition)
                ).flatMap(s -> s)
                .map(Objects::toString)
                .reduce("", (lhs, rhs) -> lhs + " " + rhs)
                .stripLeading();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LRItem lrItem = (LRItem) o;
        return dotPosition == lrItem.dotPosition && Objects.equals(production, lrItem.production);
    }

    @Override
    public int hashCode() {
        return Objects.hash(production, dotPosition);
    }
}
