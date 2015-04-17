package fr.m2gla.istic.projet.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baptiste on 16/04/15.
 */
public class Target {
    private List<Position> positions;
    private boolean isClose;

    public Target() {
        positions = new ArrayList<>();
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    public void addPosition(Position position) {
        this.positions.add(position);
    }

    public boolean isClose() {
        return isClose;
    }

    public void setClose(boolean isClose) {
        this.isClose = isClose;
    }
}
