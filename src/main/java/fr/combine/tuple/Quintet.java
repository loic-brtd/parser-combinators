package fr.combine.tuple;

import fr.combine.util.Util;

import java.util.Arrays;
import java.util.List;
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
}
