package saxion.menu;

import java.util.List;

/**
 * The `MenuPrinter` class provides methods to generate and format menus and option lists
 * for display in the terminal.
 */
public class MenuPrinter {

    /** Default menu options for the application. */
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
            "10) Show Dashboard Stats",
            "0) Exit"
    );

    /**
     * Generates a formatted string representing the default menu.
     *
     * @return a string containing the default menu
     */
    public String displayMenu() {
        StringBuilder menu = new StringBuilder();
        menu.append("------------- Menu ----------------\n");
        for (String option : DEFAULT_MENU_OPTIONS) {
            menu.append("- ").append(option).append("\n");
        }
        menu.append("-----------------------------------");
        return menu.toString();
    }

    /**
     * Generates a formatted string representing a menu with the specified options and title.
     *
     * @param menuOptions the list of menu options to display
     * @param title       the title of the menu
     * @return a string containing the formatted menu
     */
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

    /**
     * Generates a formatted string representing a menu with the specified options, title,
     * and an option to display indices for each menu item.
     *
     * @param menuOptions      the list of menu options to display
     * @param title            the title of the menu
     * @param showOptionIndex  {@code true} to display indices for each option; {@code false} otherwise
     * @return a string containing the formatted menu
     */
    public String displayOptions(List<String> menuOptions, String title, boolean showOptionIndex) {
        StringBuilder menu = new StringBuilder();
        menu.append("-------------");
        menu.append(title);
        menu.append("----------------\n");

        if (showOptionIndex) {
            for (int i = 0; i < menuOptions.size(); i++) {
                menu.append(i + 1).append(") ").append(menuOptions.get(i)).append("\n");
            }
        } else {
            for (String option : menuOptions) {
                menu.append("- ").append(option).append("\n");
            }
        }

        menu.append("-----------------------------------");
        return menu.toString();
    }

    /**
     * Generates a formatted string representing a menu with the specified options.
     *
     * @param menuOptions the list of menu options to display
     * @return a string containing the formatted menu
     */
    public String displayOptions(List<String> menuOptions) {
        StringBuilder menu = new StringBuilder();
        menu.append("-----------------------------\n");
        for (String option : menuOptions) {
            menu.append("- ").append(option).append("\n");
        }
        menu.append("-----------------------------------");
        return menu.toString();
    }
}
