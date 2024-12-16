package nl.saxion;

import nl.saxion.handlers.PrintTaskHandler;
import nl.saxion.handlers.PrinterHandler;
import nl.saxion.handlers.SpoolHandler;

public class Facade {
    private final PrintTaskHandler printTaskHandler;
    private final PrinterHandler printerHandler;
    private final SpoolHandler spoolHandler;

    public Facade() {
        this.printTaskHandler = new PrintTaskHandler();
        this.printerHandler = new PrinterHandler();
        this.spoolHandler = new SpoolHandler();
    }

    public void addNewPrintTask() {
        //printTaskHandler.addNewPrintTask();
    }

    public void registerPrintCompletion() {
        //printTaskHandler.registerPrintCompletion();
    }

    public void registerPrinterFailure() {
        //printerHandler.registerPrinterFailure();
    }

    public void changePrintStrategy() {
        //printerHandler.changePrintStrategy();
    }

    public void startPrintQueue() {
        //spoolHandler.startPrintQueue();
    }

    public void showPrints() {
        //printTaskHandler.showPrints();
    }

    public void showPrinters() {
        //printerHandler.showPrinters();
    }

    public void showSpools() {
        //spoolHandler.showSpools();
    }

    public void showPendingPrintTasks() {
        //printTaskHandler.showPendingPrintTasks();
    }
}
