package fr.combine.parser;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class ParsersTest_str {

    @Test
    public void emptyStringArgument_exception() {
        assertThatThrownBy(() -> Parsers.str(""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void startOfEmptyTarget_parsingError() {
        State<String> output = Parsers.str("a").run("");

        assertThat(output.error).isNotNull();
        assertThat(output.result).isEqualTo(null);
        assertThat(output.target).isEqualTo("");
        assertThat(output.index).isEqualTo(0);
    }

    @Test
    public void startOfNonEmptyTarget_ok() {
        State<String> output = Parsers.str("hell").run("hello");

        assertThat(output.error).isNull();
        assertThat(output.result).isEqualTo("hell");
        assertThat(output.target).isEqualTo("hello");
        assertThat(output.index).isEqualTo(4);
    }

    @Test
    public void endOfNonEmptyTarget_ok() {
        State<String> initialState = new State<>("hello", "hell", 4, null);
        State<String> output = Parsers.str("o").run(initialState);

        assertThat(output.error).isNull();
        assertThat(output.result).isEqualTo("o");
        assertThat(output.target).isEqualTo("hello");
        assertThat(output.index).isEqualTo(5);
    }

}