package org.lobertrand.examples;


import static org.lobertrand.parser.Parsers.*;

public class RecursiveParser {

    public static void main(String[] args) {

        var bracketed = between(str("("), str(")"));

        var expr = lateInit();
        var group = bracketed.apply(
                sequenceOf(
                        expr,
                        regex("^[-+*/]+"),
                        expr
                )
        );
        expr.is(choice(digits, group));

        System.out.println(
                expr.run("(456+((6*3)-6))")
        );

//        .map(results -> {
//            var op = results.b;
//            switch (op) {
//                case "-":
//                    return results.a - results.c;
//                case "+":
//                    return results.a + results.c;
//                case "/":
//                    return results.a / results.c;
//                case "*":
//                    return results.a * results.c;
//            }
//            throw new IllegalStateException();
//        })

    }
}
