package fr.combine.examples;

import fr.combine.parser.Parsers;
import fr.combine.tuple.Tuple;

import java.util.Objects;
import java.util.stream.Collectors;

public class JsonSpec {

    public static void main(String[] args) {

        // Trying to follow this : https://www.crockford.com/mckeeman.html

        var ws = Parsers.regex("[ \n\r\t]*");

//        var digits = Parsers.<String>lateInit();
//        var sign = possibly(choice(str("+"), str("-")));
//        var exponent = possibly(sequenceOf(choice(str("E"), str("e")), sign, digits));
//        var fraction = possibly(sequenceOf(str("."), digits));
//        var oneNine = regex("[1-9]");
//        var digit = regex("[0-9]");
//        digits.is(many1(digit).map(JsonSpec::concat));
//        var integer = sequenceOf(
//                possibly(str("-")),
//                choice(
//                        sequenceOf(oneNine, digits).map(JsonSpec::concat),
//                        digit
//                )
//        );
//        var number = sequenceOf(integer, fraction, exponent).map(JsonSpec::concat);
//
//        System.out.println(number.run("058495.09E+9"));


        var number = Parsers.regex("-?(?:0|[1-9]\\d*)(?:\\.\\d+)?(?:[eE][+-]?\\d+)?");
        var parser = Parsers.sequenceOf(ws, number, ws, Parsers.endOfInput());

        System.out.println(parser.run("  -456.565E-9  "));

    }

    private static String concat(Tuple tuple) {
        return tuple.stream()
                    .filter(Objects::nonNull)
                    .map(String::valueOf)
                    .collect(Collectors.joining());
    }

    private static String concat(Iterable<?> iterable) {
        var sb = new StringBuilder();
        for (Object o : iterable) {
            sb.append(o);
        }
        return sb.toString();
    }

}
