package saxion.printers;

import java.util.Map;

public class PrinterFactory {
    public static Printer fromMap(Map<String, Object> map) {

        int id = Integer.parseInt(map.getOrDefault("id", map.getOrDefault("1", "0")).toString());
        int type = Integer.parseInt(map.getOrDefault("type", map.getOrDefault("2", "0")).toString());
        String name = map.getOrDefault("name", map.getOrDefault("3", "unknown")).toString();
        String manufacturer = map.getOrDefault("manufacturer", map.getOrDefault("4", "unknown")).toString();
        String model = map.getOrDefault("model", map.getOrDefault("5", "unknown")).toString();
        int maxX = Integer.parseInt(map.getOrDefault("maxX", map.getOrDefault("6", "0")).toString());
        int maxY = Integer.parseInt(map.getOrDefault("maxY", map.getOrDefault("7", "0")).toString());
        int maxZ = Integer.parseInt(map.getOrDefault("maxZ", map.getOrDefault("8", "0")).toString());
        int maxColors = Integer.parseInt(map.getOrDefault("maxColors", map.getOrDefault("9", "0")).toString());


        try {
            switch (type) {
                case 1 -> {return new StandardFDM(id, name, manufacturer, false, maxX, maxY, maxZ);}
                case 2 -> {return new StandardFDM(id, name, manufacturer, true, maxX, maxY, maxZ);}
                case 3 -> {return new MultiColor(id, name, manufacturer, false, maxX, maxY, maxZ, maxColors);}
                case 4 -> {return new MultiColor(id, name, manufacturer, true, maxX, maxY, maxZ, maxColors);}
                default -> {throw new IllegalArgumentException("Invalid printer type: " + type);}
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid data format for Printer.", e);
        }
    }
}
