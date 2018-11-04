enum TYPE {REFLECTOR,ROTOR};

public class Translator extends  Substitutor {

    protected char[] PermutationsArray;
    protected char[] reversePermutationsArray;
    protected TYPE type;

    public String getTypeAsString() {
        return (type==TYPE.REFLECTOR)?"REFLECTOR":"ROTOR";
    }

    public boolean setType(TYPE type) {
        if(type.equals(TYPE.REFLECTOR) || type.equals(TYPE.ROTOR))
        {
            this.type = type;
            return true;
        }
        else
            return false;

    }


    public boolean setPermutationsArray(char[] permutationsArray) {
        if(permutationsArray!=null && permutationsArray.length==26)
        {
            PermutationsArray = permutationsArray;
            return true;
        }
        else
        {
            System.out.println("Permutation array must contain 26 letters");
            return false;
        }
    }


    @Override
    public char forwardTranslate(char letter){
        return 0;
    }

    @Override
    public char reverseTranslate(char index) {
        return 0;
    }

    @Override
    protected void reverseArray() {
        reversePermutationsArray = new char[26];
        for (int i=0;i<26;i++)
            reversePermutationsArray[letterToIndex(PermutationsArray[i])]= indexToLetter(i);

    }

//    @Override
//    public char translate(int index) {
//        return letterToIndex(PermutationsArray[index]);
//    }
}
