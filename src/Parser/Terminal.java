package Parser;

public class Terminal extends Element {
    public Terminal(String value) {
        super(value);
    }

    @Override
    public String toString() {
        return value.toLowerCase();
    }
}
