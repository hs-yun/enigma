/*
 * ****************************************************************************
 *  Compilation:  javac Enigma.java
 *  Execution:    java Enigma [Enigma specification]
 *  Dependencies: EnigmaRotor.java, EnigmaUKW.java
 *
 *  Simulates an Enigma I machine, taking in initial settings from the command
 *  -line, reading plaintext from standard input, and writing ciphertext to
 *  standard output.
 ******************************************************************************
 */

import java.util.Scanner;

/**
 * An Enigma machine should be specified as follows:
 * [left rotor] [middle rotor] [right rotor] [ukw] [steckerboard pairs]
 * <p>
 * For instance, the string
 * "1C18 3M04 2K22 B ATYKVP"
 * specifies an Enigma machine with the following intialization state:
 * + Walzenlage (wheel order)                 = I, III, II
 * + Grundstellung (ground setting)           = C, M, K
 * + Ringstellung (ring setting)              = 18, 04, 22
 * + Umkehrwalze (reflector)                  = B
 * + Steckerverbindungen (Steckerboard pairs) = (A, T), (Y, K), (V, P)
 * <p>
 * Some requirements:
 * + rotors must be in [1, 5]
 * + ground setting must be in [A, Z]
 * + ring setting must be a two digit number in [01, 26]
 * + up to 10 Steckerboard pairs are supported
 */

// TODO random constructor with user-defined number of rotors

public class Enigma {
    private final static int KEYS = 26;
    private final EnigmaRotor rotorL;
    private final EnigmaRotor rotorM;
    private final EnigmaRotor rotorR;
    private final EnigmaUKW ukw;
    private char[] stecker;

    // construct an Enigma with Steckerboard pairs
    public Enigma(String rotorLSpec, String rotorMSpec, String rotorRSpec,
                  String ukwSpec, String steckerSpec) {
        rotorL = new EnigmaRotor(rotorLSpec);
        rotorM = new EnigmaRotor(rotorMSpec);
        rotorR = new EnigmaRotor(rotorRSpec);
        ukw = new EnigmaUKW(ukwSpec);
        validateStecker(steckerSpec);
        setupStecker(steckerSpec);
    }

    // construct an Enigma without Steckerboard pairs
    public Enigma(String rotorLSpec, String rotorMSpec, String rotorRSpec,
                  String ukwSpec) {
        rotorL = new EnigmaRotor(rotorLSpec);
        rotorM = new EnigmaRotor(rotorMSpec);
        rotorR = new EnigmaRotor(rotorRSpec);
        ukw = new EnigmaUKW(ukwSpec);
    }

    // set up the Steckerboard pairs array
    private void setupStecker(String pairs) {
        stecker = new char[KEYS];
        // initialize Steckerboard with i: x -> x
        for (int i = 0; i < KEYS; i++) stecker[i] = (char) (i + 'A');

        // read in pairs
        int i = 0;
        int j = 1;
        while (j < pairs.length()) {
            char c = pairs.charAt(i);
            c = Character.toUpperCase(c);
            char d = pairs.charAt(j);
            d = Character.toUpperCase(d);
            stecker[c - 'A'] = d;
            stecker[d - 'A'] = c;
            i = i + 2;
            j = j + 2;
        }

        // verify that pairs are reciprocal
        for (int k = 0; k < KEYS; k++) {
            if (stecker[stecker[k] - 'A'] - 'A' != k)
                throw new IllegalArgumentException("Steckerboard pairs are not reciprocal");
        }
    }

    // does the input specify a valid set of Steckerboard pairs?
    private void validateStecker(String pairs) {
        if (pairs.length() > 20)
            throw new IllegalArgumentException("no more than 10 Stecker pairs are possible");

        for (int i = 0; i < pairs.length(); i++) {
            char c = pairs.charAt(i);
            c = Character.toUpperCase(c);
            if (c < 'A' || c > 'Z')
                throw new IllegalArgumentException("Steckerboard contains invalid characters");
        }
    }

    // gives Enigma's output on input c
    private char step(char c) {
        // step rotors
        if (rotorM.notch(0)) {
            rotorL.rotate(1);
            rotorM.rotate(1);
            rotorR.rotate(1);
        } else if (rotorR.notch(0)) {
            rotorM.rotate(1);
            rotorR.rotate(1);
        } else rotorR.rotate(1);

        if (stecker != null)
            c = stecker[c - 'A']; // Steckerboard substitution

        int in = rotorL.pathIn(rotorM.pathIn(rotorR.pathIn(c)));
        char d = ukw.reflect(in);
        int out = rotorR.pathOut(rotorM.pathOut(rotorL.pathOut(d)));

        if (stecker != null)
            return stecker[out];
        return (char) (out + 'A');
    }

    // take in plaintext, print out ciphertext to standard out
    public void encode(String input) {
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            c = Character.toUpperCase(c);
            if (c >= 'A' && c <= 'Z') {
                System.out.print(step(c));
            }
        }
    }

    public static void main(String[] args) {
        Enigma enigma;
        if (args.length == 5)
            enigma = new Enigma(args[0], args[1], args[2], args[3], args[4]);
        else
            enigma = new Enigma(args[0], args[1], args[2], args[3]);
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            enigma.encode(input);
        }
    }
}
