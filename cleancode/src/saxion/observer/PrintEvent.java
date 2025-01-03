package saxion.observer;

public class PrintEvent {
    private final int spoolChangeCount;
    private final int printsFulfilled;

    public PrintEvent(int spoolChangeCount, int printsFulfilled) {
        this.spoolChangeCount = spoolChangeCount;
        this.printsFulfilled = printsFulfilled;
    }

    public int getSpoolChangeCount() {
        return spoolChangeCount;
    }

    public int getPrintsFulfilled() {
        return printsFulfilled;
    }
}
