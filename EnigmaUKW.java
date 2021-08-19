// TODO support for rewirable 'D' reflector

public class EnigmaUKW {
    private final static int KEYS = 26;
    private final int[] ukw;

    // construct UKW from spec
    public EnigmaUKW(String spec) {
        char c = spec.charAt(0);
        ukw = new int[KEYS];
        if (c == 'A') setupPreset("EJMZALYXVBWFCRQUONTSPIKHGD");
        else if (c == 'B') setupPreset("YRUHQSLDPXNGOKMIEBFZCWVJAT");
        else if (c == 'C') setupPreset("FVPJIAOYEDRZXWGCTKUQSBNMHL");
            // else if (c == 'D') { // user-defined wiring
            //    String pairs = spec.substring(1);
            //    validate(pairs);
            //    setup(pairs);
            // }
        else throw new IllegalArgumentException("invalid or unsupported UKW model");
    }

    // set up the UKW pairs array from model presets
    private void setupPreset(String wiring) {
        for (int i = 0; i < KEYS; i++)
            ukw[i] = wiring.charAt(i) - 'A';
    }

    /*
    // set up the UKW pairs array from user input
    private void setup(String pairs) {
        // setup UKW array
        int i = 0;
        int j = 1;
        while (j < KEYS - 2) {
            char c = pairs.charAt(i);
            c = Character.toUpperCase(c);
            char d = pairs.charAt(j);
            d = Character.toUpperCase(d);
            ukw[c - 'A'] = d - 'A';
            ukw[d - 'A'] = c - 'A';
            i = i + 2;
            j = j + 2;
        }
        // hardwired pair (J, Y)
        ukw['J' - 'A'] = 'Y' - 'A';
        ukw['Y' - 'A'] = 'J' - 'A';

        // verify that pairs are reciprocal
        for (int k = 0; k < KEYS; k++) {
            if (ukw[ukw[k]] != k)
                throw new IllegalArgumentException("UKW pairs are not reciprocal");
        }
    }

    // does the input specify a valid set of UKW pairs?
    private void validate(String pairs) {
        if (pairs.length() != KEYS - 2)
            throw new IllegalArgumentException("UKW should specify 12 pairs");

        for (int i = 0; i < KEYS - 2; i++) {
            char c = pairs.charAt(i);
            c = Character.toUpperCase(c);
            if (c < 'A' || c > 'Z')
                throw new IllegalArgumentException("UKW contains invalid characters");
            if (c == 'J' || c == 'Y')
                throw new IllegalArgumentException("(J, Y) is hardwired into UKW");
        }
    }
     */

    // gives the reflection of i = c - 'A'
    public char reflect(int i) {
        return (char) (ukw[i] + 'A');
    }
}
