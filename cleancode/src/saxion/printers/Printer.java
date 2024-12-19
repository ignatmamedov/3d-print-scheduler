package saxion.printers;

import saxion.models.Spool;
import saxion.models.Print;
import saxion.facade.PrinterDTO;
import saxion.models.PrintTask;

import java.util.List;

public abstract class Printer {
    private final int id;
    private final String name;
    private PrintTask task;
    private final String manufacturer;
    private final boolean isHoused;

    public PrintTask getTask() {
        return task;
    }

    public void setTask(PrintTask task) {
        this.task = task;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public boolean isHoused() {
        return isHoused;
    }

    public abstract List<Spool> getCurrentSpools();

    public abstract void setCurrentSpools(List<Spool> spools);

    public Printer(int id, String printerName, String manufacturer, boolean isHoused) {
        this.id = id;
        this.name = printerName;
        this.manufacturer = manufacturer;
        this.isHoused = isHoused;
    }

    public Printer(int id, String printerName, String manufacturer) {
        this.id = id;
        this.name = printerName;
        this.manufacturer = manufacturer;
        this.isHoused = false;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public abstract int calculatePrintTime(String filename);

    public abstract boolean printFits(Print print);

    public PrinterDTO toDTO() {
        return new PrinterDTO(
                id,
                name,
                manufacturer,
                isHoused,
                null, null, null, null, null,
                getTask() != null ? getTask().toDTO() : null
        );
    }

}