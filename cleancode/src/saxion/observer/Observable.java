package saxion.observer;

/**
 * The `Observable` interface defines methods for managing a list of observers
 * and notifying them of changes or events.
 */
public interface Observable {

    /**
     * Adds an observer to the list of observers.
     *
     * @param observer the {@link Observer} to add
     */
    void addObserver(Observer observer);

    /**
     * Removes an observer from the list of observers.
     *
     * @param observer the {@link Observer} to remove
     */
    void removeObserver(Observer observer);

    /**
     * Notifies all registered observers about an event or change.
     */
    void notifyObservers();
}
