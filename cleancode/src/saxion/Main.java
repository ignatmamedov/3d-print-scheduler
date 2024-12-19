package saxion;

import nl.saxion.Models.PrintTask;
import nl.saxion.Models.Printer;
import saxion.facade.*;
import saxion.input.ConsoleInput;
import saxion.input.UserInput;
import saxion.view.TerminalView;
import saxion.view.View;

import java.util.Iterator;
import java.util.List;

public class Main {
    private final UserInput consoleInput = new ConsoleInput();
    private final Facade facade = new Facade();
    private final View<String> terminal = new TerminalView();

    public static void main(String[] args) {
        new Main().run(args);
    }

    public void run(String[] args) {
        try {
            facade.readData(args);
        } catch (Exception e) {
            terminal.show("Failed to read files");
            e.printStackTrace();
        }

        int choice = 1;
        while (choice > 0 && choice < 10) {
            // display menu should return string
            terminal.show(facade.displayMenu());
            choice = consoleInput.getIntInput(0, 9);
            switch (choice) {
                case 0 -> {break;}
                case 1 -> addNewPrintTask();
                case 2 -> facade.registerPrintCompletion();
                case 3 -> facade.registerPrinterFailure();
                case 4 -> facade.changePrintStrategy();
                case 5 -> facade.startPrintQueue();
                case 6 -> showPrints();
                case 7 -> showPrinters();
                case 8 -> showSpools();
                case 9 -> showPendingPrintTasks();
            }
        }
    }

    public void addNewPrintTask() {
        terminal.show(facade.getAvailablePrints());
        int printChoice = consoleInput.getIntInput("Print number: ", 1, facade.getPrintSize());

        terminal.show(facade.getFilamentTypesOptions());
        int filamentType = consoleInput.getIntInput("Filament type: ", 1, 3);

        terminal.show(facade.getColorsOptions(filamentType));

        facade.prepareSelectedColorsList();
        for (int i = 0; i < facade.getFilamentColorsNumber(printChoice); i++) {
            int colorChoice = consoleInput.getIntInput("Color number: ", 1, facade.getColorsSize());
            facade.addSelectedColors(colorChoice);
        }

        terminal.show(facade.addNewPrintTask(printChoice, filamentType));
    }

    public void showPrints(){
        terminal.show("---------- Available prints ----------");
        for (Iterator<PrintDTO> it = facade.getPrints(); it.hasNext(); ) {
            PrintDTO printDTO = it.next();
            terminal.show(terminal.formatPrintDTO(printDTO));
            terminal.show("--------------------------------------");
        }
    }

    private void showSpools() {
        terminal.show("---------- Spools ----------");
        for (Iterator<SpoolDTO> it = facade.getSpools(); it.hasNext(); ) {
            SpoolDTO spoolDTO = it.next();
            terminal.show(terminal.formatSpoolDTO(spoolDTO));
        }
        terminal.show("----------------------------");
    }

    private void showPendingPrintTasks() {
        terminal.show("--------- Pending Print Tasks ---------");
        for (Iterator<PrintTaskDTO> it = facade.getPendingPrintTasks(); it.hasNext(); ) {
            PrintTaskDTO printTaskDTO = it.next();
            terminal.show(terminal.formatPrintTaskDTO(printTaskDTO));
        }
        terminal.show("----------------------------");
    }

    private void showPrinters() {
        terminal.show("--------- Available printers ---------");
        for (Iterator<PrinterDTO> it = facade.getPrinters(); it.hasNext(); ) {
            PrinterDTO printerDTO = it.next();
            terminal.show(terminal.formatPrinterDTO(printerDTO));
        }
        terminal.show("----------------------------");
}

}
