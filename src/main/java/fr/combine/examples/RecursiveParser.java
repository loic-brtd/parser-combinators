package fr.combine.examples;


import fr.combine.parser.Parsers;
import fr.combine.tuple.Triplet;

import static fr.combine.parser.Parsers.*;

public class RecursiveParser {

    public static void main(String[] args) {
        var bracketed = Parsers.<Integer>between(str("("), str(")"));
        var integer = digits().map(Integer::parseInt);

        var expr = Parsers.<Integer>lateInit();
        var operation = sequenceOf(expr, regex("^[-+*/]+"), expr)
                .map(RecursiveParser::evaluate);
        var group = bracketed.apply(operation);
        expr.is(anyOf(integer, group));

        System.out.println(expr.run("(456+((6*3)-6))"));
    }

    private static int evaluate(Triplet<Integer, String, Integer> results) {
        var operator = results.second();
        switch (operator) {
            case "-":
                return results.first() - results.third();
            case "+":
                return results.first() + results.third();
            case "/":
                return results.first() / results.third();
            case "*":
                return results.first() * results.third();
        }
        throw new IllegalStateException();
    }
}
