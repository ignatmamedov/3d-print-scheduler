package nl.saxion.new_test_classes;

import nl.saxion.Models.FilamentType;
import nl.saxion.Models.Print;
import nl.saxion.Models.PrintTask;
import nl.saxion.Models.Spool;
import nl.saxion.handlers.PrintTaskHandler;
import nl.saxion.handlers.PrinterHandler;
import nl.saxion.handlers.SpoolHandler;

import java.util.ArrayList;
import java.util.List;

public class PrintManagerRefactored {
    private final PrintTaskHandler printTaskHandler;
    private final PrinterHandler printerHandler;
    private final SpoolHandler spoolHandler;
    private final List<Spool> spools = new ArrayList<>();
    // DataGenerator (factory)

    public PrintManagerRefactored() {
        this.printTaskHandler = new PrintTaskHandler();
        this.printerHandler = new PrinterHandler();
        this.spoolHandler = new SpoolHandler();
    }

    public List<Print> getAvailablePrints() {
//        return printTaskHandler.getAvailablePrints(); or just return a list of prints
        return null;
    }

    public void addNewPrintTask(String printName, int filamentType, List<String> colors) {
        printTaskHandler.addNewPrintTask();
    }

    public FilamentType getFilamentType(int filamentType) {
        switch (filamentType) {
            case 1:
                return FilamentType.PLA;
            case 2:
                return FilamentType.PETG;
            case 3:
                return FilamentType.ABS;
            default:
                throw new IllegalArgumentException("Invalid filament type");
        }
    }

    public List<String> getAvailableColors(FilamentType type, int colorChoice) {
        List<String> availableColors = new ArrayList<>();
        for (Spool spool : spools) {
            if (spool.getFilamentType() == type && !availableColors.contains(spool.getColor())) {
                availableColors.add(spool.getColor());
            }
        }

        return List.of(availableColors.get(colorChoice - 1));
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
