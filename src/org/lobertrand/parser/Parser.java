package org.lobertrand.parser;

import java.util.function.Function;

public class Parser<U> {

    protected Function<State<?>, State<U>> stateTransformer;

    public Parser(Function<State<?>, State<U>> parserStateTransformer) {
        this.stateTransformer = parserStateTransformer;
    }

    public State<U> run(String target) {
        var initialState = new State<>(target, null, 0);
        return this.stateTransformer.apply(initialState);
    }

    public <V> Parser<V> map(Function<U, V> fn) {
        return new Parser<>(parserState -> {
            var nextState = this.stateTransformer.apply(parserState);
            return nextState.withResult(fn.apply(nextState.result));
        });
    }

    public <V> Parser<? extends V> chain(Function<U, Parser<? extends V>> fn) {
        return new Parser<>(parserState -> {
            var nextState = this.stateTransformer.apply(parserState);

            var nextParser = fn.apply(nextState.result);

            return nextParser.stateTransformer.apply(nextState);
        });
    }
}