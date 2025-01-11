package saxion;

import saxion.dataprovider.DataProvider;
import saxion.handlers.PrintTaskHandler;
import saxion.handlers.PrinterHandler;
import saxion.handlers.SpoolHandler;
import saxion.models.Print;
import saxion.models.PrintTask;
import saxion.models.Spool;
import saxion.observer.Observable;
import saxion.observer.Observer;
import saxion.observer.PrintEvent;
import saxion.printers.Printer;
import saxion.strategy.EfficientSpoolChange;
import saxion.strategy.LessSpoolChanges;
import saxion.types.FilamentType;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class PrintManager implements Observable, Observer {
    private final PrintTaskHandler printTaskHandler;
    private final PrinterHandler printerHandler;
    private final SpoolHandler spoolHandler;
    private final DataProvider dataProvider;
    private List<Print> prints;

    private List<PrintTask> pendingPrintTasks = new ArrayList<>();
    private List<String> selectedColors;

    private final List<Observer> observers = new ArrayList<>();
    private final LessSpoolChanges lessSpoolChanges = new LessSpoolChanges();
    private final EfficientSpoolChange efficientSpoolChange = new EfficientSpoolChange();
    private int spoolChangeCount = 0;
    private int printsFulfilled = 0;

    public PrintManager() {
        this.printTaskHandler = new PrintTaskHandler(lessSpoolChanges);
        this.printerHandler = new PrinterHandler();
        this.spoolHandler = new SpoolHandler();
        this.dataProvider = new DataProvider();

        lessSpoolChanges.addObserver(this);
        efficientSpoolChange.addObserver(this);
    }

    public List<Print> getPrints() {
        return List.copyOf(prints);
    }

    public int getPrintsSize(){
        return prints.size();
    }

    public SpoolHandler getSpoolHandler() {
        return spoolHandler;
    }
    public PrinterHandler getPrinterHandler() {
        return printerHandler;
    }

    public String addNewPrintTask(String printName, int filamentType) {
        try {
            FilamentType type = FilamentType.getFilamentType(filamentType);
            Print print = getPrintByName(printName);
            spoolHandler.validateColors(selectedColors, type);

            return printTaskHandler.addNewPrintTask(print, selectedColors, type);
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



    public String finalizeRunningTask(int printerId, boolean isSuccessful) {
        Printer printer = printerHandler.getRunningPrinterById(printerId);
        PrintTask task = removeTaskFromPrinter(printer);
        if (!isSuccessful) {
            pendingPrintTasks.add(task);
        } else {
            printsFulfilled++;
            notifyObservers();
        }
        spoolHandler.reduceSpoolLength(printer, task);
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

    public String startPrintQueue() {
        StringBuilder result = new StringBuilder();
        for (Printer printer : printerHandler.getPrinters()) {
            if (printer.getTask() == null) {
                String output = selectPrintTask(printer);
                if (output != null && !output.isEmpty()) {
                    result.append(output);
                    result.append(System.lineSeparator());
                }
            }
        }

        return result.toString();
    }

    public void setPrintingStrategy(int strategyChoice) {
        switch (strategyChoice) {
            case 1 -> printTaskHandler.setPrintingStrategy(lessSpoolChanges);
            case 2 -> printTaskHandler.setPrintingStrategy(efficientSpoolChange);
        }
    }

    public List<String> getAvailableStrategies() {
        return List.of("Less spool changes", "Efficient Spool usage");
    }

    public String selectPrintTask(int printerId) {
        try {
            Printer printer = printerHandler.getPrinterById(printerId);
            return selectPrintTask(printer);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    private String selectPrintTask(Printer printer) {
        return printTaskHandler.selectPrintTask(printer, spoolHandler.getFreeSpools());
    }

    public void createSelectedColorsList() {
        selectedColors = new ArrayList<>();
    }

    public void addSelectedColors(Integer filamentType, Integer colorChoice) {
        List<String> colors = spoolHandler.getAvailableColors(filamentType);
        selectedColors.add(colors.get(colorChoice - 1));
    }

    public void readData(String[] args) throws FileNotFoundException {
        String printsFile = args.length > 0 ? args[0] : "";
        String spoolsFile = args.length > 1 ? args[1] : "";
        String printersFile = args.length > 2 ? args[2] : "";
        prints = dataProvider.readFromFile(printsFile, Print.class, true);
        spoolHandler.setSpools(dataProvider.readFromFile(spoolsFile, Spool.class, true));
        setPrinters(dataProvider.readFromFile(printersFile, Printer.class, true));
    }

    public void setPrinters(List<Printer> printers){
        printerHandler.setPrinters(printers);
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        PrintEvent event = new PrintEvent(spoolChangeCount, printsFulfilled);
        for (Observer observer : observers) {
            observer.update(event);
        }
    }

    @Override
    public void update(PrintEvent event) {
        spoolChangeCount += event.getSpoolChangeCount();
        printsFulfilled += event.getPrintsFulfilled();
    }
}
