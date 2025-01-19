package tests;

public enum Filament {
    PLA(1, "PLA"),
    PETG(2, "PETG"),
    ABS(3, "ABS");
    final int code;
    final String name;

    Filament(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
