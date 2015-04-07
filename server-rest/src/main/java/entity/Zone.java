package entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mds on 11/03/15.
 * Class ${CLASS}
 */
public class Zone {

    List<Position> positions = new ArrayList<Position>();

    public Zone() {
    }

    public List<Position> getPositions() {
        return positions;
    }

    public Iterator<Position> positionIterator() {
        Iterator it = positions.iterator();

        return it;
    }

    public Iterator<Position> addPosition(Position position) {
        this.positions.add(position);
        return positionIterator();
    }
}
