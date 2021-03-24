package org.lobertrand.parser;

import java.util.List;
import java.util.stream.Stream;

public interface Tuple {

    class S2<A, B> implements Tuple {
        public final A a;
        public final B b;

        private S2(A a, B b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public String toString() {
            return "(" + a + ", " + b + ')';
        }

        @Override
        public Stream<Object> stream() {
            return Stream.of(a, b);
        }

        @Override
        public List<Object> toList() {
            return List.of(a, b);
        }
    }

    class S3<A, B, C> implements Tuple {
        public final A a;
        public final B b;
        public final C c;

        private S3(A a, B b, C c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        @Override
        public String toString() {
            return "(" + a + ", " + b + ", " + c + ')';
        }

        @Override
        public Stream<Object> stream() {
            return Stream.of(a, b, c);
        }

        @Override
        public List<Object> toList() {
            return List.of(a, b, c);
        }
    }

    class S4<A, B, C, D> implements Tuple {
        public final A a;
        public final B b;
        public final C c;
        public final D d;

        private S4(A a, B b, C c, D d) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
        }

        @Override
        public String toString() {
            return "(" + a + ", " + b + ", " + c + ", " + d + ')';
        }

        @Override
        public Stream<Object> stream() {
            return Stream.of(a, b, c, d);
        }

        @Override
        public List<Object> toList() {
            return List.of(a, b, c, d);
        }
    }

    class S5<A, B, C, D, E> implements Tuple {
        public final A a;
        public final B b;
        public final C c;
        public final D d;
        public final E e;

        private S5(A a, B b, C c, D d, E e) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
        }

        @Override
        public String toString() {
            return "(" + a + ", " + b + ", " + c + ", " + d + ", " + e + ')';
        }

        @Override
        public Stream<Object> stream() {
            return Stream.of(a, b, c, d, e);
        }

        @Override
        public List<Object> toList() {
            return List.of(a, b, c, d, e);
        }
    }

    Stream<Object> stream();

    List<Object> toList();

    static <A, B> S2<A, B> of(A a, B b) {
        return new S2<>(a, b);
    }

    static <A, B, C> S3<A, B, C> of(A a, B b, C c) {
        return new S3<>(a, b, c);
    }

    static <A, B, C, D> S4<A, B, C, D> of(A a, B b, C c, D d) {
        return new S4<>(a, b, c, d);
    }

    static <A, B, C, D, E> S5<A, B, C, D, E> of(A a, B b, C c, D d, E e) {
        return new S5<>(a, b, c, d, e);
    }
}
