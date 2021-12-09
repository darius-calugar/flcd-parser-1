package Parser;

import Util.Pair;

import java.util.Deque;

public record Configuration(
        boolean accepted,
        Deque<Pair<Element, State>> workingStack,
        Deque<Terminal> inputStack,
        Deque<Production> outputStack
) {
}
