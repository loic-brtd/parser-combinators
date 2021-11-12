package fr.combine.parser;

import org.junit.jupiter.api.Test;

import java.util.List;

import static fr.combine.parser.Parsers.*;
import static org.assertj.core.api.Assertions.assertThat;

public class OtherTests {

    @Test
    public void addition_ok() {
        Parser<Integer> parser = addition_parser();
        var output = parser.run("15+ 25 + 2  ");

        assertThat(output.error).isNull();
        assertThat(output.result).isEqualTo(42);
        assertThat(output.index).isEqualTo(12);
    }

    @Test
    public void addition_withError() {
        Parser<Integer> parser = addition_parser();
        var output = parser.run("15+ 25 + 2  t");

        assertThat(output.error).isNotNull();
        assertThat(output.index).isEqualTo(12);
    }

    private Parser<Integer> addition_parser() {
        var integer = digits().map(Integer::parseInt);
        var optSpacing = regex("\\s*");
        var operator = sequenceOf(List.of(
                optSpacing,
                str("+"),
                optSpacing
        ));
        return sequenceOf(List.of(
                optSpacing,
                sepBy(integer, operator).map(this::sumIntegers),
                optSpacing,
                endOfInput()
        )).map(items -> (Integer) items.get(1));
    }

    private Integer sumIntegers(List<Integer> integers) {
        return integers.stream().reduce(0, Integer::sum);
    }

    @Test
    public void comaSeparatedIntegers_ok() {
        Parser<List<String>> parser = comaSeparatedIntegers_parser();

        var output = parser.run("12,13,14");
        System.out.println("output = " + output);

        assertThat(output.error).isNull();
        assertThat(output.result).isEqualTo(List.of("12", "13", "14"));
        assertThat(output.index).isEqualTo(8);
    }

    @Test
    public void comaSeparatedIntegers_withError() {
        Parser<List<String>> parser = comaSeparatedIntegers_parser();

        var output = parser.run("12,13,14t");

        assertThat(output.error).isNotNull();
        assertThat(output.index).isEqualTo(8);
    }

    private Parser<List<String>> comaSeparatedIntegers_parser() {
        return sequenceOf(
                sepBy(digits(), str(",")),
                endOfInput()
        ).map(items -> items.first);
    }

}
