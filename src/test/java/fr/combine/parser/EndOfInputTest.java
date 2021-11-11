package fr.combine.parser;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class EndOfInputTest {

    @Test
    public void startOfEmptyTarget_ok() {
        State<Void> output = Parsers.endOfInput().run("");

        assertThat(output.error).isNull();
        assertThat(output.result).isEqualTo(null);
        assertThat(output.target).isEqualTo("");
        assertThat(output.index).isEqualTo(0);
    }

    @Test
    public void startOfNonEmptyTarget_parsingError() {
        State<Void> output = Parsers.endOfInput().run("hello");

        assertThat(output.error).isNotNull();
        assertThat(output.result).isEqualTo(null);
        assertThat(output.target).isEqualTo("hello");
        assertThat(output.index).isEqualTo(0);
    }

    @Test
    public void middleOfNonEmptyTarget_parsingError() {
        State<String> initialState = new State<>("hello!", "hello", 5, null);
        State<Void> output = Parsers.endOfInput().run(initialState);

        assertThat(output.error).isNotNull();
        assertThat(output.result).isEqualTo(null);
        assertThat(output.target).isEqualTo("hello!");
        assertThat(output.index).isEqualTo(5);
    }

    @Test
    public void endOfNonEmptyTarget_ok() {
        State<String> initialState = new State<>("hello", "hello", 5, null);
        State<Void> output = Parsers.endOfInput().run(initialState);

        assertThat(output.error).isNull();
        assertThat(output.result).isEqualTo(null);
        assertThat(output.target).isEqualTo("hello");
        assertThat(output.index).isEqualTo(5);
    }

}