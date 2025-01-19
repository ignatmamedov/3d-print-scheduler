package saxion.models;

import saxion.facade.PrintTaskDTO;
import saxion.types.FilamentType;
import saxion.models.Print;

import java.util.List;

/**
 * Represents a print task that includes a 3D print, its associated colors, and filament type.
 */
public class PrintTask {

    /** The print associated with this task. */
    private final Print print;

    /** The list of colors required for the print task. */
    private final List<String> colors;

    /** The type of filament used for the print task. */
    private final FilamentType filamentType;

    /**
     * Constructs a new {@code PrintTask} with the specified print, colors, and filament type.
     *
     * @param print        the {@link Print} associated with this task
     * @param colors       the list of colors required for the task
     * @param filamentType the type of filament used for the task
     */
    public PrintTask(Print print, List<String> colors, FilamentType filamentType) {
        this.print = print;
        this.colors = colors;
        this.filamentType = filamentType;
    }

    /**
     * Gets the list of colors required for the print task.
     *
     * @return the list of colors
     */
    public List<String> getColors() {
        return colors;
    }

    /**
     * Gets the type of filament used for the print task.
     *
     * @return the filament type
     */
    public FilamentType getFilamentType() {
        return filamentType;
    }

    /**
     * Gets the print associated with this task.
     *
     * @return the {@link Print} object
     */
    public Print getPrint() {
        return print;
    }

    /**
     * Converts this {@code PrintTask} to a {@link PrintTaskDTO}.
     *
     * @return a {@code PrintTaskDTO} representation of this task
     */
    public PrintTaskDTO toDTO() {
        return new PrintTaskDTO(print.getName(), colors, filamentType);
    }
}
