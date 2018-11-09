// Enigma Machine class

public class Enigma extends Translator{
    // Available reflectors for this machine, may add others too
    char reflectorB[] = "YRUHQSLDPXNGOKMIEBFZCWVJAT".toCharArray();
    // Available rotors for this machine
    char I[][] = {"EKMFLGDQVZNTOWYHXUSPAIBRCJ".toCharArray(), "Q".toCharArray()};
    char II[][] = {"AJDKSIRUXBLHWTMCQGZNPYFVOE".toCharArray(), "E".toCharArray()};
    char III[][] = {"BDFHJLCPRTXVZNYEIWGAKMUSQO".toCharArray(), "V".toCharArray()};
    char IV[][] = {"ESOVPZJAYQUIRHXLNFTGKDCMWB".toCharArray(), "J".toCharArray()};
    char V[][] = {"VZBRGITYUPSDNHLXAWMJQOFECK".toCharArray(), "Z".toCharArray()};

    // parts of Machine
    private Reflector reflector;
    private Rotor left;
    private Rotor middle;
    private Rotor right;
    private Plugboard plugboard;
    private char rotorsStatus[] = {'-', '-', '-'};  // current enigma machine indicator


    // Constructor receives rothors objects and offsets
    public Enigma(String leftRotor, String middleRotor, String rightRotor,
                  char leftOffset, char middleOffset, char rightOffset,
                  char leftSetting, char middleSetting, char rightSetting, Plugboard board) {

        reflector = new Reflector(reflectorB);                  // creates reflector
        setLeftRotor(leftRotor, leftSetting, leftOffset);       // setts Rotors
        setMiddleRotor(middleRotor, middleSetting, middleOffset);
        setRightRotor(rightRotor, rightSetting, rightOffset);
        setPlugboard(board);
    }

    // setter for plugboard
    public  void setPlugboard(Plugboard board){ plugboard=board; }

    private void setLeftRotor(String leftRotor, char leftSetting, char leftOffset) {
        left = new Rotor(getRotorPermutations(leftRotor));
        left.setOffset(leftOffset);
        left.setSetting(leftSetting);

    }

    private void setMiddleRotor(String middleRotor, char middleSetting, char middleOffset) {
        middle = new Rotor(getRotorPermutations(middleRotor));
        middle.setOffset(middleOffset);
        middle.setSetting(middleSetting);
    }

    private void setRightRotor(String rightRotor, char rightSetting, char rightOffset) {
        right = new Rotor(getRotorPermutations(rightRotor));
        right.setOffset(rightOffset);
        right.setSetting(rightSetting);
    }

    private char[][] getRotorPermutations(String rotor) {
        char[][] error = {{'E'}, {'E'}};
        switch (rotor) {
            case "I":
                return I;
            case "II":
                return II;
            case "III":
                return III;
            case "IV":
                return IV;
            case "V":
                return V;
        }
        return error;
    }

    // gett all rotors offsets and print
    public void printRotorsStatus() {
        rotorsStatus[0] = left.indexToLetter(left.getOffset());
        rotorsStatus[1] = middle.indexToLetter(middle.getOffset());
        rotorsStatus[2] = right.indexToLetter(right.getOffset());

        System.out.println(rotorsStatus[0] + " | " + rotorsStatus[1] + " | " + rotorsStatus[2]);
    }

    // machine step for each letter
    public char step(char letter) {

        char zero, one, two, three, ref;

        //  make turnover's if neccessary
        if (right.isTurnoverNow() || middle.isTurnoverNow())
        {
            if (middle.isTurnoverNow())
                left.turnOver();

            middle.turnOver();
        }
        right.turnOver();

        // P = plugboard, R,L,M = are rotors right, left and middle, R - reflector
        // translattions steps : P > R > M > L > R > L > M > R > P
        zero = plugboard.forwardTranslate(letter);
        one = right.forwardTranslate(zero);
        two = middle.forwardTranslate(one);
        three = left.forwardTranslate(two);

        ref = reflector.translate(three);

        three = left.reverseTranslate(ref);
        two = middle.reverseTranslate(three);
        one = right.reverseTranslate(two);
        zero = plugboard.reverseTranslate(one);

        return zero;

    }
}
