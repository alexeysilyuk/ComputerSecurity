// Rotor class
public class Rotor extends Translator {
    private char turnoverAtLetter;
    private int offset;
    private int setting;
    // Constructor receives 2 arrays, first with permutations and second is turnover notch
    public Rotor(char args[][]) {
        setPermutationsArray(args[0]);      // set received permutations array
        reverseArray();                     // creates reveiced array
        setTurnoverAtLetter(args[1][0]);    // set turnover letter
    }


    public int getOffset() {
        return offset;
    }

    // setter for offset
    public void setOffset(char offset) {
        this.offset = letterToIndex(offset);
    }

    // setter for ground setting
    public void setSetting(char setting) {
        this.setting = letterToIndex(setting);
    }

    // turnover rotor offset forward
    public void turnOver() {
        this.offset = circularShift(this.offset + 1);
    }


    public void setTurnoverAtLetter(char turnoverAtLetter) {
            this.turnoverAtLetter = turnoverAtLetter;
    }

    // check if it now we must to make turnover
    public boolean isTurnoverNow() {
        return (letterToIndex(turnoverAtLetter) == offset);
    }

    // traslate forward ( right to left )
    @Override
    public char forwardTranslate(char letter) {
        int letterIndex = letterToIndex(letter);
        int permuteLocation = circularShift(letterIndex + offset - setting);
        char res = indexToLetter(circularShift(letterToIndex(PermutationsArray[permuteLocation]) - offset + setting));
        return res;
    }

    // traslate backward, from left to right
    @Override
    public char reverseTranslate(char letter) {
        int letterIndex = letterToIndex(letter);
        int permuteLocation = circularShift(letterIndex + offset - setting);
        char res = indexToLetter(circularShift(letterToIndex(reversePermutationsArray[permuteLocation]) - offset + setting));
        return res;
    }

}
