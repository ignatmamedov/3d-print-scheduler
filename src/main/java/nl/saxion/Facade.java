package nl.saxion;

import facade.PrintDTO;
import models.Print;
import nl.saxion.menu.MenuPrinter;
import nl.saxion.new_test_classes.PrintManagerRefactored;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Facade {
    private final PrintManagerRefactored printManager;
    private final MenuPrinter menuPrinter;

    //// todo: store data in printmanager
    private int printSize;
    List<String> colors;
    List<String> selectedColors;

    List<String> prints;

    public Facade() {
        this.printManager = new PrintManagerRefactored();
        this.menuPrinter = new MenuPrinter();
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

    public Iterator<PrintDTO> getPrints() {
        return printManager.getPrints().stream()
                .map(Print::toDTO)
                .iterator();
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

    public String getAvailablePrints(){
        prints = printManager.getAvailablePrints().stream().map(printTask -> printTask.getName()).toList();
        printSize = prints.size();
        return menuPrinter.displayOptions(prints, "Prints");
    }

    public Integer getPrintSize(){
        return printSize;
    }

    public String getFilamentTypesOptions(){
       return menuPrinter.displayOptions(List.of("PLA", "PETG", "ABS"), "Filament Types ");
    }

    public String getColorsOptions(Integer filamentType){
        colors = printManager.getAvailableColors(printManager.getFilamentType(filamentType), 1);
        return menuPrinter.displayOptions(colors, "Colors");
    }

    public Integer getFilamentColorsNumber(Integer printChoice){
        return printManager.getAvailablePrints().get(printChoice - 1).getFilamentLength().size();
    }

    public String getColorsOptions(){
        return menuPrinter.displayOptions(colors, "Colors");
    }

    public Integer getColorsSize(){
        return colors.size();
    }

    public void prepareSelectedColorsList(){
        selectedColors = new ArrayList<>();
    }

    public void addSelectedColors(Integer colorChoice){
        selectedColors.add(colors.get(colorChoice - 1));
    }

    public void addNewPrintTask(Integer printChoice, Integer filamentType) {
        printManager.addNewPrintTask(prints.get(printChoice - 1), filamentType, colors);
    }

    public void displayMenu(){
     //  return menuPrinter.displayMenu();
    }

    public void readPrintsFromFile(String filename, boolean header) throws FileNotFoundException {
            printManager.readPrints(filename, header);
    }

    public void readSpoolsFromFile(String filename, boolean header) throws FileNotFoundException {
        printManager.readSpools(filename, header);
    }

    public void readPrintersFromFile(String filename, boolean header) throws FileNotFoundException {
        printManager.readPrinters(filename, header);
    }
}
