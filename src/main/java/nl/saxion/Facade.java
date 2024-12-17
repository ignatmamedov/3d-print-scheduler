package nl.saxion;

import nl.saxion.handlers.PrintTaskHandler;
import nl.saxion.handlers.PrinterHandler;
import nl.saxion.handlers.SpoolHandler;
import nl.saxion.new_test_classes.PrintManagerRefactored;

public class Facade {
    private final PrintManagerRefactored printManager;

    public Facade() {
        this.printManager = new PrintManagerRefactored();
    }

    public void addNewPrintTask() {
        //printManager.addNewPrintTask();
    }

    public void registerPrintCompletion() {
        //printManager.registerPrintCompletion();
    }

    public void registerPrinterFailure() {
        //printManager.registerPrinterFailure();
    }

    public void changePrintStrategy() {
        //printManager.changePrintStrategy();
    }

    public void startPrintQueue() {
        //printManager.startPrintQueue();
    }

    public void showPrints() {
        //printManager.showPrints();
    }

    public void showPrinters() {
        //printManager.showPrinters();
    }

    public void showSpools() {
        //printManager.showSpools();
    }

    public void showPendingPrintTasks() {
        //printManager.showPendingPrintTasks();
    }
}
