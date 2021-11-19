package Parser;

public class Epsilon extends Element {
    public Epsilon() {
        super("$");
    }

    @Override
    public String toString() {
        return String.valueOf((char) 0x03B5);
    }
}
