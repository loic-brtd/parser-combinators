package fr.combine.parser;

import fr.combine.tuple.*;
import fr.combine.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;


public final class Parsers {

    private static final Parser<String> LETTER_PARSER = regex(Pattern.compile("^[A-Za-z]"), "letter");
    private static final Parser<String> LETTERS_PARSER = regex(Pattern.compile("^[A-Za-z]+"), "letters");
    private static final Parser<String> DIGIT_PARSER = regex(Pattern.compile("^[0-9]"), "digit");
    private static final Parser<String> DIGITS_PARSER = regex(Pattern.compile("^[0-9]+"), "digits");

    private static final Parser<Void> END_OF_INPUT_PARSER = new Parser<>(state -> {
        if (state.isError()) return state.withSameError();

        if (state.index < state.target.length()) {
            return state.withError(fail("endOfInput", state, "Expected end of input but it wasn't"));
        }
        return state.withResult(null);
    });

    public static Parser<Void> endOfInput() {
        return END_OF_INPUT_PARSER;
    }

    public static Parser<String> letter() {
        return LETTER_PARSER;
    }

    public static Parser<String> letters() {
        return LETTERS_PARSER;
    }

    public static Parser<String> digit() {
        return DIGIT_PARSER;
    }

    public static Parser<String> digits() {
        return DIGITS_PARSER;
    }

    private static Parser<String> regex(Pattern pattern, String name) {
        return new Parser<>(state -> {
            if (state.isError()) return state.withSameError();

            var rest = Util.slice(state.target, state.index);
            var matcher = pattern.matcher(rest);
            var result = matcher.results().map(MatchResult::group).findFirst();

            return result
                    .map(match -> state.withResult(match, state.index + match.length()))
                    .orElse(state.withError(fail(name, state, "Tried to match '%s', but got '%s'", name, Util.slice(rest, 10))));
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
            if (state.isError()) return state.withSameError();

            var rest = Util.slice(state.target, state.index);
            if (rest.length() < 1) {
                return state.withError(fail("str", state, "Tried to match '%s', but got end of input", str));
            }
            if (!rest.startsWith(str)) {
                return state.withError(fail("str", state, "Tried to match '%s', but got '%s'",
                        str, Util.slice(rest, 0, str.length())));
            }
            return state.withResult(str, state.index + str.length());
        });
    }

