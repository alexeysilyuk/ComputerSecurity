import java.util.HashSet;
import java.util.Scanner;

// Driver class
public class Driver {

    private static Enigma machine;
    private static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running=true;
        String again;
        while(running) {
            initMachine();
            System.out.print("Rotors Offset: ");
            machine.printRotorsStatus();    // print initial rotors offsets
            System.out.println("Please input message to encrypt, all letters will be transfered to uppercase and spaces will be  removed\n-------------------");
            decrypt();
            System.out.print("Rotors Offset: ");
            machine.printRotorsStatus();    // print offsets after message decrypting

            System.out.println("again? ( y / n )");
            switch(input.nextLine().toLowerCase())
            {
                case "y":
                    System.out.println("OK! Let's play again :)");
                    break;
                case "n":
                    System.out.println("OK, come back to encrypt again...");
                    running=false;
                    break;
                default:
                    System.out.println("Jibrish input,i can't understand\nI suppose you don't want to play again, Bye! ");
                    running=false;
            }
        }
    }

    public static void initMachine() {
        getRotorsFromUser();
    }

    private static Plugboard createPlugBoard(){

        Plugboard plugboard = new Plugboard();

        System.out.println("Need plug board? (y / n) : ");
        String choice = input.nextLine().toLowerCase();
        switch(choice){
            case "y":
                getPlugs(plugboard);
                System.out.println("Setting plugboard...");
                break;
            case "n":
                System.out.println("OK! Setting default plugboard...");
                break;
            default:
                System.out.println("What what? I don't know what do you want :(\nsetting default plugboard...");
        }
        plugboard.createReversePlugsArray();

    return  plugboard;
    }

    public static void getPlugs(Plugboard board){
        String pair;

        System.out.println("How much plugins you want to connect?");
        int amount = input.nextInt();
        input.nextLine();   // clear \n from input
        System.out.println("Please input plugin pair i.ex  \'ZU\' or \'zu\' \n( only 2 first letters will be taken from input line )");
        if(amount>0 && amount<=10)
        {
            for(int i=0;i<amount;i++){
                System.out.print("plug #"+(i+1)+":");
                pair = input.nextLine().toUpperCase();

                if(checkInput(pair))
                    board.setPlug(pair.charAt(0),pair.charAt(1));
                else
                    System.out.println("skipping pair...");

            }
        }
    }

    // chack plugboard pair input
    private static boolean checkInput(String pair){
        return (isLetter(pair.charAt(0)) && isLetter(pair.charAt(1)));
    }


    public static void getRotorsFromUser() {

        String leftRotor, middleRotor, rightRotor;
        String leftSetting,middleSetting,rightSetting,leftOffset,middleOffset,rightOffset;
        do{
            System.out.println("Options for rotor chosing : I, II, III, IV, V");
            System.out.print("Please choose Left Rotor: ");
            leftRotor = input.nextLine().toUpperCase();
            System.out.print("Please choose Middle Rotor: ");
            middleRotor = input.nextLine().toUpperCase();
            System.out.print("Please choose Right Rotor: ");
            rightRotor = input.nextLine().toUpperCase();
        } while(checkRotorsValidity(leftRotor,middleRotor,rightRotor) == false);

        do{
            System.out.print("Left rotor Setting: ");
            leftSetting=input.nextLine().toUpperCase();
            System.out.print("Middle rotor Setting: ");
            middleSetting=input.nextLine().toUpperCase();
            System.out.print("Right rotor Setting: ");
            rightSetting=input.nextLine().toUpperCase();
        } while(checkSettings(leftSetting,middleSetting,rightSetting) == false);

        do {
            System.out.print("Left rotor Offset: ");
            leftOffset = input.nextLine().toUpperCase();
            System.out.print("Middle rotor Offset: ");
            middleOffset = input.nextLine().toUpperCase();
            System.out.print("Right rotor Offset: ");
            rightOffset = input.nextLine().toUpperCase();
        }while(checkOffsets(leftOffset,middleOffset,rightOffset) == false);

        Plugboard board = createPlugBoard();
        machine = new Enigma(leftRotor,middleRotor,rightRotor,
                            leftOffset.charAt(0),middleOffset.charAt(0),rightOffset.charAt(0),
                            leftSetting.charAt(0),middleSetting.charAt(0),rightSetting.charAt(0),
                            board);
    }

    public static boolean checkRotorsValidity(String left, String middle, String right) {
        HashSet<String> rotorsOption = new HashSet<String>();
        rotorsOption.add("I");
        rotorsOption.add("II");
        rotorsOption.add("III");
        rotorsOption.add("IV");
        rotorsOption.add("V");


        if (rotorsOption.contains(left) && rotorsOption.contains(middle) && rotorsOption.contains(right))
            return true;
        else
        {
            System.out.println("Wrong entered rotors number!");
            return false;
        }
    }

    public static boolean checkOffsets(String left, String middle, String right) {
        if (isLetter(left.charAt(0)) && isLetter(middle.charAt(0)) && isLetter(right.charAt(0)))
            return true;
        else
        {
            System.out.println("Incorrect offset, input again...");
            return false;
        }
    }

    public static boolean checkSettings(String left, String middle, String right) {
        if (isLetter(left.charAt(0)) && left.length()==1 &&
                isLetter(middle.charAt(0)) && middle.length()==1&&
                isLetter(right.charAt(0)) && right.length()==1)
            return true;
        else
        {
            System.out.println("Incorrect settings, input again...");
            return false;
        }
    }

    public static boolean isLetter(char letter) {
        return (letter >='A' && letter <='Z');
    }

    public static void decrypt() {
        String in = input.nextLine().toUpperCase();
        in = in.replaceAll(" ",""); // remove spaces
        if(in.matches("[A-Z]*"))              // check only A-Z symbols in message
            for (int i = 0; i < in.length(); i++)
                System.out.print(machine.step(in.charAt(i)));   // decrypt message letter by letter calling to machine
        else
            System.out.println("Prohibited symbols in message! \nterminating ...");
        System.out.println("");
    }
}
