package saxion.printers;

import saxion.models.Print;
import saxion.facade.PrinterDTO;

public abstract class Printer {
    private final int id;
    private final String name;

    public String getManufacturer() {
        return manufacturer;
    }

    public boolean isHoused() {
        return isHoused;
    }

    private final String manufacturer;
    private final boolean isHoused;

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
                null, null, null, null, null
        );
    }

}