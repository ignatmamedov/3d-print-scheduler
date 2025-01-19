package saxion.handlers;

import saxion.models.Print;
import saxion.models.PrintTask;
import saxion.models.Spool;
import saxion.printers.Printer;
import saxion.strategy.LessSpoolChanges;
import saxion.strategy.PrintingStrategy;
import saxion.types.FilamentType;

import java.util.ArrayList;
import java.util.List;

/**
 * Handler class for managing print tasks and coordinating printers with tasks.
 * Supports dynamic strategies for task selection and execution.
 */
public class PrintTaskHandler {

    /** The list of pending print tasks waiting for execution. */
    private List<PrintTask> pendingPrintTasks;

    /** The list of printers available for executing print tasks. */
    private List<Printer> printers;

    /** The strategy used for selecting and assigning print tasks. */
    private PrintingStrategy printingStrategy;

    /**
     * Constructs a new {@code PrintTaskHandler} with the specified printing strategy.
     *
     * @param printingStrategy the {@link PrintingStrategy} to use for task selection
     */
    public PrintTaskHandler(PrintingStrategy printingStrategy) {
        this.pendingPrintTasks = new ArrayList<>();
        this.printingStrategy = printingStrategy;
    }

    /**
     * Sets the list of printers available for executing print tasks.
     *
     * @param printers a {@link List} of {@link Printer} objects
     */
    public void setPrinters(List<Printer> printers) {
        this.printers = printers;
    }

    /**
     * Updates the strategy used for selecting and assigning print tasks.
     *
     * @param printingStrategy the new {@link PrintingStrategy} to use
     */
    public void setPrintingStrategy(PrintingStrategy printingStrategy) {
        this.printingStrategy = printingStrategy;
    }

    /**
     * Adds a new print task to the queue based on the specified print, colors, and filament type.
     *
     * @param print        the {@link Print} object representing the task
     * @param colors       a {@link List} of colors required for the task
     * @param filamentType the type of filament required for the task
     * @return a message indicating the task was successfully added to the queue
     */
    public String addNewPrintTask(Print print, List<String> colors, FilamentType filamentType) {
        PrintTask printTask = new PrintTask(print, colors, filamentType);
        pendingPrintTasks.add(printTask);
        return "Print task added to the queue";
    }

    /**
     * Adds an existing print task to the queue.
     *
     * @param printTask the {@link PrintTask} to add to the queue
     */
    public void addNewPrintTask(PrintTask printTask) {
        pendingPrintTasks.add(printTask);
    }

    /**
     * Selects a print task for the specified printer using the current printing strategy.
     *
     * @param printer   the {@link Printer} to assign the task to
     * @param freeSpools a {@link List} of available {@link Spool} objects for the task
     * @return a message indicating the result of the task selection process
     */
    public String selectPrintTask(Printer printer, List<Spool> freeSpools) {
        return printingStrategy.selectPrintTask(printer, pendingPrintTasks, printers, freeSpools);
    }

    /**
     * Gets the number of printers currently executing tasks.
     *
     * @return the number of running print tasks
     */
    public int getRunningPrintTasksSize() {
        return printers.stream().filter(printer -> printer.getTask() != null).toArray().length;
    }

    /**
     * Gets the list of pending print tasks waiting for execution.
     *
     * @return a {@link List} of {@link PrintTask} objects
     */
    public List<PrintTask> getPendingPrintTasks() {
        return pendingPrintTasks;
    }
}
