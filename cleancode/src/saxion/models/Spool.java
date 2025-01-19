package saxion.models;

import saxion.facade.SpoolDTO;
import saxion.types.FilamentType;
import java.util.Map;

/**
 * Represents a spool of filament used in 3D printing.
 */
public class Spool {

    /** The unique identifier of the spool. */
    private final int id;

    /** The color of the filament. */
    private final String color;

    /** The type of filament. */
    private final FilamentType filamentType;

    /** The remaining length of the filament on the spool. */
    private double length;

    /**
     * Constructs a new {@code Spool} instance with the specified properties.
     *
     * @param id           the unique identifier of the spool
     * @param color        the color of the filament
     * @param filamentType the type of filament
     * @param length       the initial length of the filament on the spool
     */
    public Spool(int id, String color, FilamentType filamentType, double length) {
        this.id = id;
        this.color = color;
        this.filamentType = filamentType;
        this.length = length;
    }

    /**
     * Gets the unique identifier of the spool.
     *
     * @return the spool ID
     */
    public int getId() {
        return this.id;
    }

    /**
     * Gets the remaining length of the filament on the spool.
     *
     * @return the remaining filament length
     */
    public double getLength() {
        return length;
    }

    /**
     * Checks if the spool matches the specified color and filament type.
     *
     * @param color the color to check
     * @param type  the filament type to check
     * @return {@code true} if the spool matches the color and type, {@code false} otherwise
     */
    public boolean spoolMatch(String color, FilamentType type) {
        if(color.equals(this.color) && type == this.getFilamentType()) {
            return true;
        }
        return false;
    }

    /**
     * This method will try to reduce the length of the spool.
     *
     * @param byLength the length to reduce by
     * @return boolean which tells you if it is possible or not.
     */
    public boolean reduceLength(double byLength) {
        boolean success = true;
        this.length -= byLength;
        if (this.length < 0) {
            this.length += byLength;
            success = false;
        }
        return success;
    }

    /**
     * Gets the color of the filament on the spool.
     *
     * @return the filament color
     */
    public String getColor() {
        return color;
    }

    /**
     * Gets the type of filament on the spool.
     *
     * @return the filament type
     */
    public FilamentType getFilamentType() {
        return filamentType;
    }

    /**
     * Creates a {@code Spool} instance from a map of properties.
     *
     * @param map the map containing spool properties
     * @return a new {@code Spool} instance
     */
    public static Spool fromMap(Map<String, Object> map) {
        int id = Integer.parseInt(map.getOrDefault(
                "id", map.getOrDefault("1", "0")).toString()
        );
        String color = map.getOrDefault("color", map.getOrDefault("2", "unknown")).toString();
        FilamentType type = FilamentType.valueOf(
                map.getOrDefault("filamentType", map.getOrDefault("3", "DEFAULT_TYPE")
                ).toString());
        double length = Double.parseDouble(map.getOrDefault(
                "length", map.getOrDefault("4", "0.0")
        ).toString());

        return new Spool(id, color, type, length);
    }

    /**
     * Converts this {@code Spool} instance to a {@link SpoolDTO}.
     *
     * @return a {@code SpoolDTO} representation of this spool
     */
    public SpoolDTO toDTO() {
        return new SpoolDTO(id, color, filamentType, length);
    }

}
