package fr.combine.parser;

import fr.combine.util.Util;

public class State<T> {
    public final String target;
    public final T result;
    public final int index;
    public final String error;

    public State(String target, T result, int index, String error) {
        this.target = target;
        this.result = result;
        this.index = index;
        this.error = error;
    }

    public <U> State<U> withResult(U result) {
        return new State<>(target, result, index, null);
    }

    public <U> State<U> withResult(U result, int index) {
        return new State<>(target, result, index, null);
    }

    public <U> State<U> withError(String error) {
        return new State<>(target, null, index, error);
    }

    public <U> State<U> withSameError() {
        return new State<>(target, null, index, error);
    }

    public boolean isError() {
        return error != null;
    }

    @Override
    public String toString() {
        return "State{" +
                "target=" + Util.repr(target) +
                ", result=" + Util.repr(result) +
                ", index=" + index +
                ", error=" + Util.repr(error) +
                '}';
    }
}