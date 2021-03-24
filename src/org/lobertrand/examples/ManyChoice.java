package org.lobertrand.examples;


import static org.lobertrand.parser.Parsers.*;

public class ManyChoice {

    public static void main(String[] args) {
        var parser = many(choice(
                digits.map(Integer::parseInt),
                letters
        ));

        System.out.println(
                parser.run("123abc456def789")
        );

    }
}
