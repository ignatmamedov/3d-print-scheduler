package saxion.observer;

/**
 * Represents an event related to printing activities, such as spool changes or completed prints.
 * This event is used in the observer pattern to notify observers of changes in the printing process.
 */
public class PrintEvent {

    /** The number of spool changes that occurred. */
    private final int spoolChangeCount;

    /** The number of prints that have been successfully fulfilled. */
    private final int printsFulfilled;

    /**
     * Constructs a new {@code PrintEvent} with the specified spool change count and prints fulfilled count.
     *
     * @param spoolChangeCount the number of spool changes that occurred
     * @param printsFulfilled  the number of prints that have been successfully fulfilled
     */
    public PrintEvent(int spoolChangeCount, int printsFulfilled) {
        this.spoolChangeCount = spoolChangeCount;
        this.printsFulfilled = printsFulfilled;
    }

    /**
     * Gets the number of spool changes that occurred.
     *
     * @return the spool change count
     */
    public int getSpoolChangeCount() {
        return spoolChangeCount;
    }

    /**
     * Gets the number of prints that have been successfully fulfilled.
     *
     * @return the prints fulfilled count
     */
    public int getPrintsFulfilled() {
        return printsFulfilled;
    }
}
