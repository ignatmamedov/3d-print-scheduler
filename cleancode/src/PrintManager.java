import dataprovider.DataProvider;
import handlers.PrintTaskHandler;
import handlers.PrinterHandler;
import handlers.SpoolHandler;
import models.Print;
import models.Spool;
import nl.saxion.Models.FilamentType;
import printers.Printer;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class PrintManager {
    private final PrintTaskHandler printTaskHandler;
    private final PrinterHandler printerHandler;
    private final SpoolHandler spoolHandler;
    private final DataProvider dataProvider;
    private List<Spool> spools;
    private List<Print> prints;
    List<Printer> printers;

    public PrintManager() {
        this.printTaskHandler = new PrintTaskHandler();
        this.printerHandler = new PrinterHandler();
        this.spoolHandler = new SpoolHandler();
        this.dataProvider = new DataProvider();
    }

    public List<Print> getAvailablePrints() {
        return List.copyOf(prints);
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

    public List<String> getAvailableColors(int filamentType) {
        try {
            FilamentType type = getFilamentType(filamentType);

            List<String> availableColors = new ArrayList<>();
            for (Spool spool : spools) {
                if (spool.getFilamentType() == type && !availableColors.contains(spool.getColor())) {
                    availableColors.add(spool.getColor());
                }
            }

            return availableColors;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return null;
        }
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

    public List<Print> getPrints(){
        return prints;
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

    public void readSpools(String filename, boolean header) throws FileNotFoundException {
        if (filename.isEmpty()) {
            filename = dataProvider.DEFAULT_SPOOLS_FILE;
        }
        spools = dataProvider.readFromFile(filename, Spool.class, header);
    }

    public void readPrints(String filename, boolean header) throws FileNotFoundException {
        if (filename.isEmpty()) {
            filename = dataProvider.DEFAULT_PRINTS_FILE;
        }
        prints = dataProvider.readFromFile(filename, Print.class, header);
    }

    public void readPrinters(String filename, boolean header) throws FileNotFoundException {
        if (filename.isEmpty()) {
            filename = dataProvider.DEFAULT_PRINTERS_FILE;
        }
        printers = dataProvider.readFromFile(filename, Printer.class, header);
    }


    public void addPrinters(){}

    public void addPrintTasks(){}
}
