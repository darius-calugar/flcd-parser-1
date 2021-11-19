package Parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Grammar {
    final Set<NonTerminal> nonTerminals = new HashSet<>();
    final Set<Terminal>    terminals    = new HashSet<>();
    final List<Production> productions  = new ArrayList<>();

    public void read(InputStream input) throws IOException {
        // Scan each line
        Scanner fileScanner = new Scanner(input);
        while (fileScanner.hasNextLine()) {
            String        line             = fileScanner.nextLine();
            List<Element> productionSource = new LinkedList<>();

            // Scan the production source
            Scanner sourceScanner = new Scanner(line.split("::=")[0]);
            while (sourceScanner.hasNext()) {
                String elementValue = sourceScanner.next();
                if (elementValue.equals("$")) {
                    productionSource.add(new Epsilon());
                } else if (elementValue.matches("\".*\"")) {
                    Terminal terminal = new Terminal(elementValue.substring(1, elementValue.length() - 1));
                    terminals.add(terminal);
                    productionSource.add(terminal);
                } else {
                    NonTerminal nonTerminal = new NonTerminal(elementValue);
                    nonTerminals.add(nonTerminal);
                    productionSource.add(nonTerminal);
                }
            }

            // Scan the production result
            Scanner resultScanner = new Scanner(line.split("::=")[1]);
            while (resultScanner.hasNext()) {
                List<Element> productionResult  = new LinkedList<>();
                Scanner       productionScanner = new Scanner(resultScanner.useDelimiter("\\|").next());
                while (productionScanner.hasNext()) {
                    String elementValue = productionScanner.next();
                    if (elementValue.equals("$")) {
                        productionResult.add(new Epsilon());
                    } else if (elementValue.matches("\".*\"")) {
                        Terminal terminal = new Terminal(elementValue.substring(1, elementValue.length() - 1));
                        terminals.add(terminal);
                        productionResult.add(terminal);
                    } else {
                        NonTerminal nonTerminal = new NonTerminal(elementValue);
                        nonTerminals.add(nonTerminal);
                        productionResult.add(nonTerminal);
                    }
                }

                if (productionSource.isEmpty())
                    throw new IOException("The production source cannot be empty");
                if (productionSource.stream().noneMatch(element -> element instanceof NonTerminal))
                    throw new IOException("The production source cannot be composed solely of terminals");
                if (productionResult.isEmpty())
                    throw new IOException("The production result cannot be empty");

                productions.add(new Production(
                        productionSource,
                        productionResult
                ));
            }
        }
    }

    public Set<NonTerminal> getNonTerminals() {
        return nonTerminals;
    }

    public Set<Terminal> getTerminals() {
        return terminals;
    }

    public List<Production> getProductions() {
        return productions;
    }

    public List<Production> getProductionsFor(String nonTerminal) {
        return productions.stream()
                .filter(production -> production.sourceElements.stream()
                        .anyMatch(element -> Objects.equals(element.value, nonTerminal)))
                .toList();
    }

    public boolean isCFG() {
        return productions.stream()
                .allMatch(production ->
                        production.sourceElements.size() == 1 &&
                        production.sourceElements.get(0) instanceof NonTerminal);
    }
}
