package saxion.strategy;

import saxion.models.PrintTask;
import saxion.models.Spool;
import saxion.printers.MultiColor;
import saxion.printers.Printer;
import saxion.printers.StandardFDM;
import saxion.types.FilamentType;

import java.util.ArrayList;
import java.util.List;

public class LessSpoolChanges implements PrintingStrategy {
    @Override
    public String selectPrintTask(Printer printer, List<PrintTask> pendingPrintTasks, List<Printer> printers, List<Spool> freeSpools) {
        StringBuilder result = new StringBuilder();

        PrintTask chosenTask = findTaskWithCurrentSpools(printer, pendingPrintTasks);

        if (chosenTask != null) {
            printer.setTask(chosenTask);
            pendingPrintTasks.remove(chosenTask);
            result.append("- Started task: ").append(chosenTask).append(" on printer ").append(printer.getName()).append("\n");
        } else {
            chosenTask = findTaskWithSpoolChanges(printer, pendingPrintTasks, freeSpools, result);
            if (chosenTask != null) {
                printer.setTask(chosenTask);
                pendingPrintTasks.remove(chosenTask);
                result.append("- Started task: ").append(chosenTask).append(" on printer ").append(printer.getName()).append("\n");
            }
        }

        return result.toString();
    }

    private PrintTask findTaskWithCurrentSpools(Printer printer, List<PrintTask> pendingPrintTasks) {
        List<Spool> spools = printer.getCurrentSpools();
        if (spools == null || spools.isEmpty()) {
            return null;
        }

        for (PrintTask printTask : pendingPrintTasks) {
            if (isTaskCompatible(printer, spools, printTask)) {
                return printTask;
            }
        }

        return null;
    }

    private PrintTask findTaskWithSpoolChanges(Printer printer, List<PrintTask> pendingPrintTasks, List<Spool> freeSpools, StringBuilder result) {
        for (PrintTask printTask : pendingPrintTasks) {
            if (printer.printFits(printTask.getPrint()) && printer.getTask() == null) {
                List<Spool> matchingSpools = findMatchingSpools(freeSpools, printTask);
                if (!matchingSpools.isEmpty() && matchingSpools.size() == printTask.getColors().size()) {
                    if (applySpoolChanges(printer, matchingSpools, freeSpools, result)) {
                        return printTask;
                    }
                }
            }
        }
        return null;
    }

    private boolean isTaskCompatible(Printer printer, List<Spool> spools, PrintTask printTask) {
        if (!printer.printFits(printTask.getPrint())) {
            return false;
        }

        if (printer.isHoused() && printTask.getColors().size() == 1) {
            return spools.getFirst().spoolMatch(printTask.getColors().getFirst(), printTask.getFilamentType());
        }

        if (!printer.isHoused() && printTask.getFilamentType() != FilamentType.ABS && printTask.getColors().size() == 1) {
            return spools.getFirst().spoolMatch(printTask.getColors().getFirst(), printTask.getFilamentType());
        }

        if (printer instanceof MultiColor multiColorPrinter && printTask.getFilamentType() != FilamentType.ABS
                && printTask.getColors().size() <= multiColorPrinter.getMaxColors()) {
            for (int i = 0; i < spools.size() && i < printTask.getColors().size(); i++) {
                if (!spools.get(i).spoolMatch(printTask.getColors().get(i), printTask.getFilamentType())) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    private List<Spool> findMatchingSpools(List<Spool> freeSpools, PrintTask printTask) {
        List<Spool> matchingSpools = new ArrayList<>();
        for (String color : printTask.getColors()) {
            Spool matchedSpool = freeSpools.stream()
                    .filter(spool -> spool.spoolMatch(color, printTask.getFilamentType()) && !matchingSpools.contains(spool))
                    .findFirst()
                    .orElse(null);

            if (matchedSpool != null) {
                matchingSpools.add(matchedSpool);
            }
        }
        return matchingSpools;
    }

    private boolean applySpoolChanges(Printer printer, List<Spool> newSpools, List<Spool> freeSpools, StringBuilder result) {
        if (printer instanceof StandardFDM) {
            if (newSpools.size() != 1) {
                return false;
            }
            freeSpools.addAll(printer.getCurrentSpools());
            printer.setCurrentSpools(newSpools);
            Spool spool = newSpools.getFirst();
            result.append("- Spool change: Please place spool ").append(spool.getId())
                    .append(" in printer ").append(printer.getName()).append("\n");
            freeSpools.remove(spool);
            return true;
        } else if (printer instanceof MultiColor || printer.isHoused()) {
            freeSpools.addAll(printer.getCurrentSpools());
            printer.setCurrentSpools(newSpools);
            int position = 1;
            for (Spool spool : newSpools) {
                result.append("- Spool change: Please place spool ").append(spool.getId())
                        .append(" in printer ").append(printer.getName()).append(" position ").append(position).append("\n");
                freeSpools.remove(spool);
                position++;
            }
            return true;
        }
        return false;
    }
}


