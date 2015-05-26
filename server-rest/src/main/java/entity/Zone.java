package entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author mds on 11/03/15.
 * @see Zone contains List of Position which could be iterated
 */
public class Zone {

    List<Position> positions = new ArrayList<Position>();

    public Zone() {
    }

    public List<Position> getPositions() {
        return positions;
    }

    public Iterator<Position> positionIterator() {
    	return positions.iterator();
         
    }

    public Iterator<Position> addPosition(Position position) {
        this.positions.add(position);
        return positionIterator();
    }
}
