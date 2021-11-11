package fr.combine.examples;

import fr.combine.parser.Parsers;

public class SepBy {

    public static void main(String[] args) {

        var nested = "[1,[2,[3],4],5]";

        var betweenSquareBrackets = Parsers.between(Parsers.str("["), Parsers.str("]"));
        var commaSeparated = Parsers.sepBy(Parsers.str(","));

        var arrayParser = Parsers.lateInit();
        var value = Parsers.anyOf(
                Parsers.digits(),
                arrayParser
        );
        arrayParser.is(betweenSquareBrackets.apply(commaSeparated.apply(value)));

        System.out.println(
                arrayParser.run(nested)
        );

    }
}
