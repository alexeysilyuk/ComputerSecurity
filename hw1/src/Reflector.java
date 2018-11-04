public class Reflector extends Translator {

    public Reflector(char[] PermutationsArray) {
        super.setPermutationsArray(PermutationsArray);
        super.setType(TYPE.REFLECTOR);
        reverseArray();

    }

    @Override
    public char forwardTranslate(char letter) {
        int letterIndex = letterToIndex(letter);
        int permuteLocation = circularShift(letterIndex);
        char res = indexToLetter(circularShift(letterToIndex(PermutationsArray[permuteLocation])));
        return res;
    }

    @Override
    public char reverseTranslate(char letter) {
        int letterIndex = letterToIndex(letter);
        int permuteLocation = circularShift(letterIndex);
        char res = indexToLetter(circularShift(letterToIndex(PermutationsArray[permuteLocation])));
        return res;
    }


}
