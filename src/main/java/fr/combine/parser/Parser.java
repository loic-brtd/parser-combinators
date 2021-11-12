package fr.combine.parser;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface Parser<U> {

    State<U> run(State<?> state);

    default State<U> run(String target) {
        return this.run(new State<Void>(target, null, 0, null));
    }

    default <V> Parser<V> map(Function<U, V> mapper) {
        Objects.requireNonNull(mapper, "mapper");

        return state -> {
            if (state.isError()) {
                return state.withSameError();
            }

            var nextState = this.run(state);
            if (nextState.isError()) return nextState.withSameError();
            return nextState.withResult(mapper.apply(nextState.result));
        };
    }

    @SuppressWarnings("unchecked")
    default <V> Parser<? extends V> chain(Function<U, Parser<? extends V>> chainer) {
        Objects.requireNonNull(chainer, "chainer");

        return state -> {
            if (state.isError()) return state.withSameError();

            var nextState = this.run(state);
            if (nextState.isError()) return nextState.withSameError();

            var nextParser = chainer.apply(nextState.result);
            return (State<V>) nextParser.run(nextState);
        };
    }
}