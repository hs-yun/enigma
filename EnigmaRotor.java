public class EnigmaRotor {
    private final static int KEYS = 26;
    private int[] offsetsIn; // input position + offsetsIn[i] = exit position R->L
    private int[] offsetsOut; // input position + offsetsOut[i] = exit position L->R
    private int rotation; // number of steps from 'A'
    private final int turnover; // char on which notch turns

    // construct an Enigma rotor from a spec String
    public EnigmaRotor(String spec) {
        if (spec.length() != 4)
            throw new IllegalArgumentException("spec must be given as a 4 character string");
        offsetsIn = new int[KEYS];
        offsetsOut = new int[KEYS];

        // which rotor wiring does the spec call for?
        char c = spec.charAt(0);
        if (c == '1') {
            setup("EKMFLGDQVZNTOWYHXUSPAIBRCJ");
            turnover = 'Q' - 'A';
        } else if (c == '2') {
            setup("AJDKSIRUXBLHWTMCQGZNPYFVOE");
            turnover = 'E' - 'A';
        } else if (c == '3') {
            setup("BDFHJLCPRTXVZNYEIWGAKMUSQO");
            turnover = 'V' - 'A';
        } else if (c == '4') {
            setup("ESOVPZJAYQUIRHXLNFTGKDCMWB");
            turnover = 'J' - 'A';
        } else if (c == '5') {
            setup("VZBRGITYUPSDNHLXAWMJQOFECK");
            turnover = 'Z' - 'A';
        } else throw new IllegalArgumentException("rotor is invalid or unsupported");

        // set Grundstellung
        rotation = 0;
        char gs = spec.charAt(1);
        gs = Character.toUpperCase(gs);
        if (gs < 'A' || gs > 'Z')
            throw new IllegalArgumentException("ground setting must be between A and Z");
        setGround(gs);

        // set Ringstellung
        int rs = Integer.parseInt(spec.substring(2, 4));
        if (rs < 1 || rs > 26)
            throw new IllegalArgumentException("ring setting must be between 1 and 26");
        setRing(rs);
    }

    // wire internals according to rotor presets
    private void setup(String wiring) {
        for (int i = 0; i < KEYS; i++) { // calculate offsets
            char c = wiring.charAt(i);
            offsetsIn[i] = c - i - 'A';
            while (offsetsIn[i] < 0) offsetsIn[i] = offsetsIn[i] + KEYS;
            offsetsOut[(i + offsetsIn[i]) % KEYS] = KEYS - offsetsIn[i];
        }
    }

    // set the Grundstellung for the rotor
    private void setGround(char c) {
        int offset = c - 'A';
        rotate(offset);
    }

    // set the Ringstellung for the rotor
    private void setRing(int i) {
        i = --i;
        int[] auxIn = new int[KEYS];
        int[] auxOut = new int[KEYS];
        int j = 0;
        while (j < KEYS) { // shift contacts outputs over by i
            auxIn[i] = offsetsIn[j];
            auxOut[i] = offsetsOut[j];
            i++;
            j++;
            if (i == KEYS) i = 0;
        }
        offsetsIn = auxIn;
        offsetsOut = auxOut;
    }

    /*
    private void validate(String wiring) {
        if (wiring.length() != KEYS)
            throw new IllegalArgumentException("input does not assign every letter");
        for (int i = 0; i < KEYS; i++) {
            char c = wiring.charAt(i);
            c = Character.toUpperCase(c);
            if (c < 'A' || c > 'Z')
                throw new IllegalArgumentException("input contains invalid characters");
        }
    }
    */

    // going L->R, what is the exit position of input on the rightmost rotor?
    public int pathIn(int input) {
        int offset = offsetsIn[(input + rotation) % KEYS]; // compensate for rotation
        return (input + offset) % KEYS;
    }

    public int pathIn(char c) {
        return pathIn(c - 'A');
    }

    // going R->L, what is the exit position of input on the leftmost rotor?
    public int pathOut(int input) {
        int offset = offsetsOut[(input + rotation) % KEYS]; // compensate for rotation
        return (input + offset) % KEYS;
    }

    public int pathOut(char c) {
        return pathOut(c - 'A');
    }

    // is rotation + i the turnover notch?
    public boolean notch(int i) {
        return (rotation + i) == turnover;
    }

    // rotate the rotor one position
    public void rotate(int i) {
        rotation = (rotation + i) % KEYS;
    }
}
