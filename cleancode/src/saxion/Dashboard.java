package saxion;

import saxion.observer.Observer;
import saxion.observer.PrintEvent;

public class Dashboard implements Observer {
    private int spoolChangeCount;
    private int printsFulfilled;

    public Dashboard(PrintManager printManager) {
        printManager.addObserver(this);
    }

    @Override
    public void update(PrintEvent event) {
        this.spoolChangeCount = event.getSpoolChangeCount();
        this.printsFulfilled = event.getPrintsFulfilled();
    }

    public String getStats() {
        return "==================== DASHBOARD ====================\n" +
                "Spool changes: " + spoolChangeCount + "\n" +
                "Prints fulfilled: " + printsFulfilled + "\n" +
                "===================================================";
    }
}