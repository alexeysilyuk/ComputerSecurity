public class Enigma {
    // Available reflectors for this machine
    char reflectorB[] = "YRUHQSLDPXNGOKMIEBFZCWVJAT".toCharArray();
    // Available rotors for this machine
    char I[][] = {"EKMFLGDQVZNTOWYHXUSPAIBRCJ".toCharArray(), "Q".toCharArray()};
    char II[][] = {"AJDKSIRUXBLHWTMCQGZNPYFVOE".toCharArray(), "E".toCharArray()};
    char III[][] = {"BDFHJLCPRTXVZNYEIWGAKMUSQO".toCharArray(), "V".toCharArray()};
    char IV[][] = {"ESOVPZJAYQUIRHXLNFTGKDCMWB".toCharArray(), "J".toCharArray()};
    char V[][] = {"VZBRGITYUPSDNHLXAWMJQOFECK".toCharArray(), "Z".toCharArray()};

    private Reflector reflector;
    private Rotor left;
    private Rotor middle;
    private Rotor right;
    private char rotorsStatus[] = {'-', '-', '-'};

    public Enigma(String leftRotor, String middleRotor, String rightRotor,
                  char leftOffset, char middleOffset, char rightOffset,
                  char leftSetting, char middleSetting, char rightSetting) {

        reflector = new Reflector(reflectorB);
        setLeftRotor(leftRotor, leftSetting, leftOffset);
        setMiddleRotor(middleRotor, middleSetting, middleOffset);
        setRightRotor(rightRotor, rightSetting, rightOffset);

    }

    public void setLeftRotor(String leftRotor, char leftSetting, char leftOffset) {
        left = new Rotor(getRotorPermutations(leftRotor));
        left.setOffset(leftOffset);
        left.setSetting(leftSetting);

    }

    public void setMiddleRotor(String middleRotor, char middleSetting, char middleOffset) {
        middle = new Rotor(getRotorPermutations(middleRotor));
        middle.setOffset(middleOffset);
        middle.setSetting(middleSetting);
    }

    public void setRightRotor(String rightRotor, char rightSetting, char rightOffset) {
        right = new Rotor(getRotorPermutations(rightRotor));
        right.setOffset(rightOffset);
        right.setSetting(rightSetting);
    }

    public char[][] getRotorPermutations(String rotor) {
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

    public void printRotorsStatus() {
        rotorsStatus[0] = left.indexToLetter(left.getOffset());
        rotorsStatus[1] = middle.indexToLetter(middle.getOffset());
        rotorsStatus[2] = right.indexToLetter(right.getOffset());

        System.out.println(rotorsStatus[0] + " | " + rotorsStatus[1] + " | " + rotorsStatus[2]);
    }

    public void step(char letter) {

        if (right.isTurnoverNow() || middle.isTurnoverNow()) {
            if (middle.isTurnoverNow())
                left.turnOver();
            middle.turnOver();
        }
        right.turnOver();

        char one, two, three, ref;
        one = right.forwardTranslate(letter);
        two = middle.forwardTranslate(one);
        three = left.forwardTranslate(two);

        ref = reflector.forwardTranslate(three);

        three = left.reverseTranslate(ref);
        two = middle.reverseTranslate(three);
        one = right.reverseTranslate(two);
        System.out.print(one);

    }
}
