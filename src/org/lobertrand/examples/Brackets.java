package org.lobertrand.examples;


import org.lobertrand.parser.Parsers;

import static org.lobertrand.parser.Parsers.*;

public class Brackets {

    public static void main(String[] args) {
        var betweenBrackets = Parsers.<String>between(str("("), str(")"));
        var digitsParser = betweenBrackets.apply(digits);
        System.out.println(digitsParser.run("(123)"));

        System.out.println(
                choice(
                        str("hello"),
                        digits,
                        str("goodbye")
                ).run("8967465ezdfze")
        );
    }
}
