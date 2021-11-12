package fr.combine.examples;


import java.util.List;

import static fr.combine.parser.Parsers.*;

public class Calculator {

    public static void main(String[] args) {

        var integer = digits().map(Integer::parseInt);
        var optSpacing = regex("\\s*");
        var operator = sequenceOf(List.of(
                optSpacing,
                str("+"),
                optSpacing
        ));
        var parser = sequenceOf(
                optSpacing,
                sepBy(integer, operator).map(Calculator::sumIntegers),
                optSpacing,
                endOfInput()
        ).map(elts -> elts.get(1));
        var result = parser.run("30+ 45 + 2  t");
        System.out.println(result);

    }

    private static Integer sumIntegers(List<Integer> integers) {
        return integers.stream().reduce(0, Integer::sum);
    }
}
