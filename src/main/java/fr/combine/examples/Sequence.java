package fr.combine.examples;

import fr.combine.parser.Parsers;

import java.util.List;

public class Sequence {

    public static void main(String[] args) {
        var parser = Parsers.sequenceOf(List.of(
                Parsers.str("hi"),
                Parsers.digits().map(Integer::parseInt)
        ));

        System.out.println(
                parser.run("hi456")
        );
    }
}
