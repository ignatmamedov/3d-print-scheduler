package nl.saxion;

import nl.saxion.Models.Print;
import nl.saxion.input.ConsoleInput;
import nl.saxion.input.UserInput;
import nl.saxion.menu.MenuHandler;
import nl.saxion.new_test_classes.PrintManagerRefactored;

import java.util.ArrayList;
import java.util.List;

public class Facade {
    private final PrintManagerRefactored printManager;
    private final UserInput consoleInput = new ConsoleInput();
    private final MenuHandler menuHandler;

    public Facade(MenuHandler menuHandler) {
        this.printManager = new PrintManagerRefactored();
        this.menuHandler = menuHandler;
    }

    public void addNewPrintTask() {
        // choose prints, filaments and colors step by step
        List<String> prints = printManager.getAvailablePrints().stream().map(printTask -> printTask.getName()).toList();
        System.out.println(menuHandler.displayOptions(prints, "Prints "));
        int printChoice = consoleInput.getIntInput(1, prints.size());

        System.out.println(menuHandler.displayOptions(List.of("PLA", "PETG", "ABS"), "Filament Types "));
        int filamentType = consoleInput.getIntInput(1, 3);

        List<String> colors = printManager.getAvailableColors(printManager.getFilamentType(filamentType), 1);
        System.out.println(menuHandler.displayOptions(colors, "Colors"));

        List<String> selectedColors = new ArrayList<>();
        for (int i = 1; i < printManager.getAvailablePrints().get(printChoice - 1).getFilamentLength().size(); i++) {
            System.out.println(menuHandler.displayOptions(colors, "Colors"));
            int colorChoice = consoleInput.getIntInput(1, colors.size());
            selectedColors.add(colors.get(colorChoice - 1));
        }

        printManager.addNewPrintTask(prints.get(printChoice - 1), filamentType, colors);
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

    public void showPrints() {
        //printManager.showPrints();
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
}
