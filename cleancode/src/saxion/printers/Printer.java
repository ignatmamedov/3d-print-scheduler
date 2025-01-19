package saxion.printers;

import saxion.models.Spool;
import saxion.models.Print;
import saxion.facade.PrinterDTO;
import saxion.models.PrintTask;

import java.util.List;

/**
 * Represents a 3D printer with various properties and operations.
 * This is an abstract class that must be extended by specific printer implementations.
 */
public abstract class Printer {

    /** The unique identifier of the printer. */
    private final int id;

    /** The name of the printer. */
    private final String name;

    /** The current print task assigned to the printer. */
    private PrintTask task;

    /** The manufacturer of the printer. */
    private final String manufacturer;

    /** Whether the printer is housed or enclosed. */
    private final boolean isHoused;

    /**
     * Constructs a new {@code Printer} instance with the specified properties.
     *
     * @param id          the unique identifier of the printer
     * @param printerName the name of the printer
     * @param manufacturer the manufacturer of the printer
     * @param isHoused     whether the printer is housed or enclosed
     */
    public Printer(int id, String printerName, String manufacturer, boolean isHoused) {
        this.id = id;
        this.name = printerName;
        this.manufacturer = manufacturer;
        this.isHoused = isHoused;
    }

    /**
     * Constructs a new {@code Printer} instance with the specified properties,
     * defaulting to an unhoused printer.
     *
     * @param id          the unique identifier of the printer
     * @param printerName the name of the printer
     * @param manufacturer the manufacturer of the printer
     */
    public Printer(
            int id,
            String printerName,
            String manufacturer
    ) {
        this(id, printerName, manufacturer, false);
    }

    /**
     * Gets the current print task assigned to the printer.
     *
     * @return the current {@link PrintTask}
     */
    public PrintTask getTask() {
        return task;
    }

    /**
     * Assigns a print task to the printer.
     *
     * @param task the {@link PrintTask} to assign
     */
    public void setTask(PrintTask task) {
        this.task = task;
    }

    /**
     * Gets the manufacturer of the printer.
     *
     * @return the manufacturer's name
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * Checks if the printer is housed or enclosed.
     *
     * @return {@code true} if the printer is housed; {@code false} otherwise
     */
    public boolean isHoused() {
        return isHoused;
    }

    /**
     * Gets the unique identifier of the printer.
     *
     * @return the printer ID
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of the printer.
     *
     * @return the printer name
     */
    public String getName() {
        return name;
    }

    /**
     * Calculates the estimated time required to complete a print task.
     *
     * @param filename the name of the file to print
     * @return the estimated print time in minutes
     */
    public abstract int calculatePrintTime(String filename);

    /**
     * Checks if the specified print fits within the printer's constraints.
     *
     * @param print the {@link Print} object to check
     * @return {@code true} if the print fits; {@code false} otherwise
     */
    public abstract boolean printFits(Print print);

    /**
     * Gets the current spools loaded in the printer.
     *
     * @return a {@link List} of {@link Spool} objects representing the current spools
     */
    public abstract List<Spool> getCurrentSpools();

    /**
     * Sets the current spools loaded in the printer.
     *
     * @param spools a {@link List} of {@link Spool} objects to load into the printer
     */
    public abstract void setCurrentSpools(List<Spool> spools);

    /**
     * Converts this {@code Printer} to a {@link PrinterDTO} for data transfer.
     *
     * @return a {@code PrinterDTO} representation of this printer
     */
    public PrinterDTO toDTO() {
        return new PrinterDTO(
                id,
                name,
                manufacturer,
                isHoused,
                null, null, null, null, null,
                getTask() != null ? getTask().toDTO() : null
        );
    }

    /**
     * Converts this {@code Printer} to a string representation.
     *
     * @return the name of the printer
     */
    @Override
    public String toString() {
        return getName();
    }
}
