package saxion.facade;

import saxion.types.FilamentType;

public record SpoolDTO(
        int id,
        String color,
        FilamentType filamentType,
        double length
) {}