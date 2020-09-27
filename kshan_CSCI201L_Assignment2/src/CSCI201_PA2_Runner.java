import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CSCI201_PA2_Runner {

    /** Get the file name and parse Json */
    public static InputCodes[] inputParse() {
        System.out.println("Enter an input file");

        boolean running = true;
        while (running) {
            Scanner inputSC = new Scanner(System.in);
            String path = inputSC.nextLine();

            try {
                Gson gson = new Gson();
                BufferedReader file = new BufferedReader(new FileReader(path));

                InputCodes[] codes = gson.fromJson(file, InputCodes[].class);

                running = false;
                return codes;
            }
            catch (FileNotFoundException error) {
                System.out.println("File not found, enter a new input file");
            }
        }

        return null;
    }

    /** Handle the input option and execute on passed in dictionary */
    public static void handler(Dictionary D) {
        boolean running = true;
        while (running) {
            System.out.println("What would you like to do with your database of codes?\n" +
                    "\t1) Get frequency of a code\n" +
                    "\t2) Check if a code was guessed\n" +
                    "\t3) Remove a code\n" +
                    "\t4) quit");

            Scanner inputSC = new Scanner(System.in);

            try {
                int option = inputSC.nextInt();
                String placeHolder = inputSC.nextLine();

                if (option == 1) {
                    System.out.println("Enter a code to check its frequency");

                    String toCheck = inputSC.nextLine();

                    int frequency = D.frequency(toCheck);
                    System.out.println(toCheck + " was guessed by " + frequency + " teammates");
                } else if (option == 2) {
                    System.out.println("Enter a code to check if it was guessed by a teammate");
                    String toCheck = inputSC.nextLine();

                    boolean result = D.contains(toCheck);
                    if (result) {
                        System.out.println(toCheck + " was guessed by a teammate");
                    } else {
                        System.out.println(toCheck + " was not guessed by a teammate");
                    }
                } else if (option == 3) {
                    System.out.println("Enter a code to remove");
                    String toRemove = inputSC.nextLine();

                    int frequency = D.frequency(toRemove);
                    for (int i = 0; i < frequency; i++) {
                        D.remove(toRemove);
                    }
                    System.out.println(toRemove + " was removed from your database");
                } else if (option == 4) {
                    System.out.println("Quitting, have a nice day!");
                    running = false;
                }
            }
            catch (InputMismatchException error) {
                System.out.println("Invalid input, enter again");
            }
        }
    }


    public static void main(String[] args) {
        InputCodes[] codes = inputParse();
        Dictionary<String> InputCodes = new Dictionary<>();

        for (InputCodes temp : codes) {
            for (int i = 0; i < temp.codes.length; i++) {
                InputCodes.add(temp.codes[i]);
            }
        }

        handler(InputCodes);
    }

}
