package entity;

import java.util.List;

/**
 * Created by alban on 16/04/15.
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
