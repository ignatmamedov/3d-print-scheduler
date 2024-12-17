package nl.saxion.new_test_classes;

import nl.saxion.handlers.PrintTaskHandler;
import nl.saxion.handlers.PrinterHandler;
import nl.saxion.handlers.SpoolHandler;

public class PrintManagerRefactored {
    private final PrintTaskHandler printTaskHandler;
    private final PrinterHandler printerHandler;
    private final SpoolHandler spoolHandler;
    // DataGenerator (factory)

    public PrintManagerRefactored() {
        this.printTaskHandler = new PrintTaskHandler();
        this.printerHandler = new PrinterHandler();
        this.spoolHandler = new SpoolHandler();
    }

    public void addNewPrintTask() {
        printTaskHandler.addNewPrintTask();
    }

    public void registerPrintCompletion() {
        printTaskHandler.registerPrintCompletion();
    }

    public void registerPrinterFailure() {
        printerHandler.registerPrinterFailure();
    }

    public void changePrintStrategy() {
        // Change the print strategy (strategy pattern)
    }

    public void startPrintQueue() {
        printTaskHandler.startPrintQueue();
    }

    public void showPrints() {
        printTaskHandler.showPrints();
    }

    public void showPrinters() {
        printerHandler.showPrinters();
    }

    public void showSpools() {
        spoolHandler.showSpools();
    }

    public void showPendingPrintTasks() {
        printTaskHandler.showPendingPrintTasks();
    }

    public void addSpools(){}

    public void addPrinters(){}

    public void addPrintTasks(){}
}
