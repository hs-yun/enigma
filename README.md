# Enigma

A Java program that simulates an Enigma I machine, taking in initial settings from the command-line, reading plaintext from standard input, and writing ciphertext to standard output (or vice versa).

## Compilation
```bash
javac Enigma.java
```
Dependencies: EnigmaRotor.java, EnigmaUKW.java

## Execution
An Enigma machine should be specified as follows:
  [left rotor] [middle rotor] [right rotor] [ukw] [steckerboard pairs]

For instance, 
```bash
$ java Enigma 1C18 3M04 2K22 B ATYKVP
```
specifies an Enigma machine with the following intialization state:
  + Walzenlage (wheel order)                 = I, III, II
  + Grundstellung (ground setting)           = C, M, K
  + Ringstellung (ring setting)              = 18, 04, 22
  + Umkehrwalze (reflector)                  = B
  + Steckerverbindungen (Steckerboard pairs) = (A, T), (Y, K), (V, P)

Some requirements:
  + rotors must be in [1, 5]
  + ground setting must be in [A, Z]
  + ring setting must be a two digit number in [01, 26]
  + up to 10 Steckerboard pairs are supported

Hello, World!
```bash
$ java Enigma 1C18 3M04 2K22 B ATYKVP
Hello, World!
XOUKKIVIEH
```

## License
[GNU GPLv3](https://www.gnu.org/licenses/gpl-3.0.en.html)
