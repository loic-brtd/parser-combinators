package org.lobertrand.examples;

import org.lobertrand.parser.Parsers;

import static org.lobertrand.parser.Parsers.*;

public class SepBy {

    public static void main(String[] args) {

        var nested = "[1,[2,[3],4],5]";

        var betweenSquareBrackets = between(str("["), str("]"));
        var commaSeparated = Parsers.sepBy(str(","));

        var arrayParser = lateInit();
        var value = choice(
                digits,
                arrayParser
        );
        arrayParser.is(betweenSquareBrackets.apply(commaSeparated.apply(value)));

        System.out.println(
                arrayParser.run(nested)
        );

    }
}
