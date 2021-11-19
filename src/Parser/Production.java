package Parser;

import java.util.List;
import java.util.Objects;

public class Production {
    List<Element> sourceElements;
    List<Element> resultElements;

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
}
