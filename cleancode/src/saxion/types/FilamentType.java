package saxion.types;

/**
 * The `FilamentType` enum represents the types of filament used in 3D printing.
 */
public enum FilamentType {
    PLA, PETG, ABS;

    /**
     * Retrieves a {@code FilamentType} based on its corresponding integer value.
     *
     * @param filamentType the integer representing the filament type (1 for PLA, 2 for PETG, 3 for ABS)
     * @return the corresponding {@link FilamentType} enum value
     * @throws IllegalArgumentException if the provided integer does not map to a valid filament type
     */
    public static FilamentType getFilamentType(int filamentType) {
        return switch (filamentType) {
            case 1 -> FilamentType.PLA;
            case 2 -> FilamentType.PETG;
            case 3 -> FilamentType.ABS;
            default -> throw new IllegalArgumentException("Invalid filament type");
        };
    }
}
