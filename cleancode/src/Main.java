import input.ConsoleInput;
import input.UserInput;

public class Main {
    private final UserInput consoleInput = new ConsoleInput();
    private final Facade facade = new Facade();

    public static void main(String[] args) {
        new Main().run(args);
    }

    public void run(String[] args) {
        readData(args);

        int choice = 1;
        while (choice > 0 && choice < 10) {
            // display menu should return string
            System.out.println(facade.displayMenu());
            choice = consoleInput.getIntInput(0, 9);
            switch (choice) {
                case 0 -> {
                }
                case 1 -> addNewPrintTask();
                case 2 -> facade.registerPrintCompletion();
                case 3 -> facade.registerPrinterFailure();
                case 4 -> facade.changePrintStrategy();
                case 5 -> facade.startPrintQueue();
                case 6 -> facade.showPrints();
                case 7 -> facade.showPrinters();
                case 8 -> facade.showSpools();
                case 9 -> facade.showPendingPrintTasks();
            }
        }
    }

    public void addNewPrintTask() {
        System.out.println(facade.getPrints());
        int printChoice = consoleInput.getIntInput("Print number: ", 1, facade.getPrintSize());

        System.out.println(facade.getFilamentTypesOptions());
        int filamentType = consoleInput.getIntInput("Filament type: ", 1, 3);

        System.out.println(facade.getColorsOptions(filamentType));

        facade.prepareSelectedColorsList();
        for (int i = 0; i < facade.getFilamentColorsNumber(printChoice); i++) {
            int colorChoice = consoleInput.getIntInput("Color number: ", 1, facade.getColorsSize());
            facade.addSelectedColors(colorChoice);
        }

        facade.addNewPrintTask(printChoice, filamentType);
    }

    public void readData(String[] args) {
        String printsFile = args.length > 0 ? args[0] : "";
        String spoolsFile = args.length > 1 ? args[1] : "";
        String printersFile = args.length > 2 ? args[2] : "";

        try {
            facade.readPrintsFromFile(printsFile, true);
            facade.readSpoolsFromFile(spoolsFile, true);
            facade.readPrintersFromFile(printersFile, true);
        } catch (Exception e) {
            System.err.println("Failed to read files");
            e.printStackTrace();
        }
    }
}
