package saxion.input;

import java.util.List;
import java.util.Scanner;

/**
 * The `ConsoleInput` class implements the {@link UserInput} interface to handle
 * user input from the console.
 */
public class ConsoleInput implements UserInput{

    /** A scanner for reading user input from the console. */
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Retrieves a string input from the user.
     *
     * @return the string entered by the user
     */
    @Override
    public String getStringInput() {
        return scanner.nextLine();
    }

    /**
     * Prompts the user to enter an integer within a specified range.
     *
     * @param message the message to display to the user before input
     * @param min     the minimum allowed value (inclusive), or {@code null} for no minimum
     * @param max     the maximum allowed value (inclusive), or {@code null} for no maximum
     * @return the integer entered by the user
     */
    @Override
    public int getIntInput(String message, Integer min, Integer max) {
        if(message != null) {
            System.out.println(message);
        }

        int input;
        while (true) {
            try {
                input = Integer.parseInt(scanner.nextLine());
                if (min != null && input < min) {
                    System.out.println("Input must be greater than or equal to " + min);
                    continue;
                }
                if (max != null && input > max) {
                    System.out.println("Input must be less than or equal to " + max);
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a number");
            }
        }
        return input;
    }

    /**
     * Prompts the user to enter an integer from a list of available options.
     *
     * @param availableOptions a list of valid integer options
     * @return the integer entered by the user
     */
    @Override
    public int getIntInput(List<Integer> availableOptions) {
        int input;
        while (true) {
            try {
                input = Integer.parseInt(scanner.nextLine());
                if (!availableOptions.contains(input)) {
                    System.out.println("Invalid input, please enter a valid option: " + availableOptions);
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a number");
            }
        }
        return input;
    }

    /**
     * Prompts the user to enter an integer without any constraints.
     *
     * @return the integer entered by the user
     */
    @Override
    public int getIntInput() {
        return getIntInput(null, null, null);
    }
}
