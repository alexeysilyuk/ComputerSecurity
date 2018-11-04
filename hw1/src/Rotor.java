public class Rotor extends Translator {
    private char turnoverAtLetter;
    private int offset;
    private int setting;

    // Constructor receives 2 arrays, first with permutations and second is turnover notch
    public Rotor(char args[][]) {
        this.setPermutationsArray(args[0]);
        this.reverseArray();
        this.setType(TYPE.ROTOR);
        if(!setTurnoverAtLetter(args[1][0]))
            turnoverAtLetter='Z';   // default value if sended one is incorect
    }



    public void setOffset(char offset) {
        this.offset = offset;
    }

    public void setSetting(char setting) {
        this.setting = setting;
    }


    public void turnOver() {
        this.offset = circularShift( this.offset+1);
    }



    public boolean setTurnoverAtLetter(char turnoverAtLetter) {

        if (turnoverAtLetter>='A' && turnoverAtLetter <='Z')
        {
            this.turnoverAtLetter = turnoverAtLetter;
            return true;
        }
        else
            return false;
    }

    public boolean isTurnoverNow(){
        return (letterToIndex(turnoverAtLetter)==offset);

    }

    @Override
    public char forwardTranslate(char letter) {
        int letterIndex = letterToIndex(letter);
        int permuteLocation = circularShift(letterIndex + offset - setting);
        char res =  indexToLetter(circularShift(letterToIndex(PermutationsArray[permuteLocation]) - offset + setting));
        return res;
    }

    @Override
    public char reverseTranslate(char letter) {
        int letterIndex = letterToIndex(letter);
        int permuteLocation = circularShift(letterIndex + offset - setting);
        char res =  indexToLetter(circularShift(letterToIndex(reversePermutationsArray[permuteLocation]) - offset + setting));
        return res;
    }

}
