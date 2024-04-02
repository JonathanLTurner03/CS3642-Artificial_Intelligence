package Assignments.A3_Perceptron;

import java.text.NumberFormat;
import java.util.Scanner;

// Temp file to create the Perceptron package.
public class Driver {

    /**
     * Starts the program by creating and starting the sentinel loop.
     * @param args the program args
     */
    public static void main (String[] args) {
        Driver program = new Driver();
        program.start();
    }

    /**
     * Gets user input to a number. A prompt must be provided prior to running this method.
     * If the input was not an integer/(unable to be autoboxed), returns -1.
     *
     * @return the integer input, if invalid -1
     */
    private int getIntegerInput() {
        Scanner sc = new Scanner(System.in);
        try {
            String textInput = sc.nextLine();
            return Integer.parseInt(textInput);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Used as the main sentential loop and loops until the exit option is selected.
     */
    public void start() {
        int option = this.getOption();
        while (option != 3) {
            this.executeOption(option);
            option = this.getOption();
        }
    }

    /**
     * Executes the option selected by the user.
     * @param option the option selected by the user
     */
    public void executeOption(int option) {
        if (option == 1) {
            LogicOrPerceptron perceptron = new LogicOrPerceptron();
            perceptron.beginTraining();
        } else {
            BrightPerceptron perceptron = new BrightPerceptron();
            perceptron.beginTraining();
        }
        System.out.println();
    }

    /**
     * Displays the menu options.
     */
    private void displayMenu() {
        System.out.println("-----------------MAIN MENU-----------------");
        System.out.println("1. Logical OR Perceptron Training");
        System.out.println("2. Bright Perceptron Training");
        System.out.println("3. Exit program");
        System.out.println();
    }

    /**
     * Prints out the menu of options, asks for an input, and if that input is invalid it prints an error and
     * prompts the user again for the input.
     *
     * @return the option selected
     */
    private int getOption() {
        this.displayMenu(); // Prints out the menu

        // Asks for the option and begins the loop until a valid option is gathered.
        System.out.print("Enter option number: ");
        int input = getIntegerInput();
        while (input < 1 || input > 3) { // Compares it to the valid options available.
            System.out.println("\nPlease enter a valid input.");
            System.out.print("Enter option number: ");
            input = getIntegerInput();
        }
        System.out.println();
        return input;
    }

}
