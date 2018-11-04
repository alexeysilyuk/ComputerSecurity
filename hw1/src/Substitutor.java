 public abstract class  Substitutor {

     public int letterToIndex(char letter){
         return letter-65;
     }

     public char indexToLetter(int index){
         return (char)(index+65);
     }

     public int circularShift(int index){
         index= (index<0)?index+26:index;

        return (index%26);
     }

     public abstract char reverseTranslate(char index);

     protected abstract void reverseArray();

     public abstract char forwardTranslate(char letter);

//     public abstract int translate(int index);
//
}
