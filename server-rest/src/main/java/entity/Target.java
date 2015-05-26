package entity;

import java.util.List;

/**
 * @author alban on 16/04/15
 * @see Target contains List of Postion
 */
public class Target {
    private List<Position> positions;
    private boolean isClose;

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    public boolean isClose() {
        return isClose;
    }

    public void setClose(boolean isClose) {
        this.isClose = isClose;
    }
}
