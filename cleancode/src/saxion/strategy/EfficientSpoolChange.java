package saxion.strategy;

import saxion.models.PrintTask;
import saxion.models.Spool;
import saxion.printers.Printer;
import saxion.types.FilamentType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class EfficientSpoolChange extends BasePrintingStrategy implements PrintingStrategy {
    @Override
    public String selectPrintTask(Printer printer, List<PrintTask> pendingPrintTasks, List<Printer> printers, List<Spool> freeSpools) {
        List<String> messages = new ArrayList<>();

        for (
                Iterator<PrintTask> iterator = pendingPrintTasks.iterator(); iterator.hasNext(); ) {
            PrintTask printTask = iterator.next();
            if (!printer.printFits(printTask.getPrint()) && printer.getTask() != null) {
                continue;
            }

            Spool selectedSpool = selectSmallestPossibleSpool(freeSpools, printTask, printer);
            if (selectedSpool == null && !printer.getCurrentSpools().isEmpty()) {
                selectedSpool = selectSmallestPossibleSpool(printer.getCurrentSpools(), printTask, printer);
            }

            if (selectedSpool != null) {
                handleSpoolChange(printer, printTask, new ArrayList<>(List.of(selectedSpool)), messages);

                printer.setTask(printTask);
                iterator.remove();

                if (messages.isEmpty()) {
                    return "- Started task: " + printTask.getPrint().getName() + " on printer " + printer.getName();
                } else {
                    return String.join("\n", messages) + "\n- Started task: " + printTask.getPrint().getName();
                }
            }
        }

        return null;
    }

    /**
     * Select the smallest possible spool that has enough filament to print the task.
     *
     * @param freeSpools the list of available spools
     * @param printTask  the print task to be completed
     * @return the selected spool or null if no suitable spool is found
     */
    private Spool selectSmallestPossibleSpool(List<Spool> freeSpools, PrintTask printTask, Printer printer) {
        if(!matchesCurrentPrinter(printer, printTask)){
            return null;
        }

        return freeSpools.stream()
                .filter(spool -> {
                    boolean matches = spool.getLength() >= printTask.getPrint().getLength() &&
                            spool.getFilamentType() == printTask.getFilamentType() &&
                            spool.spoolMatch(printTask.getColors().get(0), printTask.getFilamentType());
                    return matches;
                })
                .min(Comparator.comparingDouble(Spool::getLength))
                .orElse(null);
    }
}
