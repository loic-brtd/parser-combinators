package fr.combine.examples;


import fr.combine.parser.Parsers;
import fr.combine.tuple.Quintet;
import fr.combine.tuple.Triplet;
import fr.combine.tuple.Tuple;

public class Json {


    public static void main(String[] args) {

        var ws = Parsers.regex("[ \n\r\t]*");
        var sepByComma = Parsers.sepBy(Parsers.sequenceOf(ws, Parsers.str(","), ws));

        var number = Parsers.digits().map(Integer::parseInt);
        var string = Parsers.sequenceOf(Parsers.str("\""), Parsers.letters(), Parsers.str("\""))
                            .map(Triplet::second);

        var value = Parsers.lateInit();

        var entry = Parsers.sequenceOf(string, ws, Parsers.str(":"), ws, value)
                           .map(parts -> Tuple.of(parts.first(), parts.fifth()));
        var entries = sepByComma.apply(entry);
        var object = Parsers.sequenceOf(Parsers.str("{"), ws, entries, ws, Parsers.str("}"))
                            .map(Quintet::third);
        var array = Parsers.sequenceOf(Parsers.str("["), sepByComma.apply(value), Parsers.str("]"))
                           .map(Triplet::second);

        value.is(Parsers.sequenceOf(
                ws,
                Parsers.anyOf(number, string, object, array),
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
