package saxion.facade;

import saxion.types.FilamentType;

import java.util.List;

public record PrintTaskDTO(
        String print,
        List<String> colors,
        FilamentType filamentType
) {}
