package fr.combine.examples;


import fr.combine.parser.Parsers;

import static fr.combine.parser.Parsers.*;

public class Brackets {

    public static void main(String[] args) {
        var betweenBrackets = Parsers.<String>between(str("("), str(")"));
        var digitsParser = betweenBrackets.apply(digits());
        System.out.println(digitsParser.run("(123)"));

        System.out.println(
                anyOf(
                        str("hello"),
                        digits(),
                        str("goodbye")
                ).run("8967465ezdfze")
        );
    }
}
