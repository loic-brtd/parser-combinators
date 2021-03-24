package org.lobertrand.examples;


import org.lobertrand.parser.Parsers;
import org.lobertrand.parser.Tuple;

import java.util.List;

import static org.lobertrand.parser.Parsers.*;

public class Json {


    public static void main(String[] args) {

        var ws = regex("[ \n\r\t]*");
        var sepByComma = sepBy(sequenceOf(ws, str(","), ws));

        var number = digits.map(Integer::parseInt);
        var string = sequenceOf(str("\""), letters, str("\""))
                .map(parts -> parts.b);

        var value = Parsers.lateInit();

        var entry = sequenceOf(string, ws, str(":"), ws, value)
                .map(parts -> Tuple.of(parts.a, parts.e));
        var entries = sepByComma.apply(entry);
        var object = sequenceOf(str("{"), ws, entries, ws, str("}"))
                .map(parts -> parts.c);
        var array = sequenceOf(str("["), sepByComma.apply(value), str("]"))
                .map(parts -> parts.b);

        value.is(sequenceOf(
                ws,
                choice(number, string, object, array),
                ws
        ).map(parts -> parts.b));

        var test = "{\n" +
                "  \"hello\": 12" +
                ",\n" +
                "  \"yay\": [\n" +
                "    1,\n" +
                "    {\n" +
                "      \"key\": \"value\"\n" +
                "    },\n" +
                "    3\n" +
                "  ],\n" +
                "  \"yo\": {\n" +
                "    \"wow\": 45\n" +
                "  }\n" +
                "}";
        System.out.println(value.run(test));
    }

}
