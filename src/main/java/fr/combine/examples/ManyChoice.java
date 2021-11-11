package fr.combine.examples;


import static fr.combine.parser.Parsers.*;

public class ManyChoice {

    public static void main(String[] args) {
        var parser = zeroOrMore(anyOf(
                digits().map(Integer::parseInt),
                letters()
        ));

        System.out.println(
                parser.run("123abc456def789")
        );

    }
}
