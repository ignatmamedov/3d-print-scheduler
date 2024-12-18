package saxion.view;

import saxion.facade.PrintDTO;
import saxion.facade.PrintTaskDTO;
import saxion.facade.SpoolDTO;

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


}
