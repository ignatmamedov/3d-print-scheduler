package saxion.strategy;

import saxion.models.PrintTask;
import saxion.models.Spool;
import saxion.printers.Printer;

import java.util.List;

public class EfficientSpoolChange implements PrintingStrategy{
    @Override
    public void selectPrintTask(Printer printer, List<PrintTask> pendingPrintTasks, List<Printer> printers, List<Spool> freeSpools) {

    }
}
