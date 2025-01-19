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

/**
 * Base implementation of a printing strategy for managing print tasks and spool changes.
 * Supports notifying observers about spool change events and provides reusable methods
 * for matching printers and spools to print tasks.
 */
public class BasePrintingStrategy implements Observable {

    /** List of observers monitoring this strategy. */
    private final List<Observer> observers = new ArrayList<>();

    /** Counter for tracking spool change events. */
    private int spoolChangeCount = 0;

    /**
     * Updates the spool change count and notifies all observers.
     */
    protected void updateSpoolChangeCount() {
        spoolChangeCount = 1;
        notifyObservers();
        spoolChangeCount = 0;
    }

    /**
     * Adds an observer to monitor spool change events.
     *
     * @param observer the {@link Observer} to add
     */
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer from monitoring spool change events.
     *
     * @param observer the {@link Observer} to remove
     */
    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all observers about the current spool change event.
     */
    @Override
    public void notifyObservers() {
        PrintEvent event = new PrintEvent(spoolChangeCount, 0);
        for (Observer observer : observers) {
            observer.update(event);
        }
    }

    /**
     * Checks if the print task matches the requirements for a standard FDM printer.
     *
     * @param printTask the {@link PrintTask} to check
     * @return {@code true} if the task matches; {@code false} otherwise
     */
    protected boolean matchesStandardFDM(PrintTask printTask) {
        return printTask.getFilamentType() != FilamentType.ABS && printTask.getColors().size() == 1;
    }

    /**
     * Checks if the print task matches the requirements for a housed printer.
     *
     * @param printTask the {@link PrintTask} to check
     * @return {@code true} if the task matches; {@code false} otherwise
     */
    protected boolean matchesHousedPrinter(PrintTask printTask) {
        return printTask.getColors().size() == 1;
    }

    /**
     * Checks if the print task matches the requirements for a multi-color printer.
     *
     * @param printer   the {@link MultiColor} printer to check against
     * @param printTask the {@link PrintTask} to check
     * @return {@code true} if the task matches; {@code false} otherwise
     */
    protected boolean matchesMultiColorPrinter(MultiColor printer, PrintTask printTask) {
        return printTask.getFilamentType() != FilamentType.ABS
                && printTask.getColors().size() <= printer.getMaxColors();
    }

    /**
     * Checks if the spools match the requirements for a standard FDM printer.
     *
     * @param printTask the {@link PrintTask} to check
     * @param spools    the list of {@link Spool} objects to validate
     * @return {@code true} if the spools match; {@code false} otherwise
     */
    protected boolean matchesSpoolsForStandardFDM(PrintTask printTask, List<Spool> spools) {
        return spools.get(0).spoolMatch(printTask.getColors().get(0), printTask.getFilamentType());
    }

    /**
     * Checks if the spools match the requirements for a housed printer.
     *
     * @param printTask the {@link PrintTask} to check
     * @param spools    the list of {@link Spool} objects to validate
     * @return {@code true} if the spools match; {@code false} otherwise
     */
    protected boolean matchesSpoolsForHousedPrinter(PrintTask printTask, List<Spool> spools) {
        return spools.get(0).spoolMatch(printTask.getColors().get(0), printTask.getFilamentType());
    }

    /**
     * Checks if the spools match the requirements for a multi-color printer.
     *
     * @param printTask the {@link PrintTask} to check
     * @param spools    the list of {@link Spool} objects to validate
     * @return {@code true} if the spools match; {@code false} otherwise
     */
    protected boolean matchesSpoolsForMultiColorPrinter(PrintTask printTask, List<Spool> spools) {
        for (int i = 0; i < spools.size() && i < printTask.getColors().size(); i++) {
            if (!spools.get(i).spoolMatch(printTask.getColors().get(i), printTask.getFilamentType())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Handles spool changes for the given printer and print task.
     *
     * @param printer      the {@link Printer} to update
     * @param printTask    the {@link PrintTask} to execute
     * @param freeSpools   the list of available {@link Spool} objects
     * @param messages     the list of messages to append spool change instructions
     * @return {@code true} if the spool change was successful; {@code false} otherwise
     */
    protected boolean handleSpoolChange(Printer printer, PrintTask printTask, List<Spool> freeSpools, List<String> messages) {
        if (printer.isHoused() && matchesHousedPrinter(printTask)) {
            return changeSpoolForHousedPrinter(printer, printTask, freeSpools, messages);
        } else if (printer instanceof MultiColor && matchesMultiColorPrinter((MultiColor) printer, printTask)) {
            return changeSpoolsForMultiColorPrinter((MultiColor) printer, printTask, freeSpools, messages);
        } else if (!printer.isHoused() && matchesStandardFDM(printTask)) {
            return changeSpoolForStandardFDM(printer, printTask, freeSpools, messages);
        }

        return false;
    }

    /**
     * Changes the spool for a standard FDM printer.
     *
     * @param printer    the {@link Printer} to update
     * @param printTask  the {@link PrintTask} to execute
     * @param freeSpools the list of available {@link Spool} objects
     * @param messages   the list of messages to append spool change instructions
     * @return {@code true} if the spool was successfully changed; {@code false} otherwise
     */
    protected boolean changeSpoolForStandardFDM(
            Printer printer,
            PrintTask printTask,
            List<Spool> freeSpools,
            List<String> messages
    ) {
        for (Spool spool : freeSpools) {
            if (spool.spoolMatch(printTask.getColors().get(0), printTask.getFilamentType())) {
                replaceSpool(printer, spool, freeSpools, messages);
                return true;
            }
        }
        return false;
    }

    /**
     * Changes the spool for a housed printer.
     *
     * @param printer    the {@link Printer} to update
     * @param printTask  the {@link PrintTask} to execute
     * @param freeSpools the list of available {@link Spool} objects
     * @param messages   the list of messages to append spool change instructions
     * @return {@code true} if the spool was successfully changed; {@code false} otherwise
     */
    protected boolean changeSpoolForHousedPrinter(
            Printer printer,
            PrintTask printTask,
            List<Spool> freeSpools,
            List<String> messages
    ) {
        for (Spool spool : freeSpools) {
            if (spool.spoolMatch(printTask.getColors().get(0), printTask.getFilamentType())) {
                replaceSpool(printer, spool, freeSpools, messages);
                return true;
            }
        }
        return false;
    }

    /**
     * Changes the spools for a multi-color printer.
     *
     * @param printer    the {@link MultiColor} printer to update
     * @param printTask  the {@link PrintTask} to execute
     * @param freeSpools the list of available {@link Spool} objects
     * @param messages   the list of messages to append spool change instructions
     * @return {@code true} if the spools were successfully changed; {@code false} otherwise
     */
    protected boolean changeSpoolsForMultiColorPrinter(
            MultiColor printer,
            PrintTask printTask,
            List<Spool> freeSpools,
            List<String> messages
    ) {
        List<Spool> chosenSpools = new ArrayList<>();
        for (int i = 0; i < printTask.getColors().size(); i++) {
            for (Spool spool : freeSpools) {
                if (spool.spoolMatch(printTask.getColors().get(i), printTask.getFilamentType())
                        && !containsSpool(chosenSpools, printTask.getColors().get(i))) {
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

    /**
     * Replaces the spool for a standard FDM printer.
     *
     * @param printer      the {@link Printer} to update
     * @param newSpool     the new {@link Spool} to use
     * @param freeSpools   the list of available spools
     * @param messages     the list of messages to append spool change instructions
     */
    protected void replaceSpool(Printer printer, Spool newSpool, List<Spool> freeSpools, List<String> messages) {
        List<Spool> currentSpools = printer.getCurrentSpools();
        if (!currentSpools.isEmpty()) {
            freeSpools.add(currentSpools.get(0));
        }
        freeSpools.remove(newSpool);
        printer.setCurrentSpools(List.of(newSpool));

        messages.add("- Spool change: Please place spool " + newSpool.getId()
                + " in printer " + printer.getName());

        updateSpoolChangeCount();
    }

    /**
     * Replaces the spools for a multi-color printer.
     *
     * @param printer      the {@link Printer} to update
     * @param newSpools    the list of new {@link Spool} objects to use
     * @param freeSpools   the list of available spools
     * @param messages     the list of messages to append spool change instructions
     */
    protected void replaceSpools(
            Printer printer,
            List<Spool> newSpools,
            List<Spool> freeSpools,
            List<String> messages
    ) {
        freeSpools.addAll(printer.getCurrentSpools());
        freeSpools.removeAll(newSpools);
        printer.setCurrentSpools(newSpools);
        int position = 1;
        for (Spool spool : newSpools) {
            messages.add("- Spool change: Please place spool " + spool.getId()
                    + " in printer " + printer.getName()
                    + " position " + position);
            position++;
            updateSpoolChangeCount();
        }
    }

    /**
     * Checks if the list of spools contains a spool with the specified color.
     *
     * @param list the list of {@link Spool} objects to check
     * @param name the color name to look for
     * @return {@code true} if a matching spool is found; {@code false} otherwise
     */
    protected boolean containsSpool(final List<Spool> list, final String name) {
        return list.stream().anyMatch(o -> o.getColor().equals(name));
    }

    /**
     * Checks if the specified printer matches the requirements for the given print task.
     *
     * @param printer   the {@link Printer} to check
     * @param printTask the {@link PrintTask} to validate
     * @return {@code true} if the printer matches; {@code false} otherwise
     */
    protected boolean matchesCurrentPrinter(Printer printer, PrintTask printTask) {
        if(printer.isHoused()){
            return matchesHousedPrinter(printTask);
        } else if(printer instanceof MultiColor){
            return matchesMultiColorPrinter((MultiColor) printer, printTask);
        } else {
            return matchesStandardFDM(printTask);
        }
    }
}
