package service.position;

/**
 * Created by alban on 16/04/15.
 */
public interface PositionUnchangedObservable {
    public void addObserversPositionsUnhanged(PositionUnchangedObserver observer);
    public void removeObserversPositionsUnhanged(PositionUnchangedObserver observer);
    public void notifyObserversForPositionUnchanged();
}
