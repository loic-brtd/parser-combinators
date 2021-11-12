package fr.combine.tuple;

import fr.combine.util.Util;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Quintet<A, B, C, D, E> implements Tuple {
    public final A first;
    public final B second;
    public final C third;
    public final D fourth;
    public final E fifth;

    Quintet(A first, B second, C third, D fourth, E fifth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
        this.fifth = fifth;
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

    public E fifth() {
        return fifth;
    }

    @Override
    public String toString() {
        return "("
                + Util.repr(first) + ", "
                + Util.repr(second) + ", "
                + Util.repr(third) + ", "
                + Util.repr(fourth) + ", "
                + Util.repr(fifth) + ')';
    }

    @Override
    public Stream<Object> stream() {
        return Stream.of(first, second, third, fourth, fifth);
    }

    @Override
    public List<Object> toList() {
        return Arrays.asList(first, second, third, fourth, fifth);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quintet<?, ?, ?, ?, ?> quintet = (Quintet<?, ?, ?, ?, ?>) o;
        return Objects.equals(first, quintet.first) && Objects.equals(second, quintet.second) && Objects.equals(third, quintet.third) && Objects.equals(fourth, quintet.fourth) && Objects.equals(fifth, quintet.fifth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, third, fourth, fifth);
    }
}
