package saxion;

import saxion.facade.*;
import saxion.input.ConsoleInput;
import saxion.input.UserInput;
import saxion.view.TerminalView;
import saxion.view.View;

import java.util.Iterator;
import java.util.function.Function;

/**
 * The main entry point of the application, managing user interaction, invoking operations,
 * and displaying results using the {@link Facade} and related components.
 */
public class Main {

    /** Input handler for receiving user input. */
    private final UserInput consoleInput;

    /** Facade for accessing and managing the printing system. */
    public final Facade facade;

    /** View component for displaying output to the terminal. */
    private final View<String> terminal;

    /**
     * Constructs a new {@code Main} instance with the specified input, facade, and view components.
     *
     * @param consoleInput the input handler
     * @param facade       the facade for managing the printing system
     * @param terminal     the terminal view for displaying output
     */
    public Main(UserInput consoleInput, Facade facade, View<String> terminal) {
        this.consoleInput = consoleInput;
        this.facade = facade;
        this.terminal = terminal;
    }

    /**
     * The application's entry point.
     *
     * @param args command-line arguments, including paths to data files for initialization
     */
    public static void main(String[] args) {
        new Main(
                new ConsoleInput(),
                new Facade(),
                new TerminalView()
        ).run(args);
    }

    /**
     * Executes the main application loop, processing user input and displaying results.
     *
     * @param args command-line arguments passed during application startup
     */
    public void run(String[] args) {
        try {
            facade.readData(args);
        } catch (Exception e) {
            terminal.show("Failed to read files");
        }

        int choice = 1;
        while (choice > 0 && choice < 11) {
            terminal.show(facade.displayMenu());
            choice = consoleInput.getIntInput(0, 10);
            switch (choice) {
                case 0 -> {break;}
                case 1 -> addNewPrintTask();
                case 2 -> registerPrintCompletion();
                case 3 -> registerPrinterFailure();
                case 4 -> changePrintStrategy();
                case 5 -> startPrintQueue();
                case 6 -> showPrints();
                case 7 -> showPrinters();
                case 8 -> showSpools();
                case 9 -> showPendingPrintTasks();
                case 10 -> showDashboardStats();
            }
        }
    }

    /**
     * Displays dashboard statistics.
     */
    public void showDashboardStats() {
        terminal.show(facade.getDashboardStats());
    }

    /**
     * Starts the print queue, assigning tasks to available printers.
     */
    public void startPrintQueue() {
        terminal.show(facade.startPrintQueue());
    }

    /**
     * Allows the user to change the current printing strategy.
     */
    public void changePrintStrategy() {
        terminal.show(facade.getAvailableStrategies());
        int strategyChoice = consoleInput.getIntInput("Strategy number: ", 1, facade.getStrategiesSize());
        facade.changePrintStrategy(strategyChoice);
    }

    /**
     * Adds a new print task to the queue based on user input.
     */
    public void addNewPrintTask() {
        terminal.show(facade.getAvailablePrints());
        int printChoice = consoleInput.getIntInput("Print number: ", 1, facade.getPrintSize());

        terminal.show(facade.getFilamentTypesOptions());
        int filamentType = consoleInput.getIntInput("Filament type: ", 1, 3);

        terminal.show(facade.getColorsOptions(filamentType));

        facade.createSelectedColorsList();
        for (int i = 0; i < facade.getFilamentColorsNumber(printChoice); i++) {
            int colorChoice = consoleInput.getIntInput(
                    "Color number: ", 1, facade.getColorsSize(filamentType)
            );
            facade.addSelectedColors(filamentType, colorChoice);
        }

        terminal.show(facade.addNewPrintTask(printChoice, filamentType));
    }

    /**
     * Displays information about the available spools.
     */
    private void showSpools() {
        showSection(
                "---------- Spools ----------",
                facade.getSpools(),
                terminal::formatSpoolDTO,
                null
        );
    }

    /**
     * Displays information about pending print tasks.
     */
    private void showPendingPrintTasks() {
        showSection(
                "--------- Pending Print Tasks ---------",
                facade.getPendingPrintTasks(),
                terminal::formatPrintTaskDTO,
                null
        );
    }

    /**
     * Displays information about available printers.
     */
    private void showPrinters() {
        showSection(
                "--------- Available Printers ---------",
                facade.getPrinters(),
                terminal::formatPrinterDTO,
                null
        );
    }

    /**
     * Displays information about available prints.
     */
    private void showPrints() {
        showSection(
                "---------- Available Prints ----------",
                facade.getPrints(),
                terminal::formatPrintDTO,
                "--------------------------------------"
        );
    }

    /**
     * Handles the registration of a completed print task for a printer.
     */
    private void registerPrintCompletion() {
        registerPrinterStatus(true);
    }

    /**
     * Handles the registration of a failed print task for a printer.
     */
    private void registerPrinterFailure() {
        registerPrinterStatus(false);
    }

    /**
     * Displays a specific section of the application output, formatted with the provided function.
     *
     * @param header        the header to display
     * @param iterator      the iterator for items to display
     * @param formatter     the function for formatting items
     * @param itemSeparator the separator between items, or {@code null} if no separator is needed
     * @param <T>           the type of items to display
     */
    private <T> void showSection(
            String header,
            Iterator<T> iterator,
            Function<T, String> formatter,
            String itemSeparator
    ) {
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

    /**
     * Registers the status of a printer, indicating whether its current task was successful or failed.
     *
     * @param isSuccess {@code true} if the task was successful; {@code false} otherwise
     */
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

            int printerId = consoleInput.getIntInput(facade.getRunningPrintersIds());
            terminal.show(facade.registerPrinterStatus(printerId, isSuccess));
        }
    }
}
