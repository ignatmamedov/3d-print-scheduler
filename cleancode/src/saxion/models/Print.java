package saxion.models;

import saxion.facade.PrintDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.List;

/**
 * Represents a 3D print job with its properties and details.
 */
public class Print {

    /** The name of the print job. */
    private String name;

    /** The height of the print job in millimeters. */
    private int height;

    /** The width of the print job in millimeters. */
    private int width;

    /** The length of the print job in millimeters. */
    private int length;

    /** The list of filament lengths required for the print job. */
    private ArrayList<Double> filamentLength;

    /** The estimated time to complete the print job in minutes. */
    private int printTime;

    /**
     * Constructs a new {@code Print} instance with the specified properties.
     *
     * @param name           the name of the print job
     * @param height         the height of the print job in millimeters
     * @param width          the width of the print job in millimeters
     * @param length         the length of the print job in millimeters
     * @param filamentLength the list of filament lengths required for the print job
     * @param printTime      the estimated time to complete the print job in minutes
     */
    public Print(String name, int height, int width, int length, ArrayList<Double> filamentLength, int printTime) {
        this.name = name;
        this.height = height;
        this.width = width;
        this.length = length;
        this.filamentLength = filamentLength;
        this.printTime = printTime;
    }

    /**
     * Gets the name of the print job.
     *
     * @return the name of the print job
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the length of the print job in millimeters.
     *
     * @return the length of the print job
     */
    public int getLength() {
        return length;
    }

    /**
     * Gets the height of the print job in millimeters.
     *
     * @return the height of the print job
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the width of the print job in millimeters.
     *
     * @return the width of the print job
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the list of filament lengths required for the print job.
     *
     * @return an {@link ArrayList} of filament lengths
     */
    public ArrayList<Double> getFilamentLength() {
        return filamentLength;
    }

    /**
     * Creates a {@code Print} instance from a map of properties.
     *
     * @param map the map containing print properties
     * @return a new {@code Print} instance
     */
    public static Print fromMap(Map<String, Object> map) {
        String name = String.valueOf(map.getOrDefault("name", map.getOrDefault("1", "unknown")));
        int height = Integer.parseInt(String.valueOf(map.getOrDefault("height", map.getOrDefault("2", "0"))));
        int width = Integer.parseInt(String.valueOf(map.getOrDefault("width", map.getOrDefault("3", "0"))));
        int length = Integer.parseInt(String.valueOf(map.getOrDefault("length", map.getOrDefault("4", "0"))));
        int printTime = Integer.parseInt((map.getOrDefault("printTime", map.getOrDefault("5", "0"))).toString());

        ArrayList<Double> filamentLength = map.getOrDefault("filamentLength", map.getOrDefault("6", ""))
                instanceof List
                ? new ArrayList<>((List<Double>) map.getOrDefault("filamentLength", map.getOrDefault("6", new ArrayList<Double>())))
                : new ArrayList<>(Arrays.stream(String.valueOf(map.getOrDefault("filamentLength", map.getOrDefault("6", "0"))).split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Double::parseDouble)
                .toList());

        return new Print(name, height, width, length, filamentLength, printTime);
    }

    /**
     * Converts this {@code Print} instance to a string representation.
     *
     * @return the name of the print job
     */
    @Override
    public String toString() {
        return getName();
    }

    /**
     * Converts this {@code Print} instance to a {@link PrintDTO}.
     *
     * @return a {@code PrintDTO} representation of this print job
     */
    public PrintDTO toDTO() {
        return new PrintDTO(name, height, width, length, filamentLength, printTime);
    }
}
