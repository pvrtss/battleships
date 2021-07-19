package battleship;

import java.util.Arrays;

public class Ship {
    String name;
    int cells;
    int hp;
    Point[] coordinates;

    public Ship(String name, int cells) {
        this.name = name;
        this.cells = cells;
        this.hp = cells;
    }

    public boolean isHit(Point p) {
        int startX = coordinates[0].x() == 0 ? 0 : coordinates[0].x() - 1;
        int endX = coordinates[1].x() == 9 ? 9 : coordinates[1].x() + 1;
        int startY = coordinates[0].y() == 0 ? 0 : coordinates[0].y() - 1;
        int endY = coordinates[1].y() == 9 ? 9 : coordinates[1].y() + 1;

        return p.x() >= startX && p.x() <= endX && p.y() >= startY && p.y() <= endY;
    }


    @Override
    public String toString() {
        return this.name;
    }

    public int getCells() {
        return this.cells;
    }

    public void hit() {
        --this.hp;
    }

    public void setCoordinates(Point[] coordinates) {
        if (coordinates == null) {
            this.coordinates = new Point[0];
        } else {
            this.coordinates = Arrays.copyOf(coordinates, coordinates.length);
        }
    }

    public boolean isDead() {
        return !(hp > 0);
    }
}
