package nl.saxion.new_test_classes;

import nl.saxion.Facade;
import nl.saxion.input.ConsoleInput;
import nl.saxion.menu.MenuHandler;

public class MainRefactored {
    private final MenuHandler menuHandler = new MenuHandler(new ConsoleInput());
    private final Facade facade = new Facade(menuHandler);

    public static void main(String[] args) {
        new nl.saxion.Main().run(args);
    }

    public void run(String[] args) {
        int choice = 1;
        while (choice > 0 && choice < 10) {
            // display menu should return string
            menuHandler.displayMenu();
            choice = menuHandler.getMenuChoice();
            switch (choice) {
                case 0:
                    break;
                case 1:
                    facade.addNewPrintTask();
                    break;
                case 2:
                    facade.registerPrintCompletion();
                    break;
                case 3:
                    facade.registerPrinterFailure();
                    break;
                case 4:
                    facade.changePrintStrategy();
                    break;
                case 5:
                    facade.startPrintQueue();
                    break;
                case 6:
                    facade.showPrints();
                    break;
                case 7:
                    facade.showPrinters();
                    break;
                case 8:
                    facade.showSpools();
                    break;
                case 9:
                    facade.showPendingPrintTasks();
                    break;
            }
        }
    }
}
