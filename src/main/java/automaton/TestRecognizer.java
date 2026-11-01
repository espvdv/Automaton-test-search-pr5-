package automaton;

public class TestRecognizer {

    public enum State {
        S, ONE, TWO, THREE, F
    }

    private State currentState;

    public TestRecognizer() {
        this.currentState = State.S;
    }

    public void processChar(char c) {
        switch (currentState) {
            case S:
                if (c == 'T') currentState = State.ONE;
                break;

            case ONE:
                if (c == 'E')      currentState = State.TWO;
                else if (c == 'T') currentState = State.ONE;
                else               currentState = State.S;
                break;

            case TWO:
                if (c == 'S')      currentState = State.THREE;
                else if (c == 'T') currentState = State.ONE;
                else               currentState = State.S;
                break;

            case THREE:
                if (c == 'T') currentState = State.F;
                else          currentState = State.S;
                break;

            case F:
                if (c == 'T') currentState = State.ONE;
                else          currentState = State.F;
                break;
        }
    }

    public State processString(String input) {
        this.currentState = State.S;
        if (input == null) return this.currentState;
        for (char c : input.toCharArray()) {
            processChar(c);
        }
        return this.currentState;
    }

    public State getCurrentState() {
        return currentState;
    }
}