package saxion.strategy;

import saxion.models.PrintTask;
import saxion.models.Spool;
import saxion.printers.Printer;

import java.util.List;

/**
 * Interface defining the strategy for selecting print tasks for printers.
 * Implementations of this interface determine how to assign pending print tasks to available printers.
 */
public interface PrintingStrategy {

    /**
     * Selects a print task for the specified printer based on the strategy's logic.
     *
     * @param printer          the {@link Printer} for which a print task is being selected
     * @param pendingPrintTasks the list of pending {@link PrintTask}s
     * @param printers         the list of all available printers
     * @param freeSpools       the list of free {@link Spool}s available for use
     * @return a string describing the selected print task and its assignment, or {@code null} if no task was selected
     */
    String selectPrintTask(Printer printer, List<PrintTask> pendingPrintTasks, List<Printer> printers, List<Spool> freeSpools);
}
