package Parser;

public class NonTerminal extends Element {
    public NonTerminal(String value) {
        super(value);
    }

    @Override
    public String toString() {
        return value.toUpperCase();
    }
}
