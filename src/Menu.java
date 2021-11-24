import Parser.Grammar;
import Parser.LRItem;
import Parser.Production;

import java.io.FileInputStream;
import java.util.*;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;

public class Menu {
    boolean running      = true;
    Grammar grammar;
    Scanner inputScanner = new Scanner(System.in);

    public void start() {
        try {
            grammar = new Grammar();
            grammar.read(new FileInputStream("input/g1.txt"));

            showMenu();
            while (running) {
                System.out.print("\n> ");
                Runnable command = switch (inputScanner.next().toLowerCase()) {
                    case "nonterminals" -> this::showNonTerminals;
                    case "terminals" -> this::showTerminals;
                    case "productions" -> this::showProductions;
                    case "productionsof" -> this::showProductionsOf;
                    case "iscfg" -> this::showIsCFG;
                    case "closures" -> this::printClosures;
                    case "gotos" -> this::printGoTos;
                    case "colcan" -> this::printCanonicalCollection;
                    case "exit" -> this::exit;
                    default -> null;
                };
                if (command != null)
                    command.run();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void showNonTerminals() {
        for (var e : grammar.getNonTerminals()) {
            System.out.println(e);
        }
    }

    void showTerminals() {
        for (var e : grammar.getTerminals()) {
            System.out.println(e);
        }
    }

    void showProductions() {
        for (var e : grammar.getProductions()) {
            System.out.println(grammar.getProductions().indexOf(e) + ": " + e);
        }
    }

    void showProductionsOf() {
        for (var e : grammar.getProductionsFor(inputScanner.next())) {
            System.out.println(grammar.getProductions().indexOf(e) + ": " + e);
        }
    }

    private void printClosures() {
        for (Production prod: grammar.getProductions()) {
            for (int i = 0; i <= prod.resultElements.size(); ++i) {
                var testLRItem = new LRItem(prod, i);
                System.out.println("Closure of " + testLRItem);
                for (var entry: grammar.closure(testLRItem))
                    System.out.println("    " + entry);
            }
        }
    }

    private void printGoTos() {
        var r = new Random();
        var prod = grammar.getProductions();
        Collections.shuffle(prod);
        var stateSize = r.nextInt(prod.size());
        var chosenProductions = prod.stream().limit(stateSize).toList();
        var state = chosenProductions
                .stream()
                .map(p -> new LRItem(p, r.nextInt(p.resultElements.size())))
                .toList();
        var symbols = state.stream().map(LRItem::firstAfterDot).collect(Collectors.toSet());

        System.out.println("Input:");
        for (var item: state)
            System.out.println("    " + item);

        for (var s: symbols) {
            System.out.println("goto (state, " + s + ")");
            for (var item: grammar.goTo(state, s))
                System.out.println("    " + item);
        }
    }

    private void printCanonicalCollection() {
        var coll = grammar.canonicalCollection();
        for (int i = 0; i < coll.size(); ++i) {
            System.out.println("State " + i + ":");
            for (var item: coll.get(i))
                System.out.println("    " + item);
        }
    }

    private void showIsCFG() {
        System.out.println(grammar.isCFG()
                ? "The grammar is context free"
                : "The grammar is NOT context free");
    }


    void exit() {
        running = false;
    }

    public void showMenu() {
        System.out.println("nonTerminals                - Show the list of non terminals");
        System.out.println("terminals                   - Show the list of terminals");
        System.out.println("productions                 - show the productions of the grammar");
        System.out.println("productionsOf {nonTerminal} - show the productions for a non terminal");
        System.out.println("isCFG                       - shows whether the grammar is context free");
        System.out.println("closures                    - shows all possible closures");
        System.out.println("gotos                       - shows all gotos of a random state");
        System.out.println("colcan                      - shows the canonical collection for this grammar");
        System.out.println("exit                        - Exit the program");
    }
}
