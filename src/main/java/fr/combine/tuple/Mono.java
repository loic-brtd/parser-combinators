package fr.combine.tuple;

import fr.combine.util.Util;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Mono<A> implements Tuple {
    public final A first;

    Mono(A first) {
        this.first = first;
    }

    public A first() {
        return first;
    }

    @Override
    public String toString() {
        return "(" + Util.repr(first) + ')';
    }

    @Override
    public Stream<Object> stream() {
        return Stream.of(first);
    }

    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    @Override
    public List<Object> toList() {
        return Arrays.asList(first);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mono<?> mono = (Mono<?>) o;
        return Objects.equals(first, mono.first);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first);
    }
}
