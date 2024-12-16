package nl.saxion.handlers;

import nl.saxion.Models.PrintTask;
import nl.saxion.Models.Printer;

import java.util.List;
import java.util.Map;

public class PrintTaskHandler {
    private List<PrintTask> pendingPrintTasks;
    private Map<Printer, PrintTask> runningPrintTasks;
}
