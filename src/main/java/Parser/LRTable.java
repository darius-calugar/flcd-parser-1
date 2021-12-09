package Parser;

import Util.Pair;

import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

public class LRTable {
    public final Map<Pair<State, Element>, State> table = new Hashtable<>();
    public final Map<State, Function<Configuration, Configuration>> actions = new Hashtable<>();
    private final State initialState;

    public LRTable(Grammar grammar) {
        var colcan = grammar.canonicalCollection();
        initialState = colcan.get(0);
        for (var state : colcan) {
            for (var possibleGoToSymbol :
                    state.getItems().stream()
                            .map(LRItem::firstAfterDot)
                            .filter(Objects::nonNull)
                            .distinct()
                            .toList()) {
                table.put(new Pair<>(state, possibleGoToSymbol), grammar.goTo(state, possibleGoToSymbol));
            }
            var shift = makeShift(state);
            var reduce = makeReduce(state);
            var accept = makeAccept(state);
            if (shift != null && reduce != null)
                throw new ParseException(state, "Shift - Reduce conflict");
            if (shift != null)
                actions.put(state, shift);
            if (reduce != null)
                actions.put(state, reduce);
            if (accept != null)
                actions.put(state, accept);
        }
    }

    private Function<Configuration, Configuration> makeShift(State state) {
        if (state.getItems().stream()
                .map(LRItem::firstAfterDot)
                .noneMatch(Objects::nonNull))
            return null;
        return configuration -> {
            Element elementFromInput = configuration.inputStack().pop();
            var currentStateElementPair = configuration.workingStack().getFirst();
            var targetState = table.getOrDefault(new Pair<>(currentStateElementPair.item2(), elementFromInput), null);
            if (targetState == null)
                throw new ParseException(state, elementFromInput, "Could not shift");
            configuration.workingStack().push(new Pair<>(
                    elementFromInput,
                    targetState
            ));
            return configuration;
        };
    }

    private Function<Configuration, Configuration> makeReduce(State state) {
        if (state.getItems().stream()
                .map(LRItem::firstAfterDot)
                .noneMatch(Objects::isNull))
            return null;
        if (state.getItems().stream()
                    .map(LRItem::firstAfterDot)
                    .filter(Objects::isNull)
                    .count() > 1)
            throw new ParseException(state, "Reduce - Reduce conflict");
        var production = state.getItems().stream()
                .filter(item -> item.firstAfterDot() == null)
                .findFirst().get().production;
        return configuration -> {
            var resultElements = production.resultElements;
            var handle = configuration.workingStack().stream()
                    .limit(resultElements.size())
                    .toList();
            if (handle.size() != resultElements.size())
                throw new ParseException(state, "Could not reduce");

            var elements = IntStream.range(0, handle.size())
                    .mapToObj(i -> handle.get(handle.size() - i - 1).item1())
                    .toList();
            if (!elements.equals(resultElements))
                throw new ParseException(state, "Could not reduce");

            handle.forEach(ignored -> configuration.workingStack().pop());
            configuration.workingStack().push(new Pair<>(
                    production.sourceElements.stream().findFirst().get(),
                    table.get(new Pair<>(
                            configuration.workingStack().getFirst().item2(),
                            production.sourceElements.stream().findFirst().get()
                    ))
            ));

            configuration.outputStack().push(production);
            return configuration;
        };
    }

    private Function<Configuration, Configuration> makeAccept(State state) {
        if (state.getItems().stream()
                .noneMatch(item -> Objects.equals(item.production.sourceElements.stream().findFirst().get().value, "S'") &&
                                   item.firstAfterDot() == null))
            return null;
        return configuration -> new Configuration(
                true,
                configuration.workingStack(),
                configuration.inputStack(),
                configuration.outputStack()
        );
    }

    public Optional<Collection<Production>> parse(List<Terminal> input) {
        var config = new Configuration(
                false,
                new LinkedList<>(List.of(new Pair<>(
                        new Epsilon(),
                        initialState
                ))),
                new LinkedList<>(input),
                new LinkedList<>()
        );
        while (!config.accepted()) {
            try {
                var currentState = config.workingStack().getFirst().item2();
                config = actions.get(currentState).apply(config);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                break;
            }
        }
        // System.out.println(config.outputStack());
        return config.accepted() ? Optional.of(config.outputStack()) : Optional.empty();
    }
}
