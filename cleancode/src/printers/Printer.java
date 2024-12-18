package printers;

import nl.saxion.Models.Print;

public abstract class Printer {
    private final int id;
    private final String name;
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

    @Override
    public String toString() {
        return  "--------" + System.lineSeparator() +
                "- ID: " + id + System.lineSeparator() +
                "- Name: " + name + System.lineSeparator() +
                "- Manufacturer: " + manufacturer + System.lineSeparator() +
                "--------";
    }

}