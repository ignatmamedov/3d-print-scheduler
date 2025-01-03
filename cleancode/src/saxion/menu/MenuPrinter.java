package saxion.menu;

import java.util.List;

//TODO: Change String to DTO(records)
public class MenuPrinter {

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

    public String displayMenu() {
        StringBuilder menu = new StringBuilder();
        menu.append("------------- Menu ----------------\n");
        for (String option : DEFAULT_MENU_OPTIONS) {
            menu.append("- ").append(option).append("\n");
        }
        menu.append("-----------------------------------");
        return menu.toString();
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
