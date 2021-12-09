package Parser;

import Util.Pair;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.Graph;

import java.util.*;
import java.util.stream.Stream;
import static guru.nidi.graphviz.model.Factory.node;

public class ParseTree {
    private final List<Pair<Element, Pair<Integer, Integer>>> table;

    public ParseTree(NonTerminal initial, List<Production> productionString) {
        List<Pair<Integer, Element>> currentResult = new ArrayList<>();
        currentResult.add(new Pair<>(0, initial));
        table = new ArrayList<>();
        table.add(new Pair<>(initial, new Pair<>(null, null)));
        for (var production : productionString) {
            var left = production.sourceElements.stream().findFirst().get(); // consider single element on left
            var elementInResult = currentResult.stream()
                    .filter(entry -> entry.item2().equals(left))
                    .reduce((a, b) -> b)
                    .orElse(null);
            var indexInResult = currentResult.lastIndexOf(elementInResult);
            currentResult.remove(indexInResult);
            Integer sibling = null;
            for (int i = production.resultElements.size() - 1; i >= 0; --i) {
                Element element = production.resultElements.get(i);
                table.add(new Pair<>(element, new Pair<>(elementInResult.item1(), sibling)));
                sibling = table.size() - 1;
                currentResult.add(indexInResult, new Pair<>(sibling, element));
            }
        }
    }

    @Override
    public String toString() { // table
        var ref = new Object() {
            Integer index = 0;
        };
        return "index, element, father, sibling" +
                table.stream()
                        .map(entry -> (ref.index++).toString() + " " +
                                entry.item1().toString() + " " +
                                (entry.item2().item1() != null ? entry.item2().item1().toString() : "null") + " " +
                                (entry.item2().item2() != null ? entry.item2().item2().toString() : "null"))
                        .reduce("", (a, b) -> a + "\n" + b);
    }

    public Graph asGraph() {
        var index = new Object() {
            Integer val = -1;
        };
        return Factory.graph()
                .directed()
                .graphAttr().with(Rank.dir(Rank.RankDir.TOP_TO_BOTTOM))
                .with(
                        table.stream()
                                .flatMap(entry -> {
                                            index.val++;
                                            if (entry.item2().item1() == null)
                                                return Stream.of();
                                            return Stream.of(
                                                    node(entry.item2().item1().toString()).with(Label.of(table.get(entry.item2().item1()).item1().value))
                                                            .link(node(index.val.toString()).with(Label.of(entry.item1().value)))
                                            );
                                        }
                                ).toList()
                );
    }
}
