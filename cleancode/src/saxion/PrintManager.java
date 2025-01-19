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

/**
 * The main manager class for handling printing tasks, printers, and spools.
 * Implements the {@link Observable} and {@link Observer} interfaces to handle
 * observer pattern notifications.
 */
public class PrintManager implements Observable, Observer {
    private final PrintTaskHandler printTaskHandler;
    private final PrinterHandler printerHandler;
    private final SpoolHandler spoolHandler;
    private final DataProvider dataProvider;
    private List<Print> prints;

    private List<String> selectedColors;

    private final List<Observer> observers = new ArrayList<>();
    private final LessSpoolChanges lessSpoolChanges = new LessSpoolChanges();
    private final EfficientSpoolChange efficientSpoolChange = new EfficientSpoolChange();
    private int spoolChangeCount = 0;
    private int printsFulfilled = 0;

    /**
     * Constructs a new {@code PrintManager} and initializes its handlers and strategies.
     */
    public PrintManager() {
        this.printTaskHandler = new PrintTaskHandler(lessSpoolChanges);
        this.printerHandler = new PrinterHandler();
        this.spoolHandler = new SpoolHandler();
        this.dataProvider = new DataProvider();

        lessSpoolChanges.addObserver(this);
        efficientSpoolChange.addObserver(this);
    }

    /**
     * Retrieves the list of prints managed by this manager.
     *
     * @return an unmodifiable list of {@link Print} objects
     */
    public List<Print> getPrints() {
        return List.copyOf(prints);
    }

    /**
     * Retrieves the number of prints managed by this manager.
     *
     * @return the size of the prints list
     */
    public int getPrintsSize(){
        return prints.size();
    }

    /**
     * Retrieves the spool handler used by this manager.
     *
     * @return the {@link SpoolHandler} instance
     */
    public SpoolHandler getSpoolHandler() {
        return spoolHandler;
    }

    /**
     * Retrieves the printer handler used by this manager.
     *
     * @return the {@link PrinterHandler} instance
     */
    public PrinterHandler getPrinterHandler() {
        return printerHandler;
    }

    /**
     * Adds a new print task to the queue.
     *
     * @param printName    the name of the print
     * @param filamentType the type of filament required for the task
     * @return a message indicating whether the task was successfully added or an error occurred
     */
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

    /**
     * Retrieves the list of pending print tasks.
     *
     * @return a list of {@link PrintTask} objects
     */
    public List<PrintTask> getPendingPrintTasks() {
        return printTaskHandler.getPendingPrintTasks();
    }

