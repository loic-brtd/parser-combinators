package org.lobertrand.examples;

import java.util.List;

import static org.lobertrand.parser.Parsers.*;

public class Sequence {

    public static void main(String[] args) {
        var parser = sequenceOf(List.of(
                str("hi"),
                digits.map(Integer::parseInt)
        ));

        System.out.println(
                parser.run("hi456")
        );
    }
}
