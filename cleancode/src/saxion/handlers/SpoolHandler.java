package saxion.handlers;

import saxion.dataprovider.DataProvider;
import saxion.models.PrintTask;
import saxion.models.Spool;
import saxion.printers.Printer;
import saxion.types.FilamentType;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
public class SpoolHandler {
    private List<Spool> spools;
    private List<Spool> freeSpools;

    public List<Spool> getSpools() {
        return spools;
    }

    public List<Spool> getFreeSpools() {
        return freeSpools;
    }

    public void validateColors(List<String> colors, FilamentType type) {
        for (String color : colors) {
            boolean found = spools.stream()
                    .anyMatch(spool -> spool.getColor().equals(color) && spool.getFilamentType() == type);
            if (!found) {
                throw new IllegalArgumentException("Color " + color + " (" + type + ") not found");
            }
        }
    }

    public List<String> getAvailableColors(int filamentType) {
        try {
            FilamentType type = FilamentType.getFilamentType(filamentType);

            List<String> availableColors = new ArrayList<>();
            for (Spool spool : spools) {
                if (spool.getFilamentType() == type && !availableColors.contains(spool.getColor())) {
                    availableColors.add(spool.getColor());
                }
            }
            return availableColors;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public void reduceSpoolLength(Printer printer, PrintTask task) {
        List<Spool> spools = printer.getCurrentSpools();
        for (int i = 0; i < spools.size() && i < task.getColors().size(); i++) {
            spools.get(i).reduceLength(task.getPrint().getFilamentLength().get(i));
        }
    }

    public void readSpools(String filename, boolean header) throws FileNotFoundException {
        DataProvider dataProvider = new DataProvider();
        if (filename.isEmpty()) {
            filename = dataProvider.DEFAULT_SPOOLS_FILE;
        }
        spools = dataProvider.readFromFile(filename, Spool.class, header);
        freeSpools = new ArrayList<>(spools);
    }
}
