package models;

import nl.saxion.Models.FilamentType;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Spool {
    private final int id;
    private final String color;
    private final FilamentType filamentType;
    private double length;

    public Spool(int id, String color, FilamentType filamentType, double length) {
        this.id = id;
        this.color = color;
        this.filamentType = filamentType;
        this.length = length;
    }

    public int getId() {
        return this.id;
    }

    public double getLength() {
        return length;
    }

    public boolean spoolMatch(String color, FilamentType type) {
        if(color.equals(this.color) && type == this.getFilamentType()) {
            return true;
        }
        return false;
    }
    /**
     * This method will try to reduce the length of the spool.
     *
     * @param byLength
     * @return boolean which tells you if it is possible or not.
     */
    public boolean reduceLength(double byLength) {
        boolean success = true;
        this.length -= byLength;
        if (this.length < 0) {
            this.length -= byLength;
            success = false;
        }
        return success;
    }

    public String getColor() {
        return color;
    }

    public FilamentType getFilamentType(){
        return filamentType;
    }

    @Override
    public String toString() {
        return  "--------" + System.lineSeparator() +
                "- id: " + id + System.lineSeparator() +
                "- color: " + color + System.lineSeparator() +
                "- filamentType: " + filamentType + System.lineSeparator() +
                "- length: " + length + System.lineSeparator() +
                "--------";
    }


    public static Spool fromMap(Map<String, Object> map) {
        int id = Integer.parseInt(map.getOrDefault("id", map.getOrDefault("1", "0")).toString());
        String color = map.getOrDefault("color", map.getOrDefault("2", "unknown")).toString();
        FilamentType type = FilamentType.valueOf(map.getOrDefault("filamentType", map.getOrDefault("3", "DEFAULT_TYPE")).toString());
        double length = Double.parseDouble(map.getOrDefault("length", map.getOrDefault("4", "0.0")).toString());

        return new Spool(id, color, type, length);

    }
}
