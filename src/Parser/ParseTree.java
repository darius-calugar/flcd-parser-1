package Parser;

import Util.Pair;

import java.util.*;

public class ParseTree {
    private List<Pair<Element, Pair<Integer, Integer>>> table;

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
}
