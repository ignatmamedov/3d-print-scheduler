package saxion.facade;

import saxion.Dashboard;
import saxion.PrintManager;
import saxion.menu.MenuPrinter;
import saxion.models.Print;
import saxion.models.PrintTask;
import saxion.models.Spool;
import saxion.printers.Printer;
import saxion.types.FilamentType;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;

/**
 * The `Facade` class serves as a unified interface to simplify interaction with the underlying
 * printing system components, including print tasks, printers, spools, and strategies.
 */
public class Facade {
    private final PrintManager printManager;
    private final Dashboard dashboard;
    private final MenuPrinter menuPrinter;

    /**
     * Constructs a new {@code Facade} and initializes its components.
     */
    public Facade() {
        this.printManager = new PrintManager();
        this.menuPrinter = new MenuPrinter();
        this.dashboard = new Dashboard(printManager);
    }

    /**
     * Retrieves the current dashboard statistics.
     *
     * @return a string containing the dashboard statistics
     */
    public String getDashboardStats() {
        printManager.notifyObservers();
        return dashboard.getStats();
    }

    /**
     * Registers the status of a printer after a task is completed.
     *
     * @param printerId    the ID of the printer
     * @param isSuccessful whether the task was successfully completed
     * @return a string containing the result of the status registration
     */
    public String registerPrinterStatus(int printerId, boolean isSuccessful) {
        String result = "-----------------------------------\n";
        result += printManager.finalizeRunningTask(printerId, isSuccessful);
        result += System.lineSeparator();
        result += printManager.selectPrintTask(printerId);
        return result;
    }

    /**
     * Adds a new print task to the system.
     *
     * @param printChoice   the index of the selected print
     * @param filamentType  the type of filament for the task
     * @return a string describing the result of adding the task
     */
    public String addNewPrintTask(Integer printChoice, Integer filamentType) {
        List<String> prints = printManager.getPrints().stream().map(Print::getName).toList();
        return printManager.addNewPrintTask(prints.get(printChoice - 1), filamentType);
    }

    /**
     * Retrieves the list of available prints as a formatted string.
     *
     * @return a string displaying the available prints
     */
    public String getAvailablePrints() {
        List<String> prints = printManager.getPrints().stream().map(Print::getName).toList();
        return menuPrinter.displayOptions(prints, "Prints", true);
    }

    /**
     * Retrieves the available colors for a specific filament type.
     *
     * @param filamentType the filament type as an integer
     * @return a string displaying the available colors
     */
    public String getColorsOptions(Integer filamentType) {
        List<String> colors = printManager.getSpoolHandler().getAvailableColors(filamentType);
        List<String> colorsWithFilament = colors.stream().map(color -> color + " (" + FilamentType.getFilamentType(filamentType) + ")").toList();
        return menuPrinter.displayOptions(colorsWithFilament, "Colors", true);
    }

    /**
     * Retrieves the list of available printing strategies as a formatted string.
     *
     * @return a string displaying the available strategies
     */
    public String getAvailableStrategies() {
        return menuPrinter.displayOptions(printManager.getAvailableStrategies(), "Strategies", true);
    }

    /**
     * Displays the main menu.
     *
     * @return a string representing the menu
     */
    public String displayMenu() {
        return menuPrinter.displayMenu();
    }

    /**
     * Retrieves the list of filament types as a formatted string.
     *
     * @return a string displaying the available filament types
     */
    public String getFilamentTypesOptions() {
        return menuPrinter.displayOptions(List.of("PLA", "PETG", "ABS"), "Filament Types ", true);
    }

    /**
     * Reads data from files to initialize prints, spools, and printers.
     *
     * @param args an array of file paths for prints, spools, and printers
     * @throws FileNotFoundException if any of the files cannot be found
     */
    public void readData(String[] args) throws FileNotFoundException {
        printManager.readData(args);
    }

    /**
     * Retrieves the number of available prints.
     *
     * @return the size of the prints list
     */
    public Integer getPrintSize() {
        return printManager.getPrintsSize();
    }

    /**
     * Changes the current printing strategy.
     *
     * @param strategyChoice the index of the chosen strategy
     */
    public void changePrintStrategy(int strategyChoice) {
        printManager.setPrintingStrategy(strategyChoice);
    }

    /**
     * Retrieves the number of available printing strategies.
     *
     * @return the size of the strategies list
     */
    public Integer getStrategiesSize() {
        return printManager.getAvailableStrategies().size();
    }

    /**
     * Starts the print queue by assigning tasks to available printers.
     *
     * @return a string summarizing the tasks started
     */
    public String startPrintQueue() {
        return printManager.startPrintQueue();
    }

    /**
     * Retrieves the number of filament colors required for a specific print.
     *
     * @param printChoice the index of the selected print
     * @return the number of filament colors
     */
    public Integer getFilamentColorsNumber(Integer printChoice) {
        return printManager.getPrints().get(printChoice - 1).getFilamentLength().size();
    }

    /**
     * Retrieves the number of available colors for a specific filament type.
     *
     * @param filamentType the filament type as an integer
     * @return the number of available colors
     */
    public Integer getColorsSize(Integer filamentType) {
        return printManager.getSpoolHandler().getAvailableColors(filamentType).size();
    }

    /**
     * Initializes the selected colors list for a print task.
     */
    public void createSelectedColorsList() {
        printManager.createSelectedColorsList();
    }

    /**
     * Adds a selected color to the print task.
     *
     * @param filamentType the filament type as an integer
     * @param colorChoice  the index of the selected color
     */
    public void addSelectedColors(Integer filamentType, Integer colorChoice) {
        printManager.addSelectedColors(filamentType, colorChoice);
    }

    /**
     * Retrieves an iterator for the list of prints as DTOs.
     *
     * @return an iterator of {@link PrintDTO} objects
     */
    public Iterator<PrintDTO> getPrints() {
        return printManager.getPrints().stream()
                .map(Print::toDTO)
                .iterator();
    }

    /**
     * Retrieves an iterator for the list of spools as DTOs.
     *
     * @return an iterator of {@link SpoolDTO} objects
     */
    public Iterator<SpoolDTO> getSpools() {
        return printManager.getSpoolHandler().getSpools().stream()
                .map(Spool::toDTO)
                .iterator();
    }

    /**
     * Retrieves an iterator for the list of pending print tasks as DTOs.
     *
     * @return an iterator of {@link PrintTaskDTO} objects
     */
    public Iterator<PrintTaskDTO> getPendingPrintTasks() {
        return printManager.getPendingPrintTasks().stream()
                .map(PrintTask::toDTO)
                .iterator();
    }

    /**
     * Retrieves an iterator for the list of printers as DTOs.
     *
     * @return an iterator of {@link PrinterDTO} objects
     */
    public Iterator<PrinterDTO> getPrinters() {
        return printManager.getPrinterHandler().getPrinters().stream()
                .map(Printer::toDTO)
                .iterator();
    }

    /**
     * Retrieves an iterator for the list of printers that are currently running tasks as DTOs.
     *
     * @return an iterator of {@link PrinterDTO} objects for running printers
     */
    public Iterator<PrinterDTO> getRunningPrinters() {
        return printManager.getPrinterHandler().getPrinters().stream()
                .filter(printer -> printer.getTask() != null)
                .map(Printer::toDTO)
                .iterator();
    }

    /**
     * Retrieves a list of IDs of printers that are currently running tasks.
     *
     * @return a list of printer IDs
     */
    public List<Integer> getRunningPrintersIds() {
        return printManager.getPrinterHandler().getPrinters().stream()
                .filter(printer -> printer.getTask() != null)
                .map(Printer::getId)
                .toList();
    }

    /**
     * Sets the list of printers in the system.
     *
     * @param printers the list of {@link Printer} objects
     */
    public void setPrinters(List<Printer> printers){
        printManager.setPrinters(printers);
    }
}
