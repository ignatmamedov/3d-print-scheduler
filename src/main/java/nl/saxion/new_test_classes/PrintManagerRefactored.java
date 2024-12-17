package nl.saxion.new_test_classes;

import nl.saxion.handlers.PrintTaskHandler;
import nl.saxion.handlers.PrinterHandler;
import nl.saxion.handlers.SpoolHandler;

public class PrintManagerRefactored {
    private final PrintTaskHandler printTaskHandler;
    private final PrinterHandler printerHandler;
    private final SpoolHandler spoolHandler;
    // DataGenerator

    public PrintManagerRefactored() {
        this.printTaskHandler = new PrintTaskHandler();
        this.printerHandler = new PrinterHandler();
        this.spoolHandler = new SpoolHandler();
    }

    public void addNewPrintTask() {
    }

    public void registerPrintCompletion() {
    }

    public void registerPrinterFailure() {
    }

    public void changePrintStrategy() {
    }

    public void startPrintQueue() {
    }

    public void showPrints() {
    }

    public void showPrinters() {
    }

    public void showSpools() {
    }

    public void showPendingPrintTasks() {
    }
}
