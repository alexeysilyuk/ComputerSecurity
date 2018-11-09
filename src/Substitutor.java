
// class for substituting and permutation
public abstract class Substitutor {

    // translate letter to index
    public int letterToIndex(char letter) {
        return letter - 65;
    }

    // translate index to letter
    public char indexToLetter(int index) {
        return (char)(index + 65);
    }

    // modulu function
    public int circularShift(int index) {
        index = (index < 0) ? index + 26 : index; // index=index + 26 in case of negative result

        return (index % 26);                      // return modulu result
    }


    public abstract char forwardTranslate(char letter);
    public abstract  char reverseTranslate(char letter);

}
