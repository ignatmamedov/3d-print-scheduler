package saxion.printers;

import saxion.models.Spool;

import java.util.List;

/**
 * Interface for managing spools in a 3D printer.
 * Provides methods to set and retrieve the current spools loaded in the printer.
 */
public interface SpoolManager {

    /**
     * Sets the current spools loaded into the printer.
     *
     * @param spools a {@link List} of {@link Spool} objects to load into the printer
     */
    void setCurrentSpools(List<Spool> spools);

    /**
     * Gets the current spools loaded into the printer.
     *
     * @return a {@link List} of {@link Spool} objects representing the current spools
     */
    List<Spool> getCurrentSpools();
}
