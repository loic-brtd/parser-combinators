package fr.combine.tuple;

import fr.combine.util.Util;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Quartet<A, B, C, D> implements Tuple {
    public final A first;
    public final B second;
    public final C third;
    public final D fourth;

    Quartet(A first, B second, C third, D fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
    }

    public A first() {
        return first;
    }

    public B second() {
        return second;
    }

    public C third() {
        return third;
    }

    public D fourth() {
        return fourth;
    }

    @Override
    public String toString() {
        return "("
                + Util.repr(first) + ", "
                + Util.repr(second) + ", "
                + Util.repr(third) + ", "
                + Util.repr(fourth) + ')';
    }

    @Override
    public Stream<Object> stream() {
        return Stream.of(first, second, third, fourth);
    }

    @Override
    public List<Object> toList() {
        return Arrays.asList(first, second, third, fourth);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quartet<?, ?, ?, ?> quartet = (Quartet<?, ?, ?, ?>) o;
        return Objects.equals(first, quartet.first) && Objects.equals(second, quartet.second) && Objects.equals(third, quartet.third) && Objects.equals(fourth, quartet.fourth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, third, fourth);
    }
}
