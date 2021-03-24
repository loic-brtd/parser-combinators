package org.lobertrand.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import static org.lobertrand.parser.Utils.hasLessThanTwoElements;
import static org.lobertrand.parser.Utils.slice;

public final class Parsers {

    private static final Pattern LETTERS_PATTERN = Pattern.compile("^[A-Za-z]+");
    private static final Pattern DIGITS_PATTERN = Pattern.compile("^[0-9]+");

    public static final Parser<String> letters = regex(LETTERS_PATTERN, "letters");
    public static final Parser<String> digits = regex(DIGITS_PATTERN, "digits");

    private static Parser<String> regex(Pattern pattern, String name) {
        return new Parser<>(state -> {
            var rest = slice(state.target, state.index);
            var matcher = pattern.matcher(rest);
            var result = matcher.results().map(MatchResult::group).findFirst();
            return result
                    .map(match -> state.withResult(match, state.index + match.length()))
                    .orElseThrow(() -> error(name, state, "Tried to match '%s', but got '%s'", name, slice(rest, 10)));
        });
    }

    public static Parser<String> regex(String regex) {
        if (regex.charAt(0) != '^') {
            regex = '^' + regex;
        }
        return regex(Pattern.compile(regex), regex);
    }

    public static Parser<String> str(String str) {
        if (str.length() == 0) {
            throw new IllegalArgumentException("str() must be called with a non-empty String");
        }
        return new Parser<>(state -> {
            var rest = slice(state.target, state.index);
            if (rest.length() < 1) {
                throw error("str", state, "Tried to match '%s', but got end of input", str);
            }
            if (!rest.startsWith(str)) {
                throw error("str", state, "Tried to match '%s', but got '%s'",
                        str, slice(rest, 0, str.length()));
            }
            return state.withResult(str, state.index + str.length());
        });
    }

    public static Parser<Character> chr(char c) {
        return new Parser<>(state -> {
            var rest = slice(state.target, state.index);
            if (rest.length() < 1) {
                throw error("char", state, "Tried to match '%c', but got end of input", c);
            }
            if (rest.charAt(0) != c) {
                throw error("char", state, "Tried to match '%c', but got '%c'", c, rest.charAt(0));
            }
            return state.withResult(c, state.index + 1);
        });
    }

    public static <A, B> Parser<Tuple.S2<A, B>> sequenceOf(Parser<A> a, Parser<B> b) {
        return new Parser<>(parserState -> {
            var sa = a.stateTransformer.apply(parserState);
            var sb = b.stateTransformer.apply(sa);
            return sb.withResult(Tuple.of(sa.result, sb.result));
        });
    }

    public static <A, B, C> Parser<Tuple.S3<A, B, C>> sequenceOf(Parser<A> a, Parser<B> b, Parser<C> c) {
        return new Parser<>(parserState -> {
            var sa = a.stateTransformer.apply(parserState);
            var sb = b.stateTransformer.apply(sa);
            var sc = c.stateTransformer.apply(sb);
            return sc.withResult(Tuple.of(sa.result, sb.result, sc.result));
        });
    }

    public static <A, B, C, D> Parser<Tuple.S4<A, B, C, D>> sequenceOf(Parser<A> a, Parser<B> b, Parser<C> c, Parser<D> d) {
        return new Parser<>(parserState -> {
            var sa = a.stateTransformer.apply(parserState);
            var sb = b.stateTransformer.apply(sa);
            var sc = c.stateTransformer.apply(sb);
            var sd = d.stateTransformer.apply(sc);
            return sd.withResult(Tuple.of(sa.result, sb.result, sc.result, sd.result));
        });
    }

    public static <A, B, C, D, E> Parser<Tuple.S5<A, B, C, D, E>> sequenceOf(Parser<A> a, Parser<B> b, Parser<C> c, Parser<D> d, Parser<E> e) {
        return new Parser<>(parserState -> {
            var sa = a.stateTransformer.apply(parserState);
            var sb = b.stateTransformer.apply(sa);
            var sc = c.stateTransformer.apply(sb);
            var sd = d.stateTransformer.apply(sc);
            var se = e.stateTransformer.apply(sd);
            return se.withResult(Tuple.of(sa.result, sb.result, sc.result, sd.result, se.result));
        });
    }

    @SuppressWarnings("unchecked")
    public static <T> Parser<List<T>> sequenceOf(Iterable<Parser<? extends T>> parsers) {
        return new Parser<>(state -> {
            var results = new ArrayList<T>();
            var nextState = (State<? extends T>) state;
            for (var p : parsers) {
                nextState = p.stateTransformer.apply(nextState);
                results.add(nextState.result);
            }
            return nextState.withResult(List.copyOf(results));
        });
    }

