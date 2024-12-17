package models;

import nl.saxion.Models.HousedPrinter;
import nl.saxion.Models.MultiColor;
import nl.saxion.Models.StandardFDM;
import nl.saxion.Models.Printer;

import java.util.Map;

public class PrinterFactory {
    public static Printer fromMap(Map<String, Object> map) {

        int id = ((Long) map.get("id")).intValue();
        int type = ((Long) map.get("type")).intValue();
        String name = (String) map.get("name");
        String manufacturer = (String) map.get("manufacturer");
        int maxX = ((Long) map.get("maxX")).intValue();
        int maxY = ((Long) map.get("maxY")).intValue();
        int maxZ = ((Long) map.get("maxZ")).intValue();
        int maxColors = ((Long) map.get("maxColors")).intValue();

        try {
            switch (type) {
                case 1 -> {return new StandardFDM(id, name, manufacturer, maxX, maxY, maxZ);}
                case 2 -> {return new HousedPrinter(id, name, manufacturer, maxX, maxY, maxZ);}
                case 3 -> {return new MultiColor(id, name, manufacturer, maxX, maxY, maxZ, maxColors);}
                default -> {throw new IllegalArgumentException("Invalid printer type: " + type);}
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid data format for Printer.", e);
        }
    }
}
