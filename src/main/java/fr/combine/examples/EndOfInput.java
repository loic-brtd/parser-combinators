package fr.combine.examples;

import static fr.combine.parser.Parsers.*;

public class EndOfInput {

    public static void main(String[] args) {
        var parser = sequenceOf(str("hello"), endOfInput());
        System.out.println(parser.run("hello"));
        System.out.println(parser.run("hello!"));
    }
}