    @SuppressWarnings("unchecked")
    public static <T> Parser<T> possibly(Parser<? extends T> parser) {
        return new Parser<>(state -> {
            try {
                return (State<T>) parser.stateTransformer.apply(state);
            } catch (ParseException e) {
                return state.withResult(null);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static <T> Parser<T> choice(Iterable<Parser<? extends T>> parsers) {
        if (hasLessThanTwoElements(parsers)) {
            throw new IllegalArgumentException("choice() should take at least two parsers as input");
        }
        return new Parser<>(state -> {
            for (var p : parsers) {
                try {
                    return (State<T>) p.stateTransformer.apply(state);
                } catch (ParseException e) {
                    // Trying with next parser in the loop
                }
            }
            throw error("choice", state, "Unable to match with any parser");
        });
    }

    @SafeVarargs
    public static <T> Parser<T> choice(Parser<? extends T>... parsers) {
        return choice(Arrays.asList(parsers));
    }

    @SuppressWarnings("unchecked")
    public static <T> Parser<List<T>> many(Parser<? extends T> parser) {
        return new Parser<>(state -> {
            var results = new ArrayList<T>();
            var nextState = (State<? extends T>) state;
            while (true) {
                try {
                    // nextState is only updated if parser execution didn't fail with an Exception
                    nextState = parser.stateTransformer.apply(nextState);
                    results.add(nextState.result);
                } catch (ParseException e) {
                    break;
                }
            }
            return nextState.withResult(List.copyOf(results));
        });
    }

    @SuppressWarnings("unchecked")
    public static <T> Parser<List<T>> many1(Parser<? extends T> parser) {
        return new Parser<>(state -> {
            var results = new ArrayList<T>();
            var nextState = (State<? extends T>) state;
            while (true) {
                try {
                    // nextState is only updated if parser execution didn't fail with an Exception
                    nextState = parser.stateTransformer.apply(nextState);
                    results.add(nextState.result);
                } catch (ParseException e) {
                    break;
                }
            }

            if (results.isEmpty()) {
                throw error("many1", state, "Unable to match input using parser");
            }

            return nextState.withResult(List.copyOf(results));
        });
    }

    public static <T> Function<Parser<? extends T>, Parser<T>> between(Parser<?> leftParser, Parser<?> rightParser) {
        return contentParser -> sequenceOf(
                leftParser,
                contentParser,
                rightParser
        ).map(tuple -> tuple.b);
    }

    @SuppressWarnings("unchecked")
    public static <T> Function<Parser<? extends T>, Parser<List<T>>> sepBy(Parser<?> separatorParser) {
        return valueParser -> new Parser<>(state -> {
            var results = new ArrayList<T>();
            var nextState = state;

            while (true) {
                try {
                    // nextState is only updated if parser execution didn't fail with an Exception
                    nextState = valueParser.stateTransformer.apply(nextState);
                    results.add((T) nextState.result);
                    // same here
                    nextState = separatorParser.stateTransformer.apply(nextState);
                } catch (ParseException e) {
                    break;
                }
            }

            return nextState.withResult(List.copyOf(results));
        });
    }

    @SuppressWarnings("unchecked")
    public static <T> Function<Parser<? extends T>, Parser<List<T>>> sepBy1(Parser<?> separatorParser) {
        return valueParser -> new Parser<>(state -> {
            var results = new ArrayList<T>();
            var nextState = state;

            while (true) {
                try {
                    // nextState is only updated if parser execution didn't fail with an Exception
                    nextState = valueParser.stateTransformer.apply(nextState);
                    results.add((T) nextState.result);
                    // same here
                    nextState = separatorParser.stateTransformer.apply(nextState);
                } catch (ParseException e) {
                    break;
                }
            }

            if (results.isEmpty()) {
                throw error("sepBy1", state, "Unable to capture any results");
            }

            return nextState.withResult(List.copyOf(results));
        });
    }

    public static <T> Parser<T> lazy(Supplier<Parser<T>> parserSupplier) {
        return new Parser<>(state -> {
            var parser = parserSupplier.get();
            return parser.stateTransformer.apply(state);
        });
    }

    public static <T> LateInitParser<T> lateInit() {
        return new LateInitParser<>();
    }

    private static ParseException error(String name, State<?> state, String message, Object... args) {
        var formattedMsg = Utils.escapeString(name) + ": " + String.format(message, args) + " at index " + state.index;
        return new ParseException(formattedMsg, state);
    }

}
