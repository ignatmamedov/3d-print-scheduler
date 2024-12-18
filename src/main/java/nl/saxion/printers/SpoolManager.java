package nl.saxion.printers;

import nl.saxion.Models.Spool;

import java.util.List;

public interface SpoolManager {
    void setCurrentSpools(List<Spool> spools);
    List<Spool> getCurrentSpools();
}