    public static Parser<Character> chr(char c) {
        return new Parser<>(state -> {
            if (state.isError()) return state.withSameError();

            var rest = Util.slice(state.target, state.index);
            if (rest.length() < 1) {
                return state.withError(fail("chr", state, "Tried to match '%c', but got end of input", c));
            }
            if (rest.charAt(0) != c) {
                return state.withError(fail("chr", state, "Tried to match '%c', but got '%c'", c, rest.charAt(0)));
            }
            return state.withResult(c, state.index + 1);
        });
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <A> Parser<Mono<A>> sequenceOf(Parser<A> a) {
        return (Parser) sequenceOf(List.of(a)).map(Util::dynamicTuple);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <A, B> Parser<Pair<A, B>> sequenceOf(Parser<A> a, Parser<B> b) {
        return (Parser) sequenceOf(List.of(a, b)).map(Util::dynamicTuple);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <A, B, C> Parser<Triplet<A, B, C>> sequenceOf(Parser<A> a, Parser<B> b, Parser<C> c) {
        return (Parser) sequenceOf(List.of(a, b, c)).map(Util::dynamicTuple);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <A, B, C, D> Parser<Quartet<A, B, C, D>> sequenceOf(Parser<A> a, Parser<B> b, Parser<C> c, Parser<D> d) {
        return (Parser) sequenceOf(List.of(a, b, c, d)).map(Util::dynamicTuple);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <A, B, C, D, E> Parser<Quintet<A, B, C, D, E>> sequenceOf(Parser<A> a, Parser<B> b, Parser<C> c, Parser<D> d, Parser<E> e) {
        return (Parser) sequenceOf(List.of(a, b, c, d, e)).map(Util::dynamicTuple);
    }

    @SuppressWarnings("unchecked")
    public static <T> Parser<List<T>> sequenceOf(Iterable<Parser<? extends T>> parsers) {
        return new Parser<>(state -> {
            if (state.isError()) return state.withSameError();

            var results = new ArrayList<T>();
            var nextState = (State<? extends T>) state;

            for (var p : parsers) {
                var out = p.stateTransformer.apply(nextState);

                if (out.isError()) {
                    return out.withSameError();
                } else {
                    nextState = out;
                    results.add(nextState.result);
                }
            }

            return nextState.withResult(results);
        });
    }

    @SuppressWarnings("unchecked")
    public static <T> Parser<T> optional(Parser<? extends T> parser) {
        return new Parser<>(state -> {
            if (state.isError()) return state.withSameError();

            try {
                return (State<T>) parser.stateTransformer.apply(state);
            } catch (ParseException e) {
                return state.withResult(null);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static <T> Parser<T> anyOf(Iterable<Parser<? extends T>> parsers) {
        if (Util.hasLessThanTwoElements(parsers)) {
            throw new IllegalArgumentException("choice() should take at least two parsers as input");
        }
        return new Parser<>(state -> {
            if (state.isError()) return state.withSameError();

            for (var p : parsers) {
                try {
                    return (State<T>) p.stateTransformer.apply(state);
                } catch (ParseException e) {
                    // Trying with next parser in the loop
                }
            }
            return state.withError(fail("anyOf", state, "Unable to match with any parser"));
        });
    }

    @SafeVarargs
    public static <T> Parser<T> anyOf(Parser<? extends T>... parsers) {
        return anyOf(Arrays.asList(parsers));
    }

    @SuppressWarnings("unchecked")
    public static <T> Parser<List<T>> zeroOrMore(Parser<? extends T> parser) {
        return new Parser<>(state -> {
            if (state.isError()) return state.withSameError();

            var results = new ArrayList<T>();
            var nextState = (State<? extends T>) state;
            while (true) {
                var out = parser.stateTransformer.apply(nextState);
                if (out.isError()) break;
                // nextState is only updated if parser execution didn't fail
                nextState = out;
                results.add(nextState.result);
            }
            return nextState.withResult(results);
        });
    }

    @SuppressWarnings("unchecked")
    public static <T> Parser<List<T>> oneOrMore(Parser<? extends T> parser) {
        return new Parser<>(state -> {
            if (state.isError()) return state.withSameError();

            var results = new ArrayList<T>();
            var nextState = (State<? extends T>) state;
            while (true) {
                var out = parser.stateTransformer.apply(nextState);
                if (out.isError()) break;

                // nextState is only updated if parser execution didn't fail
                nextState = out;
                results.add(nextState.result);
            }

            if (results.isEmpty()) {
                return state.withError(fail("oneOrMore", state, "Unable to match input using parser"));
            }

            return nextState.withResult(results);
        });
    }

    public static <T> Function<Parser<? extends T>, Parser<T>> between(Parser<?> leftParser, Parser<?> rightParser) {
        return contentParser -> sequenceOf(
                leftParser,
                contentParser,
                rightParser
        ).map(Triplet::second);
    }

    public static <T> Function<Parser<T>, Parser<List<T>>> sepBy(Parser<?> separatorParser) {
        return valueParser -> new Parser<>(state -> sepByAux(valueParser, separatorParser, state));
    }

    public static <T> Parser<List<T>> sepBy(Parser<T> valueParser, Parser<?> separatorParser) {
        return new Parser<>(state -> sepByAux(valueParser, separatorParser, state));
    }

    private static <T> State<List<T>> sepByAux(Parser<T> valueParser, Parser<?> separatorParser, State<?> state) {
        if (state.isError()) return state.withSameError();

        var nextState = state;
        var error = (State<? extends T>) null;
        var results = new ArrayList<T>();

        while (true) {
            var valState = valueParser.stateTransformer.apply(nextState);
            var sepState = separatorParser.stateTransformer.apply(valState);

            if (valState.isError()) {
                error = valState;
                break;
            } else {
                results.add(valState.result);
            }

            if (sepState.isError()) {
                nextState = valState;
                break;
            }

            nextState = sepState;
        }

        if (error != null) {
            if (results.isEmpty()) {
                return state.withResult(results);
            }
            return error.withSameError();
        }

        return nextState.withResult(results);
    }

    public static <T> Function<Parser<T>, Parser<List<T>>> atLeastOneSepBy(Parser<?> separatorParser) {
        return valueParser -> new Parser<>(state -> atLeastOneSepByAux(valueParser, separatorParser, state));
    }

    public static <T> Parser<List<T>> atLeastOneSepBy(Parser<T> valueParser, Parser<?> separatorParser) {
        return new Parser<>(state -> atLeastOneSepByAux(valueParser, separatorParser, state));
    }

    private static <T> State<List<T>> atLeastOneSepByAux(Parser<T> valueParser, Parser<?> separatorParser, State<?> state) {
        if (state.isError()) return state.withSameError();

        var out = sepBy(valueParser, separatorParser).stateTransformer.apply(state);
        if (out.isError()) return out.withSameError();

        if (out.result.isEmpty()) {
            return state.withError(fail("atLeastOneSepBy", state, "Unable to capture any results"));
        }
        return out;
    }

    public static <T> Parser<T> lazy(Supplier<Parser<T>> parserSupplier) {
        return new Parser<>(state -> {
            if (state.isError()) return state.withSameError();

            var parser = parserSupplier.get();
            return parser.stateTransformer.apply(state);
        });
    }

    public static <T> LateInitParser<T> lateInit() {
        return new LateInitParser<>();
    }

    private static ParseException error(String name, State<?> state, String message, Object... args) {
        var formattedMsg = Util.escapeString(name) + ": " + String.format(message, args) + " at index " + state.index;
        return new ParseException(formattedMsg, state);
    }

    private static String fail(String name, State<?> state, String message, Object... args) {
        return Util.escapeString(name) + ": " + String.format(message, args) + " at index " + state.index;
    }

}
