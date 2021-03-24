package org.lobertrand.examples;

import java.util.List;

import static java.lang.Integer.parseInt;
import static org.lobertrand.parser.Parsers.*;

public class Diceroll {

    static class Node<T> {
        String type;
        T value;

        Node(String type, T value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "type='" + type + '\'' +
                    ", value=" + value +
                    '}';
        }
    }

    public static void main(String[] args) {
        var stringParser = letters.map(result -> new Node<>("string", result));
        var numberParser = digits.map(result -> new Node<>("number", parseInt(result)));
        var dicerollParser = sequenceOf(
                digits,
                str("d"),
                digits
        ).map(results -> new Node<>(
                "diceroll",
                List.of(parseInt(results.a), parseInt(results.c)))
        );

        var parser = sequenceOf(letters, str(":"))
                .map(results -> results.a)
                .chain(type -> {
                    if (type.equals("string")) {
                        return stringParser;
                    } else if (type.equals("number")) {
                        return numberParser;
                    }
                    return dicerollParser;
                });

        System.out.println(parser.run("string:hello"));
        System.out.println(parser.run("number:42"));
        System.out.println(parser.run("diceroll:2d8"));
    }
}
