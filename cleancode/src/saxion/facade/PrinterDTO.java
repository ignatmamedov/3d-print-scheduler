package saxion.facade;

import saxion.facade.SpoolDTO;
import java.util.List;

public record PrinterDTO(
        int id,
        String name,
        String manufacturer,
        boolean isHoused,
        Integer maxX,
        Integer maxY,
        Integer maxZ,
        Integer maxColors,
        List<SpoolDTO> spools
) {}

