package org.lobertrand.parser;

public class State<T> {
    public final String target;
    public final T result;
    public final int index;

    public State(String target, T result, int index) {
        this.target = target;
        this.result = result;
        this.index = index;
    }

    public <U> State<U> withResult(U result) {
        return new State<>(target, result, index);
    }

    public <U> State<U> withResult(U result, int index) {
        return new State<>(target, result, index);
    }

    @Override
    public String toString() {
        var resultType = result == null ? "?" : result.getClass().getSimpleName();
        return String.format("State(target='%s', result='%s', type=%s, index=%d)",
                target, result, resultType, index);
    }
}