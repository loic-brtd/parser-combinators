package org.lobertrand.examples;

import org.lobertrand.parser.Parsers;

import java.util.List;

import static org.lobertrand.parser.Parsers.*;

public class JsonSpec {

    public static void main(String[] args) {

        // Trying to follow this : https://www.crockford.com/mckeeman.html

        var ws = regex("[ \n\r\t]*");

        var digits = Parsers.<List<String>>lateInit();
        var sign = possibly(choice(str("+"), str("-")));
        var exponent = possibly(sequenceOf(choice(str("+"), str("-")), sign, digits));
        var fraction = possibly(sequenceOf(str("."), digits));
        var oneNine = regex("[1-9]");
        var digit = regex("[0-9]");
        digits.is(many1(digit));
        var integer = sequenceOf(possibly(str("-")), choice(digit, sequenceOf(oneNine, digits)));
        var number = sequenceOf(integer, fraction, exponent);

        System.out.println(number.run("5849509"));
    }
}
