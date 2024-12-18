package saxion.input;

import java.util.Optional;

public interface UserInput {
    String getStringInput();

    int getIntInput(String message, Integer min, Integer max);

    default int getIntInput() {
        return getIntInput(null,null, null);
    }

    default int getIntInput(Integer min, Integer max) {
        return getIntInput(null, min, max);
    }
}
