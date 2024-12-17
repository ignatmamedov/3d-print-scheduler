package nl.saxion.handlers;

import nl.saxion.Models.PrintTask;
import nl.saxion.Models.Printer;

import java.util.List;
import java.util.Map;

public class PrintTaskHandler {
    private List<PrintTask> pendingPrintTasks;
    private Map<Printer, PrintTask> runningPrintTasks;

    public void addNewPrintTask() {
        // Add a new print task to the pending print tasks
    }

    public void registerPrintCompletion() {
        // Register a print completion
    }

    public void startPrintQueue() {
        // Start the print queue
    }

    public void showPrints() {
        // Show the prints
    }

    public void showPendingPrintTasks() {
        // Show the pending print tasks
    }

    public void selectPrintTask() {
        // Select a print task (Strategy pattern)
    }
}
