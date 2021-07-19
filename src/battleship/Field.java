package battleship;

import java.util.Scanner;

public class Field {

    private final char[][] field = new char[10][10];

    final private Ship[] ships = {
            new Ship("Aircraft Carrier", 5),
            new Ship("Battleship", 4),
            new Ship("Submarine", 3),
            new Ship("Cruiser", 3),
            new Ship("Destroyer", 2)
    };

    public Field() { // creates an empty field
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                field[i][j] = '~';
            }
        }
    }

    public String getShipName(int index) {
        return ships[index].toString();
    }

    public int getShipCells(int index) {
        return ships[index].getCells();
    }

    public void printField() {
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        for (int i = 0; i < 10; i++) {
            System.out.printf("%c", 65 + i);
            for (int j = 0; j < 10; j++) {
                System.out.printf(" %c", field[i][j]);
            }
            System.out.println();
        }
    }

    public void printFogField() {
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        for (int i = 0; i < 10; i++) {
            System.out.printf("%c", 65 + i);
            for (int j = 0; j < 10; j++) {
                if (field[i][j] != 'O') {
                    System.out.printf(" %c", field[i][j]);
                } else {
                    System.out.printf(" %c", '~');
                }
            }
            System.out.println();
        }
    }

    public void placeShip(int index) {
        System.out.printf("Enter the coordinates of the %s (%d cells):\n", getShipName(index), getShipCells(index));
        Point[] points = readCoordinatesForShip(index);
        for (int i = points[0].x(); i < points[1].x() + 1; i++) {
            for (int j = points[0].y(); j < points[1].y() + 1; j++) {
                field[i][j] = 'O';
            }
        }
        ships[index].setCoordinates(points);
    }

    public Point readShootPoint() {
        var scanner = new Scanner(System.in);
        boolean isContinue = true;
        Point p = new Point(-1, -1);
        do {
            if (scanner.hasNext()) {
                String input = scanner.nextLine().trim();
                if (input.matches("[ABCDEFGHIJ]\\d+")) {
                    p = makePoint(input);
                    if (p.y() >= 0 && p.y() <= 9) {
                        isContinue = false;
                    } else {
                        System.out.println("Error! You entered the wrong coordinates! Try again:");
                    }
                } else {
                    System.out.println("Error! Enter coordinates in appropriate way: [A-J][1-10] (e.g. C6 B5)");
                }
            } else {
                printFogField();
                System.out.println("Error! Too few arguments! Try again:");
            }
        } while (isContinue);
        return p;
    }

    public void makeShot(Point p, int player) {
        if (field[p.x()][p.y()] == 'O') {
            field[p.x()][p.y()] = 'X';
            ships[getShipIndex(p)].hit();
            System.out.println();
            if (ships[getShipIndex(p)].isDead() && !isGameEnded()) {
                System.out.println("You sank a ship!");
            } else if (isGameEnded()) {
                System.out.printf("You sank the last ship. Player %d won. Congratulations!\n", player);
            } else {
                System.out.println("You hit a ship!");

            }
        } else if (field[p.x()][p.y()] == 'X') {
            System.out.println();
            System.out.println("You hit a ship!");
        } else {
            field[p.x()][p.y()] = 'M';
            System.out.println();
            System.out.println("You missed!");
        }
    }

    public int getShipIndex(Point p) {
        int index = -1;
        for (int i = 0; i < ships.length; i++) {
            if (ships[i].isHit(p)) {
                index = i;
                break;
            }
        }
        return index;
    }

    public boolean isGameEnded() {
        boolean flag = true;
        for (Ship s : ships) {
            if (!s.isDead()) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    private static Point makePoint(String token) {
        int x = token.charAt(0) - 65;
        int y = Integer.parseInt(token.substring(1)) - 1;
        return new Point(x, y);
    }

    private static void sortPoints(Point[] p) {
        if (p[0].x() == p[1].x()) {
            if (p[0].y() > p[1].y()) {
                var tmp = p[1];
                p[1] = p[0];
                p[0] = tmp;
            }
        } else {
            if (p[0].x() > p[1].x()) {
                var tmp = p[1];
                p[1] = p[0];
                p[0] = tmp;
            }
        }
    }

    private boolean isAvailable(Point[] points) {
        // out of bounds
        if (points[0].y() < 0 || points[0].y() > 9 || points[1].y() < 0 || points[1].y() > 9) {
            throw new IllegalArgumentException("Error! Wrong ship location! Try again:");
        } else {
            // rectangle check
            // X axis range
            int startX = points[0].x() == 0 ? 0 : points[0].x() - 1;
            int endX = points[1].x() == 9 ? 9 : points[1].x() + 1;
            // Y axis range
            int startY = points[0].y() == 0 ? 0 : points[0].y() - 1;
            int endY = points[1].y() == 9 ? 9 : points[1].y() + 1;
            boolean flag = true;
            for (int i = startX; i < endX + 1 && flag; i++) {
                for (int j = startY; j < endY + 1; j++) {
                    if (this.field[i][j] != '~') {
                        flag = false;
                        break;
                    }
                }
            }
            return flag;
        }
    }

    private static boolean isLength(Ship ship, Point[] points) {
        if (points[0].x() == points[1].x()) {
            return points[1].y() - points[0].y() + 1 == ship.getCells();
        } else {
            return points[1].x() - points[0].x() + 1 == ship.getCells();
        }
    }

    private Point[] readCoordinatesForShip(int index) {
        var scanner = new Scanner(System.in);
        boolean isContinue = true;
        Point[] points = new Point[2];
        do {
            if (scanner.hasNext()) {
                String input = scanner.nextLine().trim();
                String[] tokens = input.split("\\s+");
                if (tokens.length != 2) {
                    System.out.println("Error! Wrong amount of arguments (must be exactly 2)! Try again:");
                } else if (tokens[0].matches("[ABCDEFGHIJ]\\d+") && tokens[1].matches("[ABCDEFGHIJ]\\d+")) {
                    for (int i = 0; i < 2; i++) {
                        points[i] = makePoint(tokens[i]);
                    }
                    if (points[0].x() == points[1].x() || points[0].y() == points[1].y()) {
                        sortPoints(points);
                        if (isLength(ships[index], points)) {
                            try {
                                if (isAvailable(points)) {
                                    isContinue = false;
                                } else {
                                    System.out.println("Error! You placed it too close to another one. Try again:");
                                }
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getMessage());
                            }
                        } else {
                            System.out.printf("Error! Wrong length of the %s! Try again:\n", ships[index].toString());
                        }
                    } else {
                        System.out.println("Error! Wrong ship location! Try again:");
                    }
                } else {
                    System.out.println("Error! Enter coordinates in appropriate way: [A-J][1-10] (e.g. C6 B5)");
                }
            } else {
                System.out.println("Error! Too few arguments! Try again:");
            }
        } while (isContinue);
        return points;
    }

}
