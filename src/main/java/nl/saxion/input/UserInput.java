package nl.saxion.input;

import java.util.Optional;

public interface UserInput {
    String getStringInput();

    int getIntInput(Integer min, Integer max);

    default int getIntInput() {
        return getIntInput(null, null);
    }
}
