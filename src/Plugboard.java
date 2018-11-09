
// Plugboard class
public class Plugboard extends Translator{

    // initialize with default settings A->A, B->B etc, by default all letters are themselves
    public Plugboard() {
        PermutationsArray= new char[arraySize];
        for (int i=0;i<arraySize;i++)
            PermutationsArray[i]=indexToLetter(i);
    }

    // add new plug tupple
    public void setPlug(char from,char to){
        PermutationsArray[letterToIndex(from)]=to;  // swap 2 received letters
        PermutationsArray[letterToIndex(to)]=from;
    }


    public void createReversePlugsArray(){
        reverseArray();
    }

    // translate forward
    public char forwardTranslate(char letter){
        return PermutationsArray[letterToIndex(letter)];
    }

    // translate backward
    public char reverseTranslate(char letter){
        return  reversePermutationsArray[letterToIndex(letter)];
    }

}
