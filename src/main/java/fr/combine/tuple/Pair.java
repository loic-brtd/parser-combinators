package fr.combine.tuple;

import fr.combine.util.Util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Pair<A, B> implements Tuple {
    public final A first;
    public final B second;

    Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A first() {
        return first;
    }

    public B second() {
        return second;
    }

    @Override
    public String toString() {
        return "("
                + Util.repr(first) + ", "
                + Util.repr(second) + ')';
    }

    @Override
    public Stream<Object> stream() {
        return Stream.of(first, second);
    }

    @Override
    public List<Object> toList() {
        return Arrays.asList(first, second);
    }

}
