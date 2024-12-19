package saxion.models;

import saxion.facade.PrintTaskDTO;
import saxion.types.FilamentType;
import saxion.models.Print;

import java.util.List;

public class PrintTask {
    private Print print;
    private List<String> colors;
    private FilamentType filamentType;

    public PrintTask(Print print, List<String> colors, FilamentType filamentType){
        this.print = print;
        this.colors = colors;
        this.filamentType = filamentType;

    }

    public List<String> getColors() {
        return colors;
    }

    public FilamentType getFilamentType() {
        return filamentType;
    }

    public Print getPrint(){
        return print;
    }
    public PrintTaskDTO toDTO(){
        return new PrintTaskDTO(print.getName(), colors, filamentType);
    }
}
