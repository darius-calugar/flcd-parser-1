package Parser;

public class ParseException extends RuntimeException {
    public ParseException(State state, Element element, String message) {
        super("parse exception at (" + state + "," + element + "): " + message);
    }
    public ParseException(State state, String message) {
        super("parse exception at (" + state + "): " + message);
    }
}
