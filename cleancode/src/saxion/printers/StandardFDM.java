package saxion.printers;

import saxion.models.Print;
import saxion.models.Spool;
import saxion.facade.PrinterDTO;
import saxion.facade.SpoolDTO;

import java.util.List;

public class StandardFDM extends Printer implements SpoolManager {
    private final int maxX;
    private final int maxY;
    private final int maxZ;

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getMaxZ() {
        return maxZ;
    }

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
        return currentSpool != null ? List.of(currentSpool) : List.of();
    }


    @Override
    public PrinterDTO toDTO() {
        List<SpoolDTO> spools = currentSpool != null
                ? List.of(currentSpool.toDTO())
                : null;

        return new PrinterDTO(
                getId(),
                getName(),
                getManufacturer(),
                isHoused(),
                maxX,
                maxY,
                maxZ,
                null,
                spools,
                getTask() != null ? getTask().toDTO() : null
        );
    }
}
