package saxion.printers;

import saxion.models.Spool;

import java.util.List;

public interface SpoolManager {
    void setCurrentSpools(List<Spool> spools);
    List<Spool> getCurrentSpools();
}
