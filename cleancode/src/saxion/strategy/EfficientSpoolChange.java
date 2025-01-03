package saxion.strategy;

import saxion.models.PrintTask;
import saxion.models.Spool;
import saxion.observer.Observable;
import saxion.observer.Observer;
import saxion.observer.PrintEvent;
import saxion.printers.Printer;

import java.util.ArrayList;
import java.util.List;

public class EfficientSpoolChange implements PrintingStrategy, Observable {
    private final List<Observer> observers = new ArrayList<>();
    private int spoolChangeCount = 0;

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        PrintEvent printEvent = new PrintEvent(spoolChangeCount, 0);
        for (Observer observer : observers) {
            observer.update(printEvent);
        }
    }

    @Override
    public String selectPrintTask(Printer printer, List<PrintTask> pendingPrintTasks, List<Printer> printers, List<Spool> freeSpools) {
        return "Hello from EfficientSpoolChange";
    }
}
