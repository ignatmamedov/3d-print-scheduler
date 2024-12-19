package saxion.view;

import saxion.facade.PrintDTO;
import saxion.facade.PrintTaskDTO;
import saxion.facade.PrinterDTO;
import saxion.facade.SpoolDTO;

public interface View<T> {
    public void show(String string);

    public T formatPrintDTO(PrintDTO print);

    public T formatSpoolDTO(SpoolDTO print);

    public T formatPrintTaskDTO(PrintTaskDTO printTask);

    public T formatPrinterDTO(PrinterDTO printer);

}
