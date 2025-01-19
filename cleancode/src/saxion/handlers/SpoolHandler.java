package saxion.handlers;

import saxion.models.PrintTask;
import saxion.models.Spool;
import saxion.printers.Printer;
import saxion.types.FilamentType;

import java.util.ArrayList;
import java.util.List;

/**
 * Handler class for managing spools in a 3D printing system.
 * Provides methods to validate, retrieve, and update spool-related data.
 */
public class SpoolHandler {

    /** The list of all spools available in the system. */
    private List<Spool> spools;

    /** The list of free spools available for use. */
    private List<Spool> freeSpools;

    /**
     * Gets the list of all spools.
     *
     * @return a {@link List} of {@link Spool} objects
     */
    public List<Spool> getSpools() {
        return spools;
    }

    /**
     * Gets the list of free spools available for use.
     *
     * @return a {@link List} of {@link Spool} objects representing free spools
     */
    public List<Spool> getFreeSpools() {
        return freeSpools;
    }

    /**
     * Validates that the specified colors and filament type are available in the spools.
     *
     * @param colors a {@link List} of color strings to validate
     * @param type   the {@link FilamentType} to check against
     * @throws IllegalArgumentException if any of the specified colors are not available
     */
    public void validateColors(List<String> colors, FilamentType type) {
        for (String color : colors) {
            boolean found = spools.stream()
                    .anyMatch(spool -> spool.getColor().equals(color) && spool.getFilamentType() == type);
            if (!found) {
                throw new IllegalArgumentException("Color " + color + " (" + type + ") not found");
            }
        }
    }

    /**
     * Retrieves a list of available colors for the specified filament type.
     *
     * @param filamentType the filament type as an integer
     * @return a {@link List} of available color strings, or {@code null} if the filament type is invalid
     */
    public List<String> getAvailableColors(int filamentType) {
        try {
            FilamentType type = FilamentType.getFilamentType(filamentType);

            List<String> availableColors = new ArrayList<>();
            for (Spool spool : spools) {
                if (spool.getFilamentType() == type && !availableColors.contains(spool.getColor())) {
                    availableColors.add(spool.getColor());
                }
            }
            return availableColors;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Reduces the length of spools based on the requirements of a print task.
     * Removes empty spools from the printer's current spools.
     *
     * @param printer the {@link Printer} executing the task
     * @param task    the {@link PrintTask} being executed
     */
    public void reduceSpoolLength(Printer printer, PrintTask task) {
        List<Spool> spools = printer.getCurrentSpools();
        for (int i = 0; i < spools.size() && i < task.getColors().size(); i++) {
            spools.get(i).reduceLength(task.getPrint().getFilamentLength().get(i));
        }
    }

    /**
     * Sets the list of all spools and initializes the list of free spools.
     *
     * @param spools a {@link List} of {@link Spool} objects
     */
    public void setSpools(List<Spool> spools) {
        this.spools = spools;
        this.freeSpools = new ArrayList<>(spools);
    }
}
