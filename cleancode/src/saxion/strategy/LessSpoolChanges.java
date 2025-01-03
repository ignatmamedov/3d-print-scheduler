package saxion.strategy;

import saxion.models.PrintTask;
import saxion.models.Spool;
import saxion.printers.MultiColor;
import saxion.printers.Printer;
import saxion.types.FilamentType;

import java.util.ArrayList;
import java.util.List;

public class LessSpoolChanges implements PrintingStrategy {
    @Override
    public String selectPrintTask(Printer printer, List<PrintTask> pendingPrintTasks, List<Printer> printers, List<Spool> freeSpools) {
        List<Spool> spools = printer.getCurrentSpools();
        PrintTask chosenTask = null;

        if (!spools.isEmpty()) {
            chosenTask = findTaskForCurrentSpools(printer, spools, pendingPrintTasks);
        }

        if (chosenTask == null) {
            chosenTask = findTaskForFreeSpools(printer, pendingPrintTasks, freeSpools);
        }

        if (chosenTask != null) {
            pendingPrintTasks.remove(chosenTask);
            printer.setTask(chosenTask);
            System.out.println("- Started task: " + chosenTask.getPrint().getName() + " " + chosenTask.getFilamentType() + " on printer " + printer.getName());
        }

        return "";
    }

    private PrintTask findTaskForCurrentSpools(Printer printer, List<Spool> spools, List<PrintTask> pendingPrintTasks) {
        for (PrintTask printTask : pendingPrintTasks) {
            if (printer.printFits(printTask.getPrint())) {
                if (taskMatchesPrinterAndSpools(printer, printTask, spools)) {
                    printer.setTask(printTask);
                    return printTask;
                }
            }
        }
        return null;
    }

    private PrintTask findTaskForFreeSpools(Printer printer, List<PrintTask> pendingPrintTasks, List<Spool> freeSpools) {
        for (PrintTask printTask : pendingPrintTasks) {
            if (printer.printFits(printTask.getPrint()) && printer.getTask() == null) {
                if (handleSpoolChange(printer, printTask, freeSpools)) {
                    return printTask;
                }
            }
        }
        return null;
    }

    private boolean taskMatchesPrinterAndSpools(Printer printer, PrintTask printTask, List<Spool> spools) {
        if (printer.isHoused()) {
            return matchesHousedPrinter(printTask, spools);
        } else if (printer instanceof MultiColor) {
            return matchesMultiColorPrinter((MultiColor) printer, printTask, spools);
        } else {
            return matchesStandardFDM(printTask, spools);
        }
    }

    private boolean matchesStandardFDM(PrintTask printTask, List<Spool> spools) {
        return printTask.getFilamentType() != FilamentType.ABS && printTask.getColors().size() == 1
                && spools.get(0).spoolMatch(printTask.getColors().get(0), printTask.getFilamentType());
    }

    private boolean matchesHousedPrinter(PrintTask printTask, List<Spool> spools) {
        return printTask.getColors().size() == 1
                && spools.get(0).spoolMatch(printTask.getColors().get(0), printTask.getFilamentType());
    }

    private boolean matchesMultiColorPrinter(MultiColor printer, PrintTask printTask, List<Spool> spools) {
        if (printTask.getFilamentType() == FilamentType.ABS || printTask.getColors().size() > printer.getMaxColors()) {
            return false;
        }

        for (int i = 0; i < spools.size() && i < printTask.getColors().size(); i++) {
            if (!spools.get(i).spoolMatch(printTask.getColors().get(i), printTask.getFilamentType())) {
                return false;
            }
        }
        return true;
    }

    private boolean handleSpoolChange(Printer printer, PrintTask printTask, List<Spool> freeSpools) {
        if (printer.isHoused()) {
            return changeSpoolForHousedPrinter(printer, printTask, freeSpools);
        } else if (printer instanceof MultiColor) {
            return changeSpoolsForMultiColorPrinter((MultiColor) printer, printTask, freeSpools);
        } else {
            return changeSpoolForStandardFDM(printer, printTask, freeSpools);
        }
    }

    private boolean changeSpoolForStandardFDM(Printer printer, PrintTask printTask, List<Spool> freeSpools) {
        for (Spool spool : freeSpools) {
            if (spool.spoolMatch(printTask.getColors().get(0), printTask.getFilamentType())) {
                replaceSpool(printer, spool, freeSpools);
                return true;
            }
        }
        return false;
    }

    private boolean changeSpoolForHousedPrinter(Printer printer, PrintTask printTask, List<Spool> freeSpools) {
        for (Spool spool : freeSpools) {
            if (spool.spoolMatch(printTask.getColors().get(0), printTask.getFilamentType())) {
                replaceSpool(printer, spool, freeSpools);
                return true;
            }
        }
        return false;
    }

    private boolean changeSpoolsForMultiColorPrinter(MultiColor printer, PrintTask printTask, List<Spool> freeSpools) {
        List<Spool> chosenSpools = new ArrayList<>();
        for (int i = 0; i < printTask.getColors().size(); i++) {
            for (Spool spool : freeSpools) {
                if (spool.spoolMatch(printTask.getColors().get(i), printTask.getFilamentType()) && !containsSpool(chosenSpools, printTask.getColors().get(i))) {
                    chosenSpools.add(spool);
                    break;
                }
            }
        }

        if (chosenSpools.size() == printTask.getColors().size()) {
            replaceSpools(printer, chosenSpools, freeSpools);
            return true;
        }
        return false;
    }

    private void replaceSpool(Printer printer, Spool newSpool, List<Spool> freeSpools) {
        List<Spool> currentSpools = printer.getCurrentSpools();
        if (!currentSpools.isEmpty()) {
            freeSpools.add(currentSpools.get(0));
        }
        freeSpools.remove(newSpool);
        printer.setCurrentSpools(List.of(newSpool));

        System.out.println("- Spool change: Please place spool " + newSpool.getId() + " in printer " + printer.getName());
    }

    private void replaceSpools(Printer printer, List<Spool> newSpools, List<Spool> freeSpools) {
        freeSpools.addAll(printer.getCurrentSpools());
        freeSpools.removeAll(newSpools);
        printer.setCurrentSpools(newSpools);
        int position = 1;
        for (Spool spool : newSpools) {
            System.out.println("- Spool change: Please place spool " + spool.getId() + " in printer " + printer.getName() + " position " + position);
            position++;
        }
    }

    private boolean containsSpool(final List<Spool> list, final String name) {
        return list.stream().anyMatch(o -> o.getColor().equals(name));
    }
}