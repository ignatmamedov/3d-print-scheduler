package saxion.strategy;

import saxion.models.PrintTask;
import saxion.models.Spool;
import saxion.printers.MultiColor;
import saxion.printers.Printer;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements a printing strategy that minimizes spool changes during print task execution.
 */
public class LessSpoolChanges extends BasePrintingStrategy implements PrintingStrategy {

    /**
     * Selects a print task for the specified printer with a focus on minimizing spool changes.
     *
     * @param printer          the {@link Printer} for which a print task is being selected
     * @param pendingPrintTasks the list of pending {@link PrintTask}s
     * @param printers         the list of all available printers
     * @param freeSpools       the list of free {@link Spool}s available for use
     * @return a string containing the selected print task details or an empty string if no task could be selected
     */
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

    /**
     * Finds a print task that matches the current spools loaded in the printer.
     *
     * @param printer          the {@link Printer} for which a print task is being searched
     * @param spools           the list of {@link Spool}s currently loaded in the printer
     * @param pendingPrintTasks the list of pending {@link PrintTask}s
     * @return the matching {@link PrintTask}, or {@code null} if no suitable task is found
     */
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

    /**
     * Finds a print task that matches the free spools available for the printer.
     *
     * @param printer          the {@link Printer} for which a print task is being searched
     * @param pendingPrintTasks the list of pending {@link PrintTask}s
     * @param freeSpools       the list of free {@link Spool}s available for use
     * @param messages         the list of messages to append spool change instructions
     * @return the matching {@link PrintTask}, or {@code null} if no suitable task is found
     */
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

    /**
     * Checks if a print task matches the printer and its loaded spools.
     *
     * @param printer   the {@link Printer} for which the task is being checked
     * @param printTask the {@link PrintTask} being checked
     * @param spools    the list of {@link Spool}s currently loaded in the printer
     * @return {@code true} if the task matches the printer and spools; {@code false} otherwise
     */
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
