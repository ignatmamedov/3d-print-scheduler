package saxion;

import saxion.dataprovider.DataProvider;
import saxion.handlers.PrintTaskHandler;
import saxion.handlers.PrinterHandler;
import saxion.handlers.SpoolHandler;
import saxion.models.Print;
import saxion.models.PrintTask;
import saxion.models.Spool;
import saxion.printers.Printer;
import saxion.printers.StandardFDM;
import saxion.strategy.EfficientSpoolChange;
import saxion.strategy.LessSpoolChanges;
import saxion.types.FilamentType;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class PrintManager {
    private final PrintTaskHandler printTaskHandler;
    private final PrinterHandler printerHandler;
    private final SpoolHandler spoolHandler;
    private final DataProvider dataProvider;
    private List<Spool> spools;
    private List<Spool> freeSpools;
    private List<Print> prints;
    private List<Printer> printers;

    private List<PrintTask> pendingPrintTasks = new ArrayList<>();

    public PrintManager() {
        this.printTaskHandler = new PrintTaskHandler();
        this.printerHandler = new PrinterHandler();
        this.spoolHandler = new SpoolHandler();
        this.dataProvider = new DataProvider();
    }

    public List<Print> getAvailablePrints() {
        return List.copyOf(prints);
    }

    public String addNewPrintTask(String printName, int filamentType, List<String> colors) {
        try {
            FilamentType type = getFilamentType(filamentType);
            Print print = getPrintByName(printName);
            validateColors(colors, type);

            return printTaskHandler.addNewPrintTask(print, colors, type);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    public List<PrintTask> getPendingPrintTasks() {
        return printTaskHandler.getPendingPrintTasks();
    }

    private Print getPrintByName(String printName) {
        return prints.stream()
                .filter(p -> p.getName().equals(printName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Print not found"));
    }

    private void validateColors(List<String> colors, FilamentType type) {
        for (String color : colors) {
            boolean found = spools.stream()
                    .anyMatch(spool -> spool.getColor().equals(color) && spool.getFilamentType() == type);
            if (!found) {
                throw new IllegalArgumentException("Color " + color + " (" + type + ") not found");
            }
        }
    }

    public FilamentType getFilamentType(int filamentType) {
        return switch (filamentType) {
            case 1 -> FilamentType.PLA;
            case 2 -> FilamentType.PETG;
            case 3 -> FilamentType.ABS;
            default -> throw new IllegalArgumentException("Invalid filament type");
        };
    }

    public String finalizeRunningTask(int printerId, boolean isSuccessful) {
        Printer printer = getPrinterById(printerId);
        PrintTask task = removeTaskFromPrinter(printer);
        if (!isSuccessful) {
            pendingPrintTasks.add(task);
        }
        return "Task " + task + " removed from printer " + printer.getName();
    }

    public PrintTask removeTaskFromPrinter(Printer printer) {
        PrintTask task = printer.getTask();
        printer.setTask(null);
        List<Spool> spools = printer.getCurrentSpools();
        for (int i = 0; i < spools.size() && i < task.getColors().size(); i++) {
            spools.get(i).reduceLength(task.getPrint().getFilamentLength().get(i));
        }
        return task;
    }

    public Printer getPrinterById(int printerId) {
        return printers.stream()
                .filter(printer -> printer.getId() == printerId && printer.getTask() != null)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Cannot find a running task on printer with ID " + printerId
                ));
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

    public int getRunningPrintTasksSize() {
        return printTaskHandler.getRunningPrintTasksSize();
    }

    public void startPrintQueue() {
        for (Printer printer : printers) {
            if (printer.getTask() == null) {
                selectPrintTask(printer);
            }
        }
    }

    public List<Print> getPrints() {
        return prints;
    }

    public List<Spool> getSpools() {
        return spools;
    }

    public List<Printer> getPrinters() {
        return printers;
    }

    public void showPendingPrintTasks() {
        printTaskHandler.showPendingPrintTasks();
    }

    public void readSpools(String filename, boolean header) throws FileNotFoundException {
        if (filename.isEmpty()) {
            filename = dataProvider.DEFAULT_SPOOLS_FILE;
        }
        spools = dataProvider.readFromFile(filename, Spool.class, header);
        freeSpools = new ArrayList<>(spools);
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
        printTaskHandler.setPrinters(printers);
    }

    public void setPrintingStrategy(int strategyChoice) {
        switch (strategyChoice) {
            case 1 -> {
                printTaskHandler.setPrintingStrategy(new LessSpoolChanges());
            }
            case 2 -> {
                printTaskHandler.setPrintingStrategy(new EfficientSpoolChange());
            }
        }
    }

    public List<String> getAvailableStrategies() {
        return List.of("Less spool changes", "Efficient Spool usage");
    }

    public String selectPrintTask(int printerId) {
        try {
            Printer printer = getPrinterById(printerId);
            return selectPrintTask(printer);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    private String selectPrintTask(Printer printer) {
        return printTaskHandler.selectPrintTask(printer, freeSpools);
    }

}
