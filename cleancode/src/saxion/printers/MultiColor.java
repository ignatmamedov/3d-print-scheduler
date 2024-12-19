package saxion.printers;

import saxion.facade.PrinterDTO;
import saxion.facade.SpoolDTO;
import saxion.models.Print;
import saxion.models.Spool;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MultiColor extends StandardFDM implements SpoolManager {
    private final int maxColors;
    private List<Spool> currentSpools;

    public MultiColor(int id, String printerName, String manufacturer, boolean isHoused, int maxX, int maxY, int maxZ, int maxColors) {
        super(id, printerName, manufacturer, isHoused, maxX, maxY, maxZ);
        this.maxColors = maxColors;
    }

    public MultiColor(int id, String printerName, String manufacturer, int maxX, int maxY, int maxZ, int maxColors, List<Spool> currentSpools) {
        super(id, printerName, manufacturer, maxX, maxY, maxZ);
        this.maxColors = maxColors;
        this.currentSpools = currentSpools;
    }

    public int getMaxColors() {
        return maxColors;
    }

    @Override
    public int calculatePrintTime(String filename) {
        return 0;
    }

    @Override
    public boolean printFits(Print print) {
        return false;
    }

    @Override
    public void setCurrentSpools(List<Spool> spools) {
        if (spools.size() > maxColors) {
            throw new IllegalArgumentException("Too many spools for this printer");
        }

        this.currentSpools = new ArrayList<>(spools);
    }

    @Override
    public List<Spool> getCurrentSpools() {
        return currentSpools != null ? new ArrayList<>(currentSpools) : new ArrayList<>();
    }

    @Override
    public PrinterDTO toDTO() {
        List<SpoolDTO> spools = currentSpools != null
                ? currentSpools.stream()
                .filter(Objects::nonNull)
                .map(Spool::toDTO)
                .collect(Collectors.toList())
                : null;

        return new PrinterDTO(
                getId(),
                getName(),
                getManufacturer(),
                isHoused(),
                getMaxX(),
                getMaxY(),
                getMaxZ(),
                maxColors,
                spools,
                getTask() != null ? getTask().toDTO() : null
        );
    }
}
