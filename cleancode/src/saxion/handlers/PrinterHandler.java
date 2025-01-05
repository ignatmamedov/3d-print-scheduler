package saxion.handlers;

import saxion.dataprovider.DataProvider;
import saxion.printers.Printer;

import java.io.FileNotFoundException;
import java.util.List;

public class PrinterHandler {

    private List<Printer> printers;

    public List<Printer> getPrinters() {
        return printers;
    }

    public Printer getPrinterById(int printerId) {
        return printers.stream()
                .filter(printer -> printer.getId() == printerId)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Cannot find a printer with ID " + printerId
                ));
    }

    public Printer getRunningPrinterById(int printerId) {
        return printers.stream()
                .filter(printer -> printer.getId() == printerId && printer.getTask() != null)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Cannot find a running task on printer with ID " + printerId
                ));
    }

    public void readPrinters(String filename, boolean header) throws FileNotFoundException {
        DataProvider dataProvider = new DataProvider();
        if (filename.isEmpty()) {
            filename = dataProvider.DEFAULT_PRINTERS_FILE;
        }
        printers = dataProvider.readFromFile(filename, Printer.class, header);
        //TODO: Fix for printTaskHandler
        //printTaskHandler.setPrinters(printers);
    }

}
