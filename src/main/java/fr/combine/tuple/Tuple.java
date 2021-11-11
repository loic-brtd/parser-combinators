package fr.combine.tuple;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public interface Tuple extends Iterable<Object> {

    List<Object> toList();

    default Stream<Object> stream() {
        return toList().stream();
    }

    @Override
    default Iterator<Object> iterator() {
        return toList().iterator();
    }

    static <A> Mono<A> of(A first) {
        return new Mono<>(first);
    }

    static <A, B> Pair<A, B> of(A first, B second) {
        return new Pair<>(first, second);
    }

    static <A, B, C> Triplet<A, B, C> of(A first, B second, C third) {
        return new Triplet<>(first, second, third);
    }

    static <A, B, C, D> Quartet<A, B, C, D> of(A first, B second, C third, D fourth) {
        return new Quartet<>(first, second, third, fourth);
    }

    static <A, B, C, D, E> Quintet<A, B, C, D, E> of(A first, B second, C third, D fourth, E fifth) {
        return new Quintet<>(first, second, third, fourth, fifth);
    }

}
