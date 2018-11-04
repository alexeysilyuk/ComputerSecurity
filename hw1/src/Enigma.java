import java.util.Map;

public class Enigma {
        // Available reflectors for this machine
        char reflectorB[]       = "YRUHQSLDPXNGOKMIEBFZCWVJAT".toCharArray();
        // Available rotors for this machine
        char I[][]         = {"EKMFLGDQVZNTOWYHXUSPAIBRCJ".toCharArray(),"Q".toCharArray()};
        char II[][]        = {"AJDKSIRUXBLHWTMCQGZNPYFVOE".toCharArray(),"E".toCharArray()};
        char III[][]       = {"BDFHJLCPRTXVZNYEIWGAKMUSQO".toCharArray(),"V".toCharArray()};
        char IV[][]        = {"ESOVPZJAYQUIRHXLNFTGKDCMWB".toCharArray(),"J".toCharArray()};
        char V[][]         = {"VZBRGITYUPSDNHLXAWMJQOFECK".toCharArray(),"Z".toCharArray()};

        private Reflector reflector;
        private Rotor left;
        private Rotor middle;
        private Rotor right;
        private char rotorsStatus[]={'-','-','-'};

        public Enigma(String leftRotor,char middleRotor,char rightRotor,
                      String leftOffset,char middleOffset,char rightOffset,
                      String leftSetting,char middleSetting,char rightSetting) {

                reflector = new Reflector(reflectorB);
                setLeftRotor(leftRotor,leftSetting,leftOffset);
                setMiddleRotor(middleRotor,middleSetting,middleOffset);
                setRightRotor(rightRotor,rightSetting,rightOffset);
                printRotorsStatus();

        }

        public void setLeftRotor(String leftRotor,char leftSetting,char leftOffset){
            left=new Rotor(getRotorPermutations(leftRotor));
            left.setOffset(leftOffset);
            left.setSetting(leftSetting);

        }
        public void setMiddleRotor(String middleRotor,char middleSetting, char middleOffset){
            middle=new Rotor(getRotorPermutations(middleRotor));
            middle.setOffset(middleOffset);
            middle.setSetting(middleSetting);
        }
        public void setRightRotor(String rightRotor,char rightSetting,char rightOffset){
                right=new Rotor(getRotorPermutations(rightRotor));
                left.setOffset(leftOffset);
                left.setSetting(leftSetting);
                //right.tostring();
        }
        public char[][] getRotorPermutations(String rotor){
                char[][] error = {{'E'},{'E'}};
                switch(rotor){
                        case "I":return I;
                        case "II":return II;
                        case "III":return III;
                        case "IV":return IV;
                        case "V":return V;
                }
                return error;
        }

        public void printRotorsStatus(){
                System.out.println(rotorsStatus[0]+" | "+rotorsStatus[1]+" | "+rotorsStatus[2]);
        }

        public void step(char letter){

                if (right.isTurnoverNow() || middle.isTurnoverNow()){
                        if (middle.isTurnoverNow())
                                left.turnOver();
                        middle.turnOver();
                }
                right.turnOver();

                char one,two,three,ref,result;
                one = right.forwardTranslate(letter);
                two = middle.forwardTranslate(one);
                three = left.forwardTranslate(two);
                ref=reflector.forwardTranslate(three);

                three = left.reverseTranslate(ref);
                two = middle.reverseTranslate(three);
                one = right.reverseTranslate(two);
            System.out.print(one);
//            c = middle.translation(c);
//            c = left.translation(c);
//            c = reflector.translation(c);

//            right.setDir(1);
//            middle.setDir(1);
//            left.setDir(1);
//
//            c = left.translation(c);
//            c = middle.translation(c);
//            c = right.translation(c);
//            System.out.println(c);
//                one = right.forwardTranslate(right.letterToIndex(letter));
//                System.out.println(one);
//                two = middle.forwardTranslate(middle.letterToIndex(one)-1);
//                System.out.println(two);
//                three = left.forwardTranslate(left.letterToIndex(two));
//                System.out.println(three);
//                ref = reflector.forwardTranslate(reflector.letterToIndex(three));
//                System.out.println(ref);
//
//                three = left.reverseTranslate(left.letterToIndex(ref));
//                System.out.println(three);
//                two = middle.reverseTranslate(middle.letterToIndex(three));
//                System.out.println(two);
//                one = right.reverseTranslate(right.letterToIndex(two)+1);
//                System.out.println(one);
//                result= right.indexToLetter(right.letterToIndex(one)-1);
//                System.out.println(result);

//                right.leftNeighbor.forwardTranslate();
//                right.leftNeighbor.leftNeighbor.forwardTranslate();

                //printRotorsStatus();
        }
}
