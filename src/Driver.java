import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

// Driver class
public class Driver {

    private static Enigma machine;
    private static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        showWelcomeBanner();
        boolean running=true,debug=false;
        System.out.println("Run debug mode? ( y / n )");
        switch(input.nextLine().toLowerCase()){
            case "y":
                runDebugMode();
                running=false;
                break;
            default:
                System.out.println("Incorrect input, running Enigma Machine");
        }

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

    private static void runDebugMode() {
        Random rand = new Random();
        Plugboard plugboard;
        char randomLetter1,randomLetter2;
        int randomPlugs;
        String message = "LLLSDJFSLSKKSLGLKSKLSDLFV";
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        System.out.println("---------- DEBUG MODE ----------\n" +
                "Generating 10000 different machines\n" +
                "All machines rothors I, II, III\n" +
                "all machine will encode message \n" +
                "\"LLLSDJFSLSKKSLGLKSKLSDLFV\"" +
                "with random settings, offsets and plugboard \n" +
                           "--------------------------------");
        for (int i=0 ; i< 10000 ; i++){
            plugboard = new Plugboard();

            randomPlugs = rand.nextInt(10) + 0;
            //System.out.println("Generates "+randomPlugs+" plugs for machine "+i);
            for(int j=0;j<randomPlugs;j++){
                randomLetter1 = alphabet.charAt(rand.nextInt(alphabet.length()));
                randomLetter2 = alphabet.charAt(rand.nextInt(alphabet.length()));
                plugboard.setPlug(randomLetter1,randomLetter2);

            }
            plugboard.reverseArray();
            Enigma machine = new Enigma("I","II","III",
                    alphabet.charAt(rand.nextInt(alphabet.length())),alphabet.charAt(rand.nextInt(alphabet.length())),alphabet.charAt(rand.nextInt(alphabet.length())),
                    alphabet.charAt(rand.nextInt(alphabet.length())),alphabet.charAt(rand.nextInt(alphabet.length())),alphabet.charAt(rand.nextInt(alphabet.length())),
                    plugboard);

            for (int k = 0; k < message.length(); k++)
                machine.step(message.charAt(k));   // decrypt message letter by letter calling to machine
        }
        System.out.println("Done!");
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


    // code for this function was found in internet, it is just for creating cool message and not requested by task
    private static void showWelcomeBanner(){

        int width = 80;
        int height = 20;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setFont(new Font("SansSerif", Font.BOLD, 16));

        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.drawString("ENIGMA", 10, 20);

        for (int y = 0; y < height; y++) {
            StringBuilder sb = new StringBuilder();
            for (int x = 0; x < width; x++) {
                sb.append(image.getRGB(x, y) == -16777216 ? " " : "?");
            }

            if (sb.toString().trim().isEmpty()) {
                continue;
            }
            System.out.println(sb);
        }
    }
}

