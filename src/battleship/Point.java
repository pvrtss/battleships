package battleship;

record Point(int x, int y) {
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Point) obj;
        return this.x == that.x &&
                this.y == that.y;
    }

    @Override
    public String toString() {
        return "Point[" +
                "x=" + x + ", " +
                "y=" + y + ']';
    }
}
