package view;

import facade.PrintDTO;

public class TerminalView implements View<String>{

    @Override
    public void show(String string){
        System.out.println(string);
    }

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



}
