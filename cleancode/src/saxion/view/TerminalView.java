package saxion.view;

import saxion.facade.PrintDTO;
import saxion.facade.PrintTaskDTO;
import saxion.facade.PrinterDTO;
import saxion.facade.SpoolDTO;
import saxion.printers.Printer;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The `TerminalView` class implements the {@link View} interface to provide
 * methods for displaying and formatting data for terminal output.
 */
public class TerminalView implements View<String>{

    /**
     * Displays a given string in the terminal.
     *
     * @param string the string to display
     */
    @Override
    public void show(String string){
        System.out.println(string);
    }

    /**
     * Formats a {@link PrintDTO} object as a human-readable string for terminal output.
     *
     * @param print the {@link PrintDTO} to format
     * @return a formatted string representation of the print
     */
    @Override
    public String formatPrintDTO(PrintDTO print){
        return "--------" + System.lineSeparator() +
                "- Name: " + print.name() + System.lineSeparator() +
                "- Height: " + print.height() + System.lineSeparator() +
                "- Width: " + print.width() + System.lineSeparator() +
                "- Length: " + print.length() + System.lineSeparator() +
                "- FilamentLength: " + print.filamentLength() + System.lineSeparator() +
                "- Print Time: " + print.printTime() + System.lineSeparator() +
                "--------";
    }

    /**
     * Formats a {@link SpoolDTO} object as a human-readable string for terminal output.
     *
     * @param spool the {@link SpoolDTO} to format
     * @return a formatted string representation of the spool
     */
    @Override
    public String formatSpoolDTO(SpoolDTO spool){
        return  "--------" + System.lineSeparator() +
                "- id: " + spool.id() + System.lineSeparator() +
                "- color: " + spool.color() + System.lineSeparator() +
                "- filamentType: " + spool.filamentType() + System.lineSeparator() +
                "- length: " + spool.length() + System.lineSeparator() +
                "--------";
    }

    /**
     * Formats a {@link PrintTaskDTO} object as a human-readable string for terminal output.
     *
     * @param printTask the {@link PrintTaskDTO} to format
     * @return a formatted string representation of the print task
     */
    @Override
    public String formatPrintTaskDTO(PrintTaskDTO printTask){
        return "< " + printTask.print() +
                " " + printTask.filamentType() +
                " " + printTask.colors().toString() +
                " >";
    }

    /**
     * Formats a {@link PrinterDTO} object as a human-readable string for terminal output.
     *
     * @param printer the {@link PrinterDTO} to format
     * @return a formatted string representation of the printer
     */
    @Override
    public String formatPrinterDTO(PrinterDTO printer){
        StringBuilder result = new StringBuilder();

        result.append("--------").append(System.lineSeparator());
        result.append("- ID: ").append(printer.id()).append(System.lineSeparator());
        result.append("- Name: ").append(printer.name()).append(System.lineSeparator());
        result.append("- Manufacturer: ").append(printer.manufacturer()).append(System.lineSeparator());
        result.append("- Housed: ").append(printer.isHoused() ? "Yes" : "No").append(System.lineSeparator());

        if (printer.maxX() != null) result.append("- maxX: ").append(printer.maxX()).append(System.lineSeparator());
        if (printer.maxY() != null) result.append("- maxY: ").append(printer.maxY()).append(System.lineSeparator());
        if (printer.maxZ() != null) result.append("- maxZ: ").append(printer.maxZ()).append(System.lineSeparator());
        if (printer.maxColors() != null) result.append("- maxColors: ").append(printer.maxColors()).append(System.lineSeparator());

        if (printer.task() != null) {
            result.append("- Task: ").append(formatPrintTaskDTO(printer.task())).append(System.lineSeparator());
        }

        List<SpoolDTO> spools = Objects.requireNonNullElse(printer.spools(), List.of());
        if (!spools.isEmpty()) {
            result.append("- Spool(s):").append(System.lineSeparator());
            String spoolsInfo = spools.stream()
                    .map(this::formatSpoolDTO)
                    .collect(Collectors.joining(System.lineSeparator()));
            result.append(spoolsInfo).append(System.lineSeparator());
        }

        result.append("--------");

        return result.toString();
    }
}
