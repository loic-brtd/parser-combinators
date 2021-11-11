package fr.combine.tuple;

import fr.combine.util.Util;

import java.util.List;
import java.util.stream.Stream;

public class Triplet<A, B, C> implements Tuple {
    public final A first;
    public final B second;
    public final C third;

    Triplet(A first, B second, C third) {
        this.first = first;
        this.second = second;
        this.third = third;
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

    @Override
    public String toString() {
        return "("
                + Util.repr(first) + ", "
                + Util.repr(second) + ", "
                + Util.repr(third) + ')';
    }

    @Override
    public Stream<Object> stream() {
        return Stream.of(first, second, third);
    }

    @Override
    public List<Object> toList() {
        return List.of(first, second, third);
    }
}
