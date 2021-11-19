import Parser.Grammar;

import java.io.FileInputStream;
import java.util.Scanner;

public class Menu {
    boolean running      = true;
    Grammar grammar;
    Scanner inputScanner = new Scanner(System.in);

    public void start() {
        try {
            grammar = new Grammar();
            grammar.read(new FileInputStream("input/g2.txt"));
            showMenu();
            while (running) {
                System.out.print("\n> ");
                Runnable command = switch (inputScanner.next().toLowerCase()) {
                    case "nonterminals" -> this::showNonTerminals;
                    case "terminals" -> this::showTerminals;
                    case "productions" -> this::showProductions;
                    case "productionsof" -> this::showProductionsOf;
                    case "iscfg" -> this::showIsCFG;
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
        System.out.println("exit                        - Exit the program");
    }
}
