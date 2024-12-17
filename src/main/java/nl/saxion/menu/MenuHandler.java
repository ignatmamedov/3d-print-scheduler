package nl.saxion.menu;

import nl.saxion.input.UserInput;

import java.util.List;

public class MenuHandler {
    private final UserInput userInput;

    private static final List<String> DEFAULT_MENU_OPTIONS = List.of(
            "1) Add new Print Task",
            "2) Register Printer Completion",
            "3) Register Printer Failure",
            "4) Change printing style",
            "5) Start Print Queue",
            "6) Show prints",
            "7) Show printers",
            "8) Show spools",
            "9) Show pending print tasks",
            "0) Exit"
    );

    public MenuHandler(UserInput userInput) {
        this.userInput = userInput;
    }

    public void displayMenu() {
        System.out.println("------------- Menu ----------------");
        for (String option : DEFAULT_MENU_OPTIONS) {
            System.out.println("- " + option);
        }
        System.out.println("-----------------------------------");
    }

    public String displayOptions(List<String> menuOptions, String title) {
        StringBuilder menu = new StringBuilder();
        menu.append("-------------");
        menu.append(title);
        menu.append("----------------\n");
        for (String option : menuOptions) {
            menu.append("- ").append(option).append("\n");
        }
        menu.append("-----------------------------------");
        return menu.toString();
    }

    public String displayOptions(List<String> menuOptions) {
        StringBuilder menu = new StringBuilder();
        menu.append("-----------------------------\n");
        for (String option : menuOptions) {
            menu.append("- ").append(option).append("\n");
        }
        menu.append("-----------------------------------");
        return menu.toString();
    }

    public int getMenuChoice() {
        return userInput.getIntInput(0, 9);
    }
}
