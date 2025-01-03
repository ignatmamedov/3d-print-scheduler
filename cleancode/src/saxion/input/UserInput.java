package saxion.input;

import java.util.List;
import java.util.Optional;

public interface UserInput {
    String getStringInput();

    int getIntInput(String message, Integer min, Integer max);

    int getIntInput(List<Integer> availableOptions);

    default int getIntInput() {
        return getIntInput(null,null, null);
    }

    default int getIntInput(Integer min, Integer max) {
        return getIntInput(null, min, max);
    }
}
