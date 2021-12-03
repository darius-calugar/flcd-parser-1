package Parser;

import Util.Pair;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class LRTable {
    private List<State> states;
    private List<Element> symbols;
    private Map<Pair<State, Element>, State> table = new Hashtable<>();
    private Map<State, Function<ParserState, ParserState>> actions;

    public LRTable(Grammar grammar) {
        for (var state: grammar.canonicalCollection()) {
            for (var possibleGoToSymbol:
                    state.getItems().stream()
                            .map(LRItem::firstAfterDot)
                            .filter(Objects::nonNull)
                            .distinct()
                            .toList()
            )
                table.put(new Pair<>(state, possibleGoToSymbol), grammar.goTo(state, possibleGoToSymbol));
        }
    }

    private Function<ParserState, ParserState> makeShift(Grammar grammar, State state) {
        var nullElem = state.getItems().stream()
                .map(LRItem::firstAfterDot)
                .filter(Objects::isNull)
                .findAny();
        if (nullElem.isPresent())
            return null;
        return parserState -> {
            Element elementFromInput = parserState.inputStack().pop();
            var currentStateElementPair = parserState.workingStack().getFirst();
            var targetState = table.getOrDefault(new Pair<>(currentStateElementPair.item2(), elementFromInput), null);
            if (targetState == null)
                throw new RuntimeException("Could not shift");
          return parserState;
        };
    }
}
