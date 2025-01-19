package saxion.observer;

/**
 * Interface representing an observer in the observer pattern.
 * Observers are notified of changes or events by the observable objects they are attached to.
 */
public interface Observer {

    /**
     * Called when the observable object notifies its observers of a change or event.
     *
     * @param event the {@link PrintEvent} containing information about the event or change
     */
    void update(PrintEvent event);
}
