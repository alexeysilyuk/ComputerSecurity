
// tranlates received index to value stored in permutations arrays
public class Translator extends Substitutor {
    final static int arraySize=26;

    protected char[] PermutationsArray;
    protected char[] reversePermutationsArray;

    // setter for permutations array
    public void setPermutationsArray(char[] permutationsArray) {
            PermutationsArray = permutationsArray;
    }

    // function that reverses received permutations array for future reverse translation
    protected void reverseArray() {

        reversePermutationsArray = new char[arraySize]; // creates reverse array

        // create reverse permutations
        for (int i = 0; i < arraySize; i++)
            reversePermutationsArray[letterToIndex(PermutationsArray[i])] = indexToLetter(i);
    }

    @Override
    public char forwardTranslate(char letter) {
        return 0;
    }

    @Override
    public char reverseTranslate(char letter) {
        return 0;
    }
}
