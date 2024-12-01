//package nl.saxion;
//
//import nl.saxion.Models.FilamentType;
//import nl.saxion.Models.Print;
//import nl.saxion.Models.Spool;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class TestChanges {

    // TODO: Change current addNewPrintTask() to something like this?
//    private void addNewPrintTask() {
//        List<String> colors = new ArrayList<>();
//        Print print = selectPrint();
//        if (print == null) return;
//
//        FilamentType type = selectFilamentType();
//        if (type == null) return;
//
//        colors = selectColors(print, type);
//        if (colors.isEmpty()) return;
//
//        manager.addPrintTask(print.getName(), colors, type);
//        System.out.println("----------------------------");
//    }
//
//    private Print selectPrint() {
//        var prints = manager.getPrints();
//        System.out.println("---------- New Print Task ----------");
//        System.out.println("---------- Available prints ----------");
//        int counter = 1;
//        for (Print p : prints) {
//            System.out.println("- " + counter + ": " + p.getName());
//            counter++;
//        }
//
//        System.out.print("- Print number: ");
//        int printNumber = numberInput(1, prints.size());
//        System.out.println("--------------------------------------");
//        return manager.findPrint(printNumber - 1);
//    }
//
//    private FilamentType selectFilamentType() {
//        System.out.println("---------- Filament Type ----------");
//        System.out.println("- 1: PLA");
//        System.out.println("- 2: PETG");
//        System.out.println("- 3: ABS");
//        System.out.print("- Filament type number: ");
//        int ftype = numberInput(1, 3);
//        System.out.println("--------------------------------------");
//
//        switch (ftype) {
//            case 1:
//                return FilamentType.PLA;
//            case 2:
//                return FilamentType.PETG;
//            case 3:
//                return FilamentType.ABS;
//            default:
//                System.out.println("- Not a valid filamentType, bailing out");
//                return null;
//        }
//    }
//
//    private List<String> selectColors(Print print, FilamentType type) {
//        List<String> colors = new ArrayList<>();
//        var spools = manager.getSpools();
//        System.out.println("---------- Colors ----------");
//        ArrayList<String> availableColors = new ArrayList<>();
//        int counter = 1;
//        for (Spool spool : spools) {
//            String colorString = spool.getColor();
//            if (type == spool.getFilamentType() && !availableColors.contains(colorString)) {
//                System.out.println("- " + counter + ": " + colorString + " (" + spool.getFilamentType() + ")");
//                availableColors.add(colorString);
//                counter++;
//            }
//        }
//
//        for (int i = 0; i < print.getFilamentLength().size(); i++) {
//            System.out.print("- Color number: ");
//            int colorChoice = numberInput(1, availableColors.size());
//            colors.add(availableColors.get(colorChoice - 1));
//        }
//        System.out.println("--------------------------------------");
//        return colors;
//    }
//}
