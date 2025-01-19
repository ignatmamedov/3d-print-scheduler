package saxion.printers;

import saxion.models.Print;
import saxion.models.Spool;
import saxion.facade.PrinterDTO;
import saxion.facade.SpoolDTO;

import java.util.List;

/**
 * Represents a standard FDM (Fused Deposition Modeling) 3D printer.
 * This printer supports a single spool and has defined maximum dimensions for prints.
 */
public class StandardFDM extends Printer implements SpoolManager {

    /** The maximum width (X dimension) the printer can handle in millimeters. */
    private final int maxX;

    /** The maximum depth (Y dimension) the printer can handle in millimeters. */
    private final int maxY;

    /** The maximum height (Z dimension) the printer can handle in millimeters. */
    private final int maxZ;

    /** The current spool loaded into the printer. */
    private Spool currentSpool;

    /**
     * Constructs a new {@code StandardFDM} printer with the specified properties.
     *
     * @param id           the unique identifier of the printer
     * @param printerName  the name of the printer
     * @param manufacturer the manufacturer of the printer
     * @param isHoused     whether the printer is housed or enclosed
     * @param maxX         the maximum width (X dimension) in millimeters
     * @param maxY         the maximum depth (Y dimension) in millimeters
     * @param maxZ         the maximum height (Z dimension) in millimeters
     */
    public StandardFDM(
            int id,
            String printerName,
            String manufacturer,
            boolean isHoused,
            int maxX,
            int maxY,
            int maxZ
    ) {
        super(id, printerName, manufacturer, isHoused);
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    /**
     * Constructs a new {@code StandardFDM} printer with the specified properties, defaulting to an unhoused printer.
     *
     * @param id           the unique identifier of the printer
     * @param printerName  the name of the printer
     * @param manufacturer the manufacturer of the printer
     * @param maxX         the maximum width (X dimension) in millimeters
     * @param maxY         the maximum depth (Y dimension) in millimeters
     * @param maxZ         the maximum height (Z dimension) in millimeters
     */
    public StandardFDM(int id, String printerName, String manufacturer, int maxX, int maxY, int maxZ) {
        super(id, printerName, manufacturer);
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    /**
     * Gets the maximum width (X dimension) the printer can handle.
     *
     * @return the maximum width in millimeters
     */
    public int getMaxX() {
        return maxX;
    }

    /**
     * Gets the maximum depth (Y dimension) the printer can handle.
     *
     * @return the maximum depth in millimeters
     */
    public int getMaxY() {
        return maxY;
    }

    /**
     * Gets the maximum height (Z dimension) the printer can handle.
     *
     * @return the maximum height in millimeters
     */
    public int getMaxZ() {
        return maxZ;
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
     * Checks if the specified print fits within the printer's maximum dimensions.
     *
     * @param print the {@link Print} object to check
     * @return {@code true} if the print fits; {@code false} otherwise
     */
    @Override
    public boolean printFits(Print print) {
        return print.getHeight() <= maxZ && print.getWidth() <= maxX && print.getLength() <= maxY;
    }

    /**
     * Sets the current spool for the printer. Only one spool is allowed.
     *
     * @param spools a {@link List} containing a single {@link Spool}
     * @throws IllegalArgumentException if more than one spool is provided
     */
    @Override
    public void setCurrentSpools(List<Spool> spools) {
        if (spools.size() != 1) {
            throw new IllegalArgumentException("StandardFDM can only handle one spool");
        }
        this.currentSpool = spools.get(0);
    }

    /**
     * Gets the current spool loaded in the printer.
     *
     * @return a {@link List} containing the current spool, or an empty list if no spool is loaded
     */
    @Override
    public List<Spool> getCurrentSpools() {
        return currentSpool != null ? List.of(currentSpool) : List.of();
    }

    /**
     * Converts this {@code StandardFDM} printer to a {@link PrinterDTO}.
     *
     * @return a {@code PrinterDTO} representation of this printer
     */
    @Override
    public PrinterDTO toDTO() {
        List<SpoolDTO> spools = currentSpool != null
                ? List.of(currentSpool.toDTO())
                : null;

        return new PrinterDTO(
                getId(),
                getName(),
                getManufacturer(),
                isHoused(),
                maxX,
                maxY,
                maxZ,
                null,
                spools,
                getTask() != null ? getTask().toDTO() : null
        );
    }
}
