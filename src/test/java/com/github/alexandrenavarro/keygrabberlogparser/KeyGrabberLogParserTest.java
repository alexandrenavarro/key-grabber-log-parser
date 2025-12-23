package com.github.alexandrenavarro.keygrabberlogparser;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class KeyGrabberLogParserTest {

    @Test
    void shouldParseLineCorrectly() {
        assertThat(KeyGrabberLogParser.parseKeyWithModifierPressed("")).isEmpty();
        assertThat(KeyGrabberLogParser.parseKeyWithModifierPressed("a")).contains(KeyPressed.of("a"));
        assertThat(KeyGrabberLogParser.parseKeyWithModifierPressed("ab")).contains(KeyPressed.of("a"), KeyPressed.of("b"));
        assertThat(KeyGrabberLogParser.parseKeyWithModifierPressed("[Ent]")).contains(KeyPressed.of("[Ent]"));
        assertThat(KeyGrabberLogParser.parseKeyWithModifierPressed("ab[Ent]")).contains(KeyPressed.of("a"), KeyPressed.of("b"), KeyPressed.of("[Ent]"));
        assertThat(KeyGrabberLogParser.parseKeyWithModifierPressed("[Rgh]ab[Ent]")).contains(KeyPressed.of("[Rgh]"), KeyPressed.of("a"), KeyPressed.of("b"), KeyPressed.of("[Ent]"));
        assertThat(KeyGrabberLogParser.parseKeyWithModifierPressed("[Sh]Ab[Ent]")).contains(KeyPressed.ofShift("A"), KeyPressed.of("b"), KeyPressed.of("[Ent]"));
        assertThat(KeyGrabberLogParser.parseKeyWithModifierPressed("[Alt] [Ent]")).contains(KeyPressed.ofAlt(" "), KeyPressed.of("[Ent]"));
        assertThat(KeyGrabberLogParser.parseKeyWithModifierPressed("[Ctl]v[Ent]")).contains(KeyPressed.ofCtrl("v"), KeyPressed.of("[Ent]"));
        assertThat(KeyGrabberLogParser.parseKeyWithModifierPressed("[Win][Lft][Ent]")).contains(KeyPressed.ofSuper("[Lft]"), KeyPressed.of("[Ent]"));
    }

    @Test
    void shouldParseFileCorrectly() {
        KeyGrabberLogParser.main("./src/test/resources/LOG.TXT");
    }

}