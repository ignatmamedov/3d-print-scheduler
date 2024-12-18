package saxion.facade;

import java.util.ArrayList;

public record PrintDTO(
        String name,
        int height,
        int width,
        int length,
        ArrayList<Double> filamentLength,
        int printTime
){}