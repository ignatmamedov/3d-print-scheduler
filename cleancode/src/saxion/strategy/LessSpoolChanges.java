package saxion.strategy;

import saxion.models.PrintTask;
import saxion.models.Spool;
import saxion.observer.Observable;
import saxion.observer.Observer;
import saxion.observer.PrintEvent;
import saxion.printers.MultiColor;
import saxion.printers.Printer;
import saxion.types.FilamentType;

import java.util.ArrayList;
import java.util.List;

public class LessSpoolChanges implements PrintingStrategy, Observable {
    private int spoolChangeCount = 0;
    private final List<Observer> observers;

    public LessSpoolChanges() {
        observers = new ArrayList<>();
    }

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
        spoolChangeCount++;
        PrintEvent event = new PrintEvent(spoolChangeCount, 0);
        for (Observer observer : observers) {
            observer.update(event);
        }
    }

    @Override
    public String selectPrintTask(
            Printer printer, List<PrintTask> pendingPrintTasks, List<Printer> printers, List<Spool> freeSpools
    ) {
        List<String> messages = new ArrayList<>();

        List<Spool> spools = printer.getCurrentSpools();
        PrintTask chosenTask = null;

        if (!spools.isEmpty()) {
            chosenTask = findTaskForCurrentSpools(printer, spools, pendingPrintTasks);
        }

        if (chosenTask == null) {
            chosenTask = findTaskForFreeSpools(printer, pendingPrintTasks, freeSpools, messages);
        }

        if (chosenTask != null) {
            pendingPrintTasks.remove(chosenTask);
            printer.setTask(chosenTask);
            messages.add("- Started task: " + chosenTask.getPrint().getName() + " "
                    + chosenTask.getFilamentType() + " on printer " + printer.getName());
        }

        return String.join("\n", messages);
    }

    private PrintTask findTaskForCurrentSpools(Printer printer, List<Spool> spools, List<PrintTask> pendingPrintTasks) {
        for (PrintTask printTask : pendingPrintTasks) {
            if (printer.printFits(printTask.getPrint())) {
                if (taskMatchesPrinterAndSpools(printer, printTask, spools)) {
                    printer.setTask(printTask);
                    return printTask;
                }
            }
        }
        return null;
    }

    private PrintTask findTaskForFreeSpools(
            Printer printer, List<PrintTask> pendingPrintTasks, List<Spool> freeSpools, List<String> messages
    ) {
        for (PrintTask printTask : pendingPrintTasks) {
            if (printer.printFits(printTask.getPrint()) && printer.getTask() == null) {
                if (handleSpoolChange(printer, printTask, freeSpools, messages)) {
                    return printTask;
                }
            }
        }
        return null;
    }

    private boolean taskMatchesPrinterAndSpools(Printer printer, PrintTask printTask, List<Spool> spools) {
        if (printer.isHoused()) {
            return matchesHousedPrinter(printTask, spools);
        } else if (printer instanceof MultiColor) {
            return matchesMultiColorPrinter((MultiColor) printer, printTask, spools);
        } else {
            return matchesStandardFDM(printTask, spools);
        }
    }

    private boolean matchesStandardFDM(PrintTask printTask, List<Spool> spools) {
        return printTask.getFilamentType() != FilamentType.ABS && printTask.getColors().size() == 1
                && spools.get(0).spoolMatch(printTask.getColors().get(0), printTask.getFilamentType());
    }

    private boolean matchesHousedPrinter(PrintTask printTask, List<Spool> spools) {
        return printTask.getColors().size() == 1
                && spools.get(0).spoolMatch(printTask.getColors().get(0), printTask.getFilamentType());
    }

    private boolean matchesMultiColorPrinter(MultiColor printer, PrintTask printTask, List<Spool> spools) {
        if (printTask.getFilamentType() == FilamentType.ABS || printTask.getColors().size() > printer.getMaxColors()) {
            return false;
        }

        for (int i = 0; i < spools.size() && i < printTask.getColors().size(); i++) {
            if (!spools.get(i).spoolMatch(printTask.getColors().get(i), printTask.getFilamentType())) {
                return false;
            }
        }
        return true;
    }

    private boolean handleSpoolChange(Printer printer, PrintTask printTask, List<Spool> freeSpools, List<String> messages) {
        if (printer.isHoused()) {
            return changeSpoolForHousedPrinter(printer, printTask, freeSpools, messages);
        } else if (printer instanceof MultiColor) {
            return changeSpoolsForMultiColorPrinter((MultiColor) printer, printTask, freeSpools, messages);
        } else {
            return changeSpoolForStandardFDM(printer, printTask, freeSpools, messages);
        }
    }

    private boolean changeSpoolForStandardFDM(Printer printer, PrintTask printTask, List<Spool> freeSpools, List<String> messages) {
        for (Spool spool : freeSpools) {
            if (spool.spoolMatch(printTask.getColors().get(0), printTask.getFilamentType())) {
                replaceSpool(printer, spool, freeSpools, messages);
                return true;
            }
        }
        return false;
    }

    private boolean changeSpoolForHousedPrinter(Printer printer, PrintTask printTask, List<Spool> freeSpools, List<String> messages) {
        for (Spool spool : freeSpools) {
            if (spool.spoolMatch(printTask.getColors().get(0), printTask.getFilamentType())) {
                replaceSpool(printer, spool, freeSpools, messages);
                return true;
            }
        }
        return false;
    }

    private boolean changeSpoolsForMultiColorPrinter(MultiColor printer, PrintTask printTask, List<Spool> freeSpools, List<String> messages) {
        List<Spool> chosenSpools = new ArrayList<>();
        for (int i = 0; i < printTask.getColors().size(); i++) {
            for (Spool spool : freeSpools) {
                if (spool.spoolMatch(printTask.getColors().get(i), printTask.getFilamentType()) && !containsSpool(chosenSpools, printTask.getColors().get(i))) {
                    chosenSpools.add(spool);
                    break;
                }
            }
        }

        if (chosenSpools.size() == printTask.getColors().size()) {
            replaceSpools(printer, chosenSpools, freeSpools, messages);
            return true;
        }
        return false;
    }

    private void replaceSpool(Printer printer, Spool newSpool, List<Spool> freeSpools, List<String> messages) {
        List<Spool> currentSpools = printer.getCurrentSpools();
        if (!currentSpools.isEmpty()) {
            freeSpools.add(currentSpools.get(0));
        }
        freeSpools.remove(newSpool);
        printer.setCurrentSpools(List.of(newSpool));

        messages.add("- Spool change: Please place spool " + newSpool.getId()
                + " in printer " + printer.getName());
        notifyObservers();
    }

    private void replaceSpools(Printer printer, List<Spool> newSpools, List<Spool> freeSpools, List<String> messages) {
        freeSpools.addAll(printer.getCurrentSpools());
        freeSpools.removeAll(newSpools);
        printer.setCurrentSpools(newSpools);
        int position = 1;
        for (Spool spool : newSpools) {
            messages.add("- Spool change: Please place spool " + spool.getId()
                    + " in printer " + printer.getName()
                    + " position " + position);
            position++;
        }

        notifyObservers();
    }

    private boolean containsSpool(final List<Spool> list, final String name) {
        return list.stream().anyMatch(o -> o.getColor().equals(name));
    }
}