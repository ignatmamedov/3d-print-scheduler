package saxion.facade;

import saxion.Dashboard;
import saxion.PrintManager;
import saxion.menu.MenuPrinter;
import saxion.models.Print;
import saxion.models.PrintTask;
import saxion.models.Spool;
import saxion.printers.Printer;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;


public class Facade {
    private final PrintManager printManager;
    private final Dashboard dashboard;
    private final MenuPrinter menuPrinter;

    public Facade() {
        this.printManager = new PrintManager();
        this.menuPrinter = new MenuPrinter();
        this.dashboard = new Dashboard(printManager);
    }

    public String getDashboardStats() {
        printManager.notifyObservers();
        return dashboard.getStats();
    }

    public void changePrintStrategy(int strategyChoice) {
        printManager.setPrintingStrategy(strategyChoice);
    }

    public String getAvailableStrategies() {
        return menuPrinter.displayOptions(printManager.getAvailableStrategies(), "Strategies", true);
    }

    public Integer getStrategiesSize() {
        return printManager.getAvailableStrategies().size();
    }

    public String startPrintQueue() {
        return printManager.startPrintQueue();
    }

    public Iterator<PrintDTO> getPrints() {
        return printManager.getPrints().stream()
                .map(Print::toDTO)
                .iterator();
    }

    public Iterator<SpoolDTO> getSpools() {
        return printManager.getSpools().stream()
                .map(Spool::toDTO)
                .iterator();
    }

    public Iterator<PrintTaskDTO> getPendingPrintTasks() {
        return printManager.getPendingPrintTasks().stream()
                .map(PrintTask::toDTO)
                .iterator();
    }

    public Iterator<PrinterDTO> getPrinters() {
        return printManager.getPrinters().stream()
                .map(Printer::toDTO)
                .iterator();
    }

    public int getRunningPrintersNum() {
        return (int) printManager.getPrinters().stream()
                .filter(printer -> printer.getTask() != null)
                .count();
    }

    public Iterator<PrinterDTO> getRunningPrinters() {
        return printManager.getPrinters().stream()
                .filter(printer -> printer.getTask() != null)
                .map(Printer::toDTO)
                .iterator();
    }

    public String registerPrinterStatus(int printerId, boolean isSuccessful) {
        String result = "-----------------------------------\n";
        result += printManager.finalizeRunningTask(printerId, isSuccessful);
        result += printManager.selectPrintTask(printerId);
        return result;
    }

    public String getAvailablePrints() {
        List<String> prints = printManager.getPrints().stream().map(Print::getName).toList();
        return menuPrinter.displayOptions(prints, "Prints", true);
    }

    public Integer getPrintSize() {
        return printManager.getPrintsSize();
    }

    public String getFilamentTypesOptions() {
        return menuPrinter.displayOptions(List.of("PLA", "PETG", "ABS"), "Filament Types ", true);
    }

    public String getColorsOptions(Integer filamentType) {
        List<String> colors = printManager.getAvailableColors(filamentType);
        List<String> colorsWithFilament = colors.stream().map(color -> color + " (" + printManager.getFilamentType(filamentType) + ")").toList();
        return menuPrinter.displayOptions(colorsWithFilament, "Colors", true);
    }

    public Integer getFilamentColorsNumber(Integer printChoice) {
        return printManager.getPrints().get(printChoice - 1).getFilamentLength().size();
    }

    public Integer getColorsSize(Integer filamentType) {
        return printManager.getAvailableColors(filamentType).size();
    }

    public void createSelectedColorsList() {
        printManager.createSelectedColorsList();
    }

    public void addSelectedColors(Integer filamentType, Integer colorChoice) {
        printManager.addSelectedColors(filamentType, colorChoice);
    }

    public String addNewPrintTask(Integer printChoice, Integer filamentType) {
        List<String> prints = printManager.getPrints().stream().map(Print::getName).toList();
        return printManager.addNewPrintTask(prints.get(printChoice - 1), filamentType);
    }

    public String displayMenu() {
        return menuPrinter.displayMenu();
    }

    public void readData(String[] args) throws FileNotFoundException {
        String printsFile = args.length > 0 ? args[0] : "";
        String spoolsFile = args.length > 1 ? args[1] : "";
        String printersFile = args.length > 2 ? args[2] : "";
        printManager.readPrints(printsFile, true);
        printManager.readSpools(spoolsFile, true);
        printManager.readPrinters(printersFile, true);
    }

    public List<Integer> getRunningPrintersIds() {
        return printManager.getPrinters().stream()
                .filter(printer -> printer.getTask() != null)
                .map(Printer::getId)
                .toList();
    }
}
