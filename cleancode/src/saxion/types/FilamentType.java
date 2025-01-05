package saxion.types;

public enum FilamentType {
    PLA, PETG, ABS;

    public static FilamentType getFilamentType(int filamentType) {
        return switch (filamentType) {
            case 1 -> FilamentType.PLA;
            case 2 -> FilamentType.PETG;
            case 3 -> FilamentType.ABS;
            default -> throw new IllegalArgumentException("Invalid filament type");
        };
    }
}
