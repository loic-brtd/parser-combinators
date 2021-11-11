package fr.combine.parser;

import java.util.function.Function;

public class Parser<U> {

    protected Function<State<?>, State<U>> stateTransformer;

    public Parser(Function<State<?>, State<U>> parserStateTransformer) {
        this.stateTransformer = parserStateTransformer;
    }

    public State<U> run(String target) {
        State<Void> initialState = new State<>(target, null, 0, null);
        return this.stateTransformer.apply(initialState);
    }

    public State<U> run(State<?> initialState) {
        return this.stateTransformer.apply(initialState);
    }

    public <V> Parser<V> map(Function<U, V> fn) {
        return new Parser<>(state -> {
            if (state.isError()) return state.withSameError();

            var nextState = this.stateTransformer.apply(state);
            if (state.isError()) return state.withSameError();

            return nextState.withResult(fn.apply(nextState.result));
        });
    }

    public <V> Parser<? extends V> chain(Function<U, Parser<? extends V>> fn) {
        return new Parser<>(state -> {
            if (state.isError()) return state.withSameError();

            var nextState = this.stateTransformer.apply(state);
            if (nextState.isError()) return nextState.withSameError();

            var nextParser = fn.apply(nextState.result);
            return nextParser.stateTransformer.apply(nextState);
        });
    }
}