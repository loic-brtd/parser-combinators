package fr.combine.examples;


import fr.combine.parser.Parser;
import fr.combine.tuple.Pair;
import fr.combine.tuple.Quartet;
import fr.combine.tuple.Quintet;
import fr.combine.tuple.Triplet;
import fr.combine.util.Util;

import java.util.List;

import static fr.combine.parser.Parsers.*;

public class Calculator {

    public static void main(String[] args) {

        var integer = digits().map(s -> {
            System.out.println("digits to int = " + Util.repr(s));
            return Integer.parseInt(s);
        });
        var optSpacing = regex("\\s*");
        var operator = sequenceOf(List.of(
                optSpacing,
                str("+"),
                optSpacing
        ));
        Parser<Integer> parser = sequenceOf(
                optSpacing,
                sepBy(integer, operator)
                        .map(Calculator::sumIntegers),
                optSpacing,
                endOfInput()
        ).map(Quartet::second);
        var result = parser.run("30+ 45 + 2  ");
        System.out.println(result);

    }

    private static Integer sumIntegers(List<Integer> integers) {
        return integers.stream().reduce(0, Integer::sum);
    }
}
