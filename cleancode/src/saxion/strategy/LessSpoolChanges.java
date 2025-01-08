package saxion.strategy;

import saxion.models.PrintTask;
import saxion.models.Spool;
import saxion.printers.MultiColor;
import saxion.printers.Printer;
import java.util.ArrayList;
import java.util.List;

public class LessSpoolChanges extends BasePrintingStrategy implements PrintingStrategy {
    @Override
    public String selectPrintTask(Printer printer, List<PrintTask> pendingPrintTasks, List<Printer> printers, List<Spool> freeSpools) {
        List<String> messages = new ArrayList<>();

        List<Spool> spools = printer.getCurrentSpools();
        PrintTask chosenTask = null;

        if (!spools.isEmpty()) {
            chosenTask = findTaskForCurrentSpools(printer, spools, pendingPrintTasks);
        }

        if (chosenTask == null) {
            chosenTask = findTaskForFreeSpools(printer, pendingPrintTasks, freeSpools, messages);
        }

        if (chosenTask != null) {
            pendingPrintTasks.remove(chosenTask);
            printer.setTask(chosenTask);
            messages.add("- Started task: " + chosenTask.getPrint().getName() + " "
                    + chosenTask.getFilamentType() + " on printer " + printer.getName());
        }

        return String.join("\n", messages);
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

    private PrintTask findTaskForFreeSpools(
            Printer printer, List<PrintTask> pendingPrintTasks, List<Spool> freeSpools, List<String> messages
    ) {
        for (PrintTask printTask : pendingPrintTasks) {
            if (printer.printFits(printTask.getPrint()) && printer.getTask() == null) {
                if (handleSpoolChange(printer, printTask, freeSpools, messages)) {
                    return printTask;
                }
            }
        }
        return null;
    }

    private boolean taskMatchesPrinterAndSpools(Printer printer, PrintTask printTask, List<Spool> spools) {
        if (printer.isHoused()) {
            return matchesHousedPrinter(printTask) && matchesSpoolsForHousedPrinter(printTask, spools);
        } else if (printer instanceof MultiColor) {
            return matchesMultiColorPrinter((MultiColor) printer, printTask) && matchesSpoolsForMultiColorPrinter(printTask, spools);
        } else {
            return matchesStandardFDM(printTask) && matchesSpoolsForStandardFDM(printTask, spools);
        }
    }
}
