package saxion.printers;

import saxion.facade.PrinterDTO;
import saxion.facade.SpoolDTO;
import saxion.models.Spool;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents a multi-color 3D printer that can handle multiple spools of different colors.
 * Extends the {@link StandardFDM} printer to support multi-color printing.
 */
public class MultiColor extends StandardFDM implements SpoolManager {

    /** The maximum number of colors (spools) the printer can handle. */
    private final int maxColors;

    /** The list of current spools loaded into the printer. */
    private List<Spool> currentSpools;

    /**
     * Constructs a new {@code MultiColor} printer with the specified properties.
     *
     * @param id           the unique identifier of the printer
     * @param printerName  the name of the printer
     * @param manufacturer the manufacturer of the printer
     * @param isHoused     whether the printer is housed or enclosed
     * @param maxX         the maximum width (X dimension) in millimeters
     * @param maxY         the maximum depth (Y dimension) in millimeters
     * @param maxZ         the maximum height (Z dimension) in millimeters
     * @param maxColors    the maximum number of colors (spools) the printer can handle
     */
    public MultiColor(
            int id,
            String printerName,
            String manufacturer,
            boolean isHoused,
            int maxX,
            int maxY,
            int maxZ,
            int maxColors
    ) {
        super(id, printerName, manufacturer, isHoused, maxX, maxY, maxZ);
        this.maxColors = maxColors;
    }

    /**
     * Constructs a new {@code MultiColor} printer with the specified properties and an initial list of spools.
     *
     * @param id           the unique identifier of the printer
     * @param printerName  the name of the printer
     * @param manufacturer the manufacturer of the printer
     * @param maxX         the maximum width (X dimension) in millimeters
     * @param maxY         the maximum depth (Y dimension) in millimeters
     * @param maxZ         the maximum height (Z dimension) in millimeters
     * @param maxColors    the maximum number of colors (spools) the printer can handle
     * @param currentSpools the initial list of spools loaded into the printer
     */
    public MultiColor(
            int id,
            String printerName,
            String manufacturer,
            int maxX,
            int maxY,
            int maxZ,
            int maxColors,
            List<Spool> currentSpools
    ) {
        super(id, printerName, manufacturer, maxX, maxY, maxZ);
        this.maxColors = maxColors;
        this.currentSpools = currentSpools;
    }

    /**
     * Gets the maximum number of colors (spools) the printer can handle.
     *
     * @return the maximum number of colors
     */
    public int getMaxColors() {
        return maxColors;
    }

    /**
     * Calculates the estimated print time for the given file.
     *
     * @param filename the name of the file to print
     * @return the estimated print time in minutes (default is 0 in this implementation)
     */
    @Override
    public int calculatePrintTime(String filename) {
        return 0;
    }

    /**
     * Sets the current spools for the printer. Ensures the number of spools does not exceed the maximum allowed.
     *
     * @param spools a {@link List} of {@link Spool} objects to load into the printer
     * @throws IllegalArgumentException if the number of spools exceeds the maximum allowed
     */
    @Override
    public void setCurrentSpools(List<Spool> spools) {
        if (spools.size() > maxColors) {
            throw new IllegalArgumentException("Too many spools for this printer");
        }
        this.currentSpools = new ArrayList<>(spools);
    }

    /**
     * Gets the current spools loaded into the printer.
     *
     * @return a {@link List} of {@link Spool} objects representing the current spools
     */
    @Override
    public List<Spool> getCurrentSpools() {
        return currentSpools != null ? new ArrayList<>(currentSpools) : new ArrayList<>();
    }

    /**
     * Converts this {@code MultiColor} printer to a {@link PrinterDTO}.
     *
     * @return a {@code PrinterDTO} representation of this printer
     */
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
