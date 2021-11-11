package fr.combine.examples;

import fr.combine.parser.Parsers;
import fr.combine.tuple.Pair;

import java.util.List;

import static java.lang.Integer.parseInt;

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
        var stringParser = Parsers.letters().map(result -> new Node<>("string", result));
        var numberParser = Parsers.digits().map(result -> new Node<>("number", parseInt(result)));
        var dicerollParser = Parsers.sequenceOf(
                Parsers.digits(),
                Parsers.str("d"),
                Parsers.digits()
        ).map(results -> new Node<>(
                "diceroll",
                List.of(parseInt(results.first()), parseInt(results.third())))
        );

        var parser = Parsers.sequenceOf(Parsers.letters(), Parsers.str(":"))
                            .map(Pair::first)
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
