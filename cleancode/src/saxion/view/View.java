package saxion.view;

import saxion.facade.PrintDTO;

public interface View<T> {
    public void show(String string);

    public T formatPrintDTO(PrintDTO print);
}
