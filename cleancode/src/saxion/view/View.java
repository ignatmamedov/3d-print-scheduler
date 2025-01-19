package saxion.view;

import saxion.facade.PrintDTO;
import saxion.facade.PrintTaskDTO;
import saxion.facade.PrinterDTO;
import saxion.facade.SpoolDTO;

/**
 * The `View` interface defines methods for displaying and formatting
 * data to be presented in a user interface.
 *
 * @param <T> the type of the formatted output (e.g., {@code String} for terminal views)
 */
public interface View<T> {

    /**
     * Displays a given string to the user.
     *
     * @param string the string to display
     */
    void show(String string);

    /**
     * Formats a {@link PrintDTO} object for presentation.
     *
     * @param print the {@link PrintDTO} to format
     * @return the formatted representation of the print
     */
    T formatPrintDTO(PrintDTO print);

    /**
     * Formats a {@link SpoolDTO} object for presentation.
     *
     * @param spool the {@link SpoolDTO} to format
     * @return the formatted representation of the spool
     */
    T formatSpoolDTO(SpoolDTO spool);

    /**
     * Formats a {@link PrintTaskDTO} object for presentation.
     *
     * @param printTask the {@link PrintTaskDTO} to format
     * @return the formatted representation of the print task
     */
    T formatPrintTaskDTO(PrintTaskDTO printTask);

    /**
     * Formats a {@link PrinterDTO} object for presentation.
     *
     * @param printer the {@link PrinterDTO} to format
     * @return the formatted representation of the printer
     */
    T formatPrinterDTO(PrinterDTO printer);
}
