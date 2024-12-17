package models;

import nl.saxion.Models.Print;
import nl.saxion.Models.Spool;

import java.util.ArrayList;
import java.util.List;

public class MultiColor extends Printer implements SpoolManager {
    private final int maxColors;
    private List<Spool> currentSpools;

    public MultiColor(int id, String printerName, String manufacturer, boolean isHoused, int maxColors, List<Spool> currentSpools) {
        super(id, printerName, manufacturer, isHoused);
        this.maxColors = maxColors;
        this.currentSpools = currentSpools;
    }

    public MultiColor(int id, String printerName, String manufacturer, int maxColors, List<Spool> currentSpools) {
        super(id, printerName, manufacturer);
        this.maxColors = maxColors;
        this.currentSpools = currentSpools;
    }

    public int getMaxColors() {
        return maxColors;
    }

    @Override
    public int calculatePrintTime(String filename) {
        return 0;
    }

    @Override
    public boolean printFits(Print print) {
        return false;
    }

    @Override
    public void setCurrentSpools(List<Spool> spools) {
        if (spools.size() > maxColors) {
            throw new IllegalArgumentException("Too many spools for this printer");
        }

        this.currentSpools = new ArrayList<>(spools);
    }

    @Override
    public List<Spool> getCurrentSpools() {
        return currentSpools;
    }
}
