package fr.combine.parser;

public class LateInitParser<T> extends Parser<T> {

    private boolean initialized = false;

    LateInitParser() {
        super(state -> {
            throw new IllegalStateException("LateInitParser not yet initialized");
        });
    }

    public void is(Parser<T> parser) {
        if (initialized) {
            throw new IllegalStateException("LateInitParser already initialized");
        }
        stateTransformer = parser.stateTransformer;
        initialized = true;
    }
}
