package org.lobertrand.parser;

public class ParseException extends RuntimeException {

    public final State<?> state;

    public ParseException(String message) {
        super(message);
        this.state = null;
    }

    public ParseException(String message, State<?> state) {
        super(message);
        this.state = state;
    }
}
