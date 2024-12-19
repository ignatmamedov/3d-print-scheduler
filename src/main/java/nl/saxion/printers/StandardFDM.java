package nl.saxion.printers;

import saxion.models.Print;
import saxion.models.Spool;
import saxion.printers.Printer;
import saxion.printers.SpoolManager;

import java.util.List;

public class StandardFDM extends Printer implements SpoolManager {
    private final int maxX;
    private final int maxY;
    private final int maxZ;
    private Spool currentSpool;

    public StandardFDM(int id, String printerName, String manufacturer, boolean isHoused, int maxX, int maxY, int maxZ) {
        super(id, printerName, manufacturer, isHoused);
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public StandardFDM(int id, String printerName, String manufacturer, int maxX, int maxY, int maxZ) {
        super(id, printerName, manufacturer);
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    @Override
    public int calculatePrintTime(String filename) {
        return 0;
    }

    @Override
    public boolean printFits(Print print) {
        return print.getHeight() <= maxZ && print.getWidth() <= maxX && print.getLength() <= maxY;
    }

    @Override
    public void setCurrentSpools(List<Spool> spools) {
        if (spools.size() != 1) {
            throw new IllegalArgumentException("StandardFDM can only handle one spool");
        }

        this.currentSpool = spools.get(0);
    }

    @Override
    public List<Spool> getCurrentSpools() {
        return List.of(currentSpool);
    }
}
