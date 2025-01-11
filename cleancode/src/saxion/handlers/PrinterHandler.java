package saxion.handlers;

import saxion.dataprovider.DataProvider;
import saxion.printers.Printer;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class PrinterHandler {

    private List<Printer> printers = new ArrayList<>();

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

    public void setPrinters(List<Printer> printers){
        if (this.printers.isEmpty()){
            this.printers = printers;
        }
    }

}
