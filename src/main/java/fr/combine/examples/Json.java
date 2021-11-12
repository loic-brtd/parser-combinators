package fr.combine.examples;


import fr.combine.tuple.Quintet;
import fr.combine.tuple.Triplet;
import fr.combine.tuple.Tuple;

import static fr.combine.parser.Parsers.*;

public class Json {


    public static void main(String[] args) {

        var ws = regex("[ \n\r\t]*");
        var sepByComma = sepBy(sequenceOf(ws, str(","), ws));

        var number = digits().map(Integer::parseInt);
        var string = sequenceOf(str("\""), letters(), str("\""))
                .map(Triplet::second);

        var value = lateInit();

        var entry = sequenceOf(string, ws, str(":"), ws, value)
                .map(parts -> Tuple.of(parts.first(), parts.fifth()));
        var entries = sepByComma.apply(entry);
        var object = sequenceOf(str("{"), ws, entries, ws, str("}"))
                .map(Quintet::third);
        var array = sequenceOf(str("["), sepByComma.apply(value), str("]"))
                .map(Triplet::second);

        value.is(sequenceOf(
                ws,
                anyOf(number, string, object, array),
                ws
        ).map(Triplet::second));

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
