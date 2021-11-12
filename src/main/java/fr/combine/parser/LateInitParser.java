package fr.combine.parser;

import java.util.Objects;

public class LateInitParser<T> implements Parser<T> {

    private Parser<T> parser;

    public void is(Parser<T> parser) {
        Objects.requireNonNull(parser, "parser");

        if (this.parser != null) {
            throw new IllegalStateException("LateInitParser already initialized");
        }
        this.parser = parser;
    }

    @Override
    public State<T> run(State<?> state) {
        Objects.requireNonNull(state, "state");

        if (this.parser == null) {
            throw new IllegalStateException("LateInitParser not yet initialized");
        }
        return parser.run(state);
    }
}
