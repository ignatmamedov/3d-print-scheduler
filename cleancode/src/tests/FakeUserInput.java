package tests;
import saxion.input.UserInput;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FakeUserInput implements UserInput {
    private Queue<String> inputs = new LinkedList<>();

    public void addInput(int input) {
        inputs.add(String.valueOf(input));
    }

    @Override
    public String getStringInput() {
        if (!inputs.isEmpty()) {
            return inputs.poll();
        }
        throw new IllegalStateException("No more inputs available.");
    }

    @Override
    public int getIntInput(String message, Integer min, Integer max) {
        String input = getStringInput();
        try {
            int value = Integer.parseInt(input);
            if (min != null && value < min || max != null && value > max) {
                throw new IllegalArgumentException("Input value " + value + " is out of range: " + min + " - " + max);
            }
            return value;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid integer input: " + input);
        }
    }

    @Override
    public int getIntInput(List<Integer> availableOptions) {
        String input = getStringInput();
        try {
            int value = Integer.parseInt(input);
            if (availableOptions != null && !availableOptions.contains(value)) {
                throw new IllegalArgumentException("Input value " + value + " is not in the available options: " + availableOptions);
            }
            return value;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid integer input: " + input);
        }
    }

    @Override
    public int getIntInput() {
        return getIntInput(null, null, null);
    }

    @Override
    public int getIntInput(Integer min, Integer max) {
        return getIntInput(null, min, max);
    }

    public void clearInputs(){
        this.inputs = new LinkedList<>();
    }
}
