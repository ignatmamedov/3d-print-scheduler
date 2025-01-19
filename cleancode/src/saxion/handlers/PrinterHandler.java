package saxion.handlers;

import saxion.dataprovider.DataProvider;
import saxion.printers.Printer;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handler class for managing a collection of printers.
 * Provides methods to retrieve and manage printer objects.
 */
public class PrinterHandler {

    /** The list of printers managed by this handler. */
    private List<Printer> printers = new ArrayList<>();

    /**
     * Gets the list of all printers managed by this handler.
     *
     * @return a {@link List} of {@link Printer} objects
     */
    public List<Printer> getPrinters() {
        return printers;
    }

    /**
     * Retrieves a printer by its unique ID.
     *
     * @param printerId the ID of the printer to retrieve
     * @return the {@link Printer} with the specified ID
     * @throws IllegalStateException if no printer with the given ID is found
     */
    public Printer getPrinterById(int printerId) {
        return printers.stream()
                .filter(printer -> printer.getId() == printerId)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Cannot find a printer with ID " + printerId
                ));
    }

    /**
     * Retrieves a running printer by its unique ID.
     * A running printer is defined as one that currently has an assigned task.
     *
     * @param printerId the ID of the printer to retrieve
     * @return the {@link Printer} with the specified ID and a running task
     * @throws IllegalStateException if no such printer is found
     */
    public Printer getRunningPrinterById(int printerId) {
        return printers.stream()
                .filter(printer -> printer.getId() == printerId && printer.getTask() != null)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Cannot find a running task on printer with ID " + printerId
                ));
    }

    /**
     * Sets the list of printers managed by this handler.
     * If the list is already populated, it will not be overwritten.
     *
     * @param printers a {@link List} of {@link Printer} objects to set
     */
    public void setPrinters(List<Printer> printers) {
        if (this.printers.isEmpty()) {
            this.printers = printers;
        }
    }
}
