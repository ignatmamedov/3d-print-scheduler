package saxion.handlers;

import saxion.models.Print;
import saxion.models.PrintTask;
import saxion.printers.MultiColor;
import saxion.printers.Printer;
import saxion.strategy.LessSpoolChanges;
import saxion.strategy.PrintingStrategy;
import saxion.types.FilamentType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrintTaskHandler {
    private List<PrintTask> pendingPrintTasks;
    private List<Printer> printers;
    private PrintingStrategy printingStrategy;

    public PrintTaskHandler() {
        this.pendingPrintTasks = new ArrayList<>();
        this.printingStrategy = new LessSpoolChanges();
        // set default strategy
    }

    public void setPrinters(List<Printer> printers) {
        this.printers = printers;
    }

    public void setPrintingStrategy(PrintingStrategy printingStrategy) {
        this.printingStrategy = printingStrategy;
    }

    public String addNewPrintTask(Print print, List<String> colors, FilamentType filamentType) {
        PrintTask printTask = new PrintTask(print, colors, filamentType);
        pendingPrintTasks.add(printTask);

        return "Print task added to the queue";
    }

    public void registerPrintCompletion() {
        // Register a print completion
    }

    public void startPrintQueue() {
        // Start the print queue
    }

    public void showPendingPrintTasks() {
        // Show the pending print tasks
    }

    public void selectPrintTask() {
        // Select a print task (Strategy pattern)
        //printingStrategy.selectPrintTask();
    }

    public int getRunningPrintTasksSize() {
        return printers.stream().filter(printer -> printer.getTask() != null).toArray().length;
    }

    public List<PrintTask> getPendingPrintTasks() {
        return pendingPrintTasks;
    }

}
