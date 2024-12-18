package saxion.input;

import java.util.Scanner;

public class ConsoleInput implements UserInput{
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public String getStringInput() {
        return scanner.nextLine();
    }

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

    @Override
    public int getIntInput() {
        return getIntInput(null, null, null);
    }
}
