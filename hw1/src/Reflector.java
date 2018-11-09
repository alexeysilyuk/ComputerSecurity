// Reflector class
public class Reflector extends Translator {

    // constructor
    public Reflector(char[] PermutationsArray) {
        setPermutationsArray(PermutationsArray);
        reverseArray(); // call reverse permutations array
    }

    // receives letter, calculation it's translation inside
    public char translate(char letter) {
        int letterIndex = letterToIndex(letter);                // translate received letter to index
        return indexToLetter(circularShift(letterToIndex(PermutationsArray[letterIndex]))); // translate letter to it's permutation
    }

}
