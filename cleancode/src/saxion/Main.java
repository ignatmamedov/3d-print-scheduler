package saxion;

import saxion.facade.*;
import saxion.input.ConsoleInput;
import saxion.input.UserInput;
import saxion.view.TerminalView;
import saxion.view.View;

import java.util.Iterator;
import java.util.function.Function;

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
                case 0 -> {
                    break;
                }
                case 1 -> addNewPrintTask();
                case 2 -> registerPrintCompletion();
                case 3 -> registerPrinterFailure();
                case 4 -> changePrintStrategy();
                case 5 -> startPrintQueue();
                case 6 -> showPrints();
                case 7 -> showPrinters();
                case 8 -> showSpools();
                case 9 -> showPendingPrintTasks();
            }
        }
    }

    public void startPrintQueue() {
        terminal.show(facade.startPrintQueue());
    }

    public void changePrintStrategy() {
        terminal.show(facade.getAvailableStrategies());
        int strategyChoice = consoleInput.getIntInput("Strategy number: ", 1, facade.getStrategiesSize());
        facade.changePrintStrategy(strategyChoice);
    }

    public void addNewPrintTask() {
        terminal.show(facade.getAvailablePrints());
        int printChoice = consoleInput.getIntInput("Print number: ", 1, facade.getPrintSize());

        terminal.show(facade.getFilamentTypesOptions());
        int filamentType = consoleInput.getIntInput("Filament type: ", 1, 3);

        terminal.show(facade.getColorsOptions(filamentType));

        facade.createSelectedColorsList();
        for (int i = 0; i < facade.getFilamentColorsNumber(printChoice); i++) {
            int colorChoice = consoleInput.getIntInput("Color number: ", 1, facade.getColorsSize(filamentType));
            facade.addSelectedColors(filamentType, colorChoice);
        }

        terminal.show(facade.addNewPrintTask(printChoice, filamentType));
    }

    private void showSpools() {
        showSection("---------- Spools ----------", facade.getSpools(), terminal::formatSpoolDTO, null);
    }

    private void showPendingPrintTasks() {
        showSection("--------- Pending Print Tasks ---------", facade.getPendingPrintTasks(), terminal::formatPrintTaskDTO, null);
    }

    private void showPrinters() {
        showSection("--------- Available Printers ---------", facade.getPrinters(), terminal::formatPrinterDTO, null);
    }

    private void showPrints() {
        showSection("---------- Available Prints ----------", facade.getPrints(), terminal::formatPrintDTO, "--------------------------------------");
    }

    private void registerPrintCompletion() {
        registerPrinterStatus(true);
    }

    private void registerPrinterFailure() {
        registerPrinterStatus(false);
    }

    private <T> void showSection(String header, Iterator<T> iterator, Function<T, String> formatter, String itemSeparator) {
        terminal.show(header);
        while (iterator.hasNext()) {
            T item = iterator.next();
            terminal.show(formatter.apply(item));
            if (itemSeparator != null) {
                terminal.show(itemSeparator);
            }
        }
        terminal.show("----------------------------");
    }

    private void registerPrinterStatus(boolean isSuccess) {
        terminal.show("---------- Currently Running Printers ----------");
        int counter = 0;
        for (Iterator<PrinterDTO> it = facade.getRunningPrinters(); it.hasNext(); ) {
            PrinterDTO printerDTO = it.next();
            terminal.show(terminal.formatPrinterDTO(printerDTO));
            counter += 1;
        }

        if (counter > 0) {
            if (isSuccess) {
                terminal.show("- Printer that is done (ID): ");
            } else {
                terminal.show("- Printer ID that failed: ");
            }

            int printerId = consoleInput.getIntInput(-1, facade.getRunningPrintersNum());
            terminal.show(facade.registerPrinterStatus(printerId, isSuccess));
        }
    }
}