    /**
     * Retrieves a print object by its name.
     *
     * @param printName the name of the print to retrieve
     * @return the {@link Print} object with the specified name
     * @throws IllegalArgumentException if no print with the specified name is found
     */
    private Print getPrintByName(String printName) {
        return prints.stream()
                .filter(p -> p.getName().equals(printName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Print not found"));
    }

    /**
     * Finalizes a running task on a printer.
     *
     * @param printerId    the ID of the printer
     * @param isSuccessful whether the task was successfully completed
     * @return a message indicating the status of the task finalization
     */
    public String finalizeRunningTask(int printerId, boolean isSuccessful) {
        Printer printer = printerHandler.getRunningPrinterById(printerId);
        PrintTask task = removeTaskFromPrinter(printer);
        if (!isSuccessful) {
            printTaskHandler.addNewPrintTask(task);
        } else {
            printsFulfilled++;
            notifyObservers();
        }
        spoolHandler.reduceSpoolLength(printer, task);
        return "Task " + task.getPrint().getName() + " "
                + task.getFilamentType() + " removed from printer " + printer.getName();
    }

    /**
     * Removes a task from a printer and updates spool usage.
     *
     * @param printer the {@link Printer} to remove the task from
     * @return the removed {@link PrintTask}
     */
    public PrintTask removeTaskFromPrinter(Printer printer) {
        PrintTask task = printer.getTask();
        printer.setTask(null);
        List<Spool> spools = printer.getCurrentSpools();
        for (int i = 0; i < spools.size() && i < task.getColors().size(); i++) {
            spools.get(i).reduceLength(task.getPrint().getFilamentLength().get(i));
        }
        return task;
    }

    /**
     * Starts the print queue, assigning tasks to available printers.
     *
     * @return a string summarizing the tasks assigned to printers
     */
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

    /**
     * Sets the printing strategy based on user choice.
     *
     * @param strategyChoice the strategy choice: 1 for Less Spool Changes, 2 for Efficient Spool Usage
     */
    public void setPrintingStrategy(int strategyChoice) {
        switch (strategyChoice) {
            case 1 -> printTaskHandler.setPrintingStrategy(lessSpoolChanges);
            case 2 -> printTaskHandler.setPrintingStrategy(efficientSpoolChange);
        }
    }

    /**
     * Retrieves the available printing strategies.
     *
     * @return a list of strategy names
     */
    public List<String> getAvailableStrategies() {
        return List.of("Less spool changes", "Efficient Spool usage");
    }

    /**
     * Selects a print task for the specified printer by its ID.
     *
     * @param printerId the ID of the printer for which a print task is being selected
     * @return a string describing the selected print task or an error message if no task could be selected
     */
    public String selectPrintTask(int printerId) {
        try {
            Printer printer = printerHandler.getPrinterById(printerId);
            return selectPrintTask(printer);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    /**
     * Selects a print task for the specified printer.
     *
     * @param printer the {@link Printer} for which a print task is being selected
     * @return a string describing the selected print task or {@code null} if no task could be selected
     */
    private String selectPrintTask(Printer printer) {
        return printTaskHandler.selectPrintTask(printer, spoolHandler.getFreeSpools());
    }


    /**
     * Creates a new list to store the selected colors for a print task.
     * This method initializes the {@code selectedColors} field as an empty list.
     */
    public void createSelectedColorsList() {
        selectedColors = new ArrayList<>();
    }

    /**
     * Adds a selected color to the list of selected colors for a print task.
     *
     * @param filamentType the type of filament as an integer
     * @param colorChoice  the index of the selected color
     * @throws IndexOutOfBoundsException if the colorChoice is invalid
     */
    public void addSelectedColors(Integer filamentType, Integer colorChoice) {
        List<String> colors = spoolHandler.getAvailableColors(filamentType);
        selectedColors.add(colors.get(colorChoice - 1));
    }

    /**
     * Reads data from the specified files to initialize prints, spools, and printers.
     *
     * @param args an array containing file names for prints, spools, and printers in order
     * @throws FileNotFoundException if any of the files are not found
     */
    public void readData(String[] args) throws FileNotFoundException {
        String printsFile = args.length > 0 ? args[0] : "";
        String spoolsFile = args.length > 1 ? args[1] : "";
        String printersFile = args.length > 2 ? args[2] : "";
        prints = dataProvider.readFromFile(printsFile, Print.class, true);
        spoolHandler.setSpools(dataProvider.readFromFile(spoolsFile, Spool.class, true));
        setPrinters(dataProvider.readFromFile(printersFile, Printer.class, true));
    }

    /**
     * Sets the list of printers managed by this manager.
     *
     * @param printers the list of {@link Printer} objects to set
     */
    public void setPrinters(List<Printer> printers){
        printerHandler.setPrinters(printers);
    }

    /**
     * Adds an observer to be notified of print-related events.
     *
     * @param observer the {@link Observer} to add
     */
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer from the list of observers.
     *
     * @param observer the {@link Observer} to remove
     */
    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all observers about the current print-related events, such as spool changes
     * or completed prints.
     */
    @Override
    public void notifyObservers() {
        PrintEvent event = new PrintEvent(spoolChangeCount, printsFulfilled);
        for (Observer observer : observers) {
            observer.update(event);
        }
    }

    /**
     * Updates the manager with a new print event. The event contains the number of spool changes
     * and completed prints to update the internal state of the manager.
     *
     * @param event the {@link PrintEvent} containing spool change and print fulfillment information
     */
    @Override
    public void update(PrintEvent event) {
        spoolChangeCount += event.getSpoolChangeCount();
        printsFulfilled += event.getPrintsFulfilled();
    }
}
