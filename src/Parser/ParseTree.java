package Parser;

import Util.Pair;

import java.util.*;

public class ParseTree {
    private Map<Element, List<Pair<Element, Element>>> table;

    public ParseTree(NonTerminal initial, List<Production> productionString) {
        List<Element> currentResult = new ArrayList<>();
        currentResult.add(initial);
        table = new Hashtable<>();
        table.put(initial, new ArrayList<>(List.of(new Pair<>(null, null))));
        for (var production : productionString) {
            var left = production.sourceElements.stream().findFirst().get(); // consider single element on left
            var indexInResult = currentResult.lastIndexOf(left);
            currentResult.remove(indexInResult);
            for (int i = 0; i < production.resultElements.size(); i++) {
                Element element = production.resultElements.get(i);
                currentResult.add(indexInResult, element);
                indexInResult += 1; // TODO currentResult rev
                var tableList = table.getOrDefault(element, null);
                var pairToInsert = new Pair<>(
                        left,
                        i < production.resultElements.size() - 1 ?
                                production.resultElements.get(i + 1) :
                                null
                );
                if (tableList == null) {
                    table.put(element, new ArrayList<>(List.of(pairToInsert)));
                } else {
                    tableList.add(pairToInsert);
                }
            }
        }
    }

    @Override
    public String toString() { // table
        return "ParseTree{" +
                "table=" + table +
                '}';
    }

    public String toGraphString() {
        return
                table.keySet()
                        .stream().sorted(Comparator.comparing(o -> o.value))
                        .map(Element::toString)
                        .reduce("", (a, b) -> a + "\n" + b) +
                "\n" +
                table.entrySet()
                        .stream()
                        .flatMap(entry -> {
                            var ref = new Object() {
                                int index = 0;
                            };
                            return entry.getValue()
                                    .stream()
                                    .map(fatherSiblingPair -> entry.getKey().toString() + ref.index++ + " " + fatherSiblingPair.item1());
                                }

                        )
                        .reduce("", (a, b) -> a + "\n" + b);
    }
}
