package saxion.view;

import saxion.facade.PrintDTO;
import saxion.facade.PrintTaskDTO;
import saxion.facade.PrinterDTO;
import saxion.facade.SpoolDTO;
import saxion.printers.Printer;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TerminalView implements View<String>{

    @Override
    public void show(String string){
        System.out.println(string);
    }

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

    @Override
    public String formatSpoolDTO(SpoolDTO spool){
        return  "--------" + System.lineSeparator() +
                "- id: " + spool.id() + System.lineSeparator() +
                "- color: " + spool.color() + System.lineSeparator() +
                "- filamentType: " + spool.filamentType() + System.lineSeparator() +
                "- length: " + spool.length() + System.lineSeparator() +
                "--------";
    }
    @Override
    public String formatPrintTaskDTO(PrintTaskDTO printTask){
        return "< " + printTask.print() +
                " " + printTask.filamentType() +
                " " + printTask.colors().toString() +
                " >";
    }

    @Override
    public String formatPrinterDTO(PrinterDTO printer){
        StringBuilder result = new StringBuilder();

        result.append("--------").append(System.lineSeparator());
        result.append("- ID: ").append(printer.id()).append(System.lineSeparator());
        result.append("- Name: ").append(printer.name()).append(System.lineSeparator());
        result.append("- Manufacturer: ").append(printer.manufacturer()).append(System.lineSeparator());

        if (printer.maxX() != null) result.append("- maxX: ").append(printer.maxX()).append(System.lineSeparator());
        if (printer.maxY() != null) result.append("- maxY: ").append(printer.maxY()).append(System.lineSeparator());
        if (printer.maxZ() != null) result.append("- maxZ: ").append(printer.maxZ()).append(System.lineSeparator());
        if (printer.maxColors() != null) result.append("- maxColors: ").append(printer.maxColors()).append(System.lineSeparator());

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
