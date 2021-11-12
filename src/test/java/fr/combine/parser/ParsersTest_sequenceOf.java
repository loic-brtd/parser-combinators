package fr.combine.parser;

import fr.combine.tuple.Triplet;
import fr.combine.tuple.Tuple;
import org.junit.jupiter.api.Test;

import java.util.List;

import static fr.combine.parser.Parsers.sequenceOf;
import static fr.combine.parser.Parsers.str;
import static org.assertj.core.api.Assertions.assertThat;

class ParsersTest_sequenceOf {

    @Test
    public void sequenceOfStrings_ok() {
        var parser = sequenceOf(str("foo"), str("bar"), str("baz"));
        var output = parser.run("foobarbaz");

        assertThat(output.error).isNull();
        assertThat(output.result).isEqualTo(Tuple.of("foo", "bar", "baz"));
        assertThat(output.index).isEqualTo(9);
    }

    @Test
    public void sequenceOfStrings_okWithRemaining() {
        var parser = sequenceOf(str("foo"), str("bar"), str("baz"));
        var output = parser.run("foobarbazboo");

        assertThat(output.error).isNull();
        assertThat(output.result).isEqualTo(Tuple.of("foo", "bar", "baz"));
        assertThat(output.index).isEqualTo(9);
    }

    @Test
    public void sequenceOfStrings_fail() {
        var parser = sequenceOf(List.of(str("foo"), str("bar"), str("baz")));
        var output = parser.run("foobabaz");

        System.out.println("output = " + output);

        assertThat(output.error).isNotNull();
        assertThat(output.index).isEqualTo(3);
    }
}