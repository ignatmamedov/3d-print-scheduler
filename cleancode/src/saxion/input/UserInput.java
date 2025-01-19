package saxion.input;

import java.util.List;
import java.util.Optional;

/**
 * The `UserInput` interface defines methods for handling user input.
 * It provides methods for retrieving string and integer inputs with
 * various constraints and options.
 */
public interface UserInput {

    /**
     * Retrieves a string input from the user.
     *
     * @return the string entered by the user
     */
    String getStringInput();

    /**
     * Prompts the user to enter an integer within a specified range, with an optional message.
     *
     * @param message the message to display to the user before input, or {@code null} for no message
     * @param min     the minimum allowed value (inclusive), or {@code null} for no minimum constraint
     * @param max     the maximum allowed value (inclusive), or {@code null} for no maximum constraint
     * @return the integer entered by the user
     */
    int getIntInput(String message, Integer min, Integer max);

    /**
     * Prompts the user to enter an integer from a list of available options.
     *
     * @param availableOptions a list of valid integer options
     * @return the integer entered by the user
     */
    int getIntInput(List<Integer> availableOptions);

    /**
     * Prompts the user to enter an integer without any constraints.
     *
     * @return the integer entered by the user
     */
    default int getIntInput() {
        return getIntInput(null,null, null);
    }

    /**
     * Prompts the user to enter an integer within a specified range.
     *
     * @param min the minimum allowed value (inclusive), or {@code null} for no minimum constraint
     * @param max the maximum allowed value (inclusive), or {@code null} for no maximum constraint
     * @return the integer entered by the user
     */
    default int getIntInput(Integer min, Integer max) {
        return getIntInput(null, min, max);
    }
}
