package Parser;

import Util.Pair;

import java.util.Deque;

public record ParserState(
        Deque<Pair<Element, State>> workingStack,
        Deque<Terminal> inputStack,
        Deque<Production> outputStack
) {}
