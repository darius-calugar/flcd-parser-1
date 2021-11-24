package Parser;

import java.util.List;
import java.util.Objects;

public class Production {
    public final List<Element> sourceElements;
    public final List<Element> resultElements;

    public Production(List<Element> source, List<Element> result) {
        this.sourceElements = source;
        this.resultElements = result;
    }

    @Override
    public String toString() {
        return sourceElements.stream()
                .map(Objects::toString)
                .reduce("", (lhs, rhs) -> lhs + " " + rhs)
                .stripLeading()
               + " -> " +
               resultElements.stream()
                .map(Objects::toString)
                .reduce("", (lhs, rhs) -> lhs + " " + rhs)
                .stripLeading();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Production that = (Production) o;
        return Objects.equals(sourceElements, that.sourceElements) && Objects.equals(resultElements, that.resultElements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceElements, resultElements);
    }
}
