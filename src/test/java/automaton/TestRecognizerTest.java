package automaton;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestRecognizerTest {

    private TestRecognizer.State getResult(String input) {
        return new TestRecognizer().processString(input);
    }

    static Stream<Arguments> provideBasicCases() {
        return Stream.of(
            Arguments.of("abcTESTabc", TestRecognizer.State.F),
            Arguments.of("abcTES",     TestRecognizer.State.THREE),
            Arguments.of("TEST",       TestRecognizer.State.F),
            Arguments.of("T",          TestRecognizer.State.ONE),
            Arguments.of("TE",         TestRecognizer.State.TWO),
            Arguments.of("TES",        TestRecognizer.State.THREE),
            Arguments.of("ABC",        TestRecognizer.State.S),
            Arguments.of("",           TestRecognizer.State.S),
            Arguments.of("TTEST",      TestRecognizer.State.F),
            Arguments.of("TETEST",     TestRecognizer.State.F),
            Arguments.of("TESTA",      TestRecognizer.State.F)
        );
    }

    @ParameterizedTest(name = "[{index}] \"{0}\" -> {1}")
    @MethodSource("provideBasicCases")
    void testBasicTransitions(String input, TestRecognizer.State expected) {
        assertEquals(expected, getResult(input));
    }

    static Stream<Arguments> provideFailureCases() {
        return Stream.of(
            Arguments.of("X",         TestRecognizer.State.S),
            Arguments.of("TX",        TestRecognizer.State.S),
            Arguments.of("TEX",       TestRecognizer.State.S),
            Arguments.of("TESX",      TestRecognizer.State.S),
            Arguments.of("TESQ",      TestRecognizer.State.S),
            Arguments.of("TES TEST",  TestRecognizer.State.F)
        );
    }

    @ParameterizedTest(name = "[fail] \"{0}\" -> {1}")
    @MethodSource("provideFailureCases")
    void testFailureResets(String input, TestRecognizer.State expected) {
        assertEquals(expected, getResult(input));
    }

    @Test
    void testOverlappingMatches() {
        assertEquals(TestRecognizer.State.F, getResult("TESTTEST"));
        assertEquals(TestRecognizer.State.ONE, getResult("TESTEST"));
        assertEquals(TestRecognizer.State.F, getResult("TESTTESTTEST"));
    }

    @Test
    void testPartialOverlap() {
        assertEquals(TestRecognizer.State.F, getResult("TTEST"));
        assertEquals(TestRecognizer.State.ONE, getResult("TESTT"));
        assertEquals(TestRecognizer.State.TWO, getResult("TESTTE"));
        assertEquals(TestRecognizer.State.THREE, getResult("TESTTES"));
    }

    @Test
    void testLongString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10_000; i++) sb.append('a');
        sb.append("TEST");
        assertEquals(TestRecognizer.State.F, getResult(sb.toString()));
    }

    @Test
    void testLongStringWithoutMatch() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10_000; i++) sb.append('x');
        assertEquals(TestRecognizer.State.S, getResult(sb.toString()));
    }

    @Test
    void testNullInput() {
        assertEquals(TestRecognizer.State.S, getResult(null));
    }
}