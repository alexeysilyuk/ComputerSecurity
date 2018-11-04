import java.util.HashSet;
import java.util.Scanner;


public class Driver {
    private static Enigma machine;
    private static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        initMachine();
        machine.printRotorsStatus();
        decrypt();
        machine.printRotorsStatus();
    }

    public static void initMachine() {
        getRotorsFromUser();
    }

    public static void getRotorsFromUser() {
/*
        String leftRotor, middleRotor, rightRotor;
        String leftSetting,middleSetting,rightSetting,leftOffset,middleOffset,rightOffset;
        System.out.println("Options for rotor chosing : I, II, III, IV, V");
        System.out.print("Please choose Left Rotor: ");
        leftRotor = input.nextLine();
        System.out.print("Please choose Middle Rotor: ");
        middleRotor = input.nextLine();
        System.out.print("Please choose Right Rotor: ");
        rightRotor = input.nextLine();

        if(!checkRotorsValidity(leftRotor,middleRotor,rightRotor))
            System.out.println("Inputed incorrect rotor number!");

        System.out.print(leftRotor+ " rotor Setting: ");
        leftSetting=input.nextLine();
        System.out.print(middleRotor+ " rotor Setting: ");
        middleSetting=input.nextLine();
        System.out.print(rightRotor + " rotor Setting: ");
        rightSetting=input.nextLine();
        if(!checkSettings(leftSetting,middleSetting,rightSetting))
            System.out.println("Inputed settings are incorrect! Only A-Z charachters");

        System.out.print(leftRotor+ " rotor Offset: ");
        leftOffset=input.nextLine();
        System.out.print(middleRotor + " rotor Offset: ");
        middleOffset=input.nextLine();
        System.out.print(rightRotor + " rotor Offset: ");
        rightOffset=input.nextLine();

        if(!checkOffsets(leftOffset,middleOffset,rightOffset))
            System.out.println("Inputed offsets are incorrect! Only A-Z charachters");


        machine = new Enigma(leftRotor,middleRotor,rightRotor,leftOffset,middleOffset,rightOffset,leftSetting,middleSetting,rightSetting);
*/

        machine = new Enigma("I", "II", "IV",
                'S', 'D', 'I',
                'C', 'H', 'F');

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
            return false;
    }

    public static boolean checkOffsets(String left, String middle, String right) {
        return (isLetter(left) && isLetter(middle) && isLetter(right));
    }

    public static boolean checkSettings(String left, String middle, String right) {
        return (isLetter(left) && isLetter(middle) && isLetter(right));
    }

    public static boolean isLetter(String letter) {
        return (letter.matches("[A-Z]"));
    }

    public static void decrypt() {
        String in = input.nextLine();
        for (int i = 0; i < in.length(); i++)
            machine.step(in.charAt(i));

        System.out.println("");
    }
}
