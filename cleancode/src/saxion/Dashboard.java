package saxion;

import saxion.observer.Observer;
import saxion.observer.PrintEvent;

/**
 * The `Dashboard` class serves as an observer that tracks and displays statistics
 * about spool changes and fulfilled prints in the printing system.
 */
public class Dashboard implements Observer {

    /** The number of spool changes recorded. */
    private int spoolChangeCount;

    /** The number of prints successfully fulfilled. */
    private int printsFulfilled;

    /**
     * Constructs a new {@code Dashboard} and registers it as an observer
     * to the specified {@link PrintManager}.
     *
     * @param printManager the {@link PrintManager} to observe
     */
    public Dashboard(PrintManager printManager) {
        printManager.addObserver(this);
    }

    /**
     * Updates the dashboard with data from a new print event.
     *
     * @param event the {@link PrintEvent} containing information about spool changes
     *              and fulfilled prints
     */
    @Override
    public void update(PrintEvent event) {
        this.spoolChangeCount = event.getSpoolChangeCount();
        this.printsFulfilled = event.getPrintsFulfilled();
    }

    /**
     * Retrieves the current dashboard statistics as a formatted string.
     *
     * @return a string displaying the number of spool changes and prints fulfilled
     */
    public String getStats() {
        return "==================== DASHBOARD ====================\n" +
                "Spool changes: " + spoolChangeCount + "\n" +
                "Prints fulfilled: " + printsFulfilled + "\n" +
                "===================================================";
    }
}