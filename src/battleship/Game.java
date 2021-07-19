package battleship;

import java.util.*;

public class Game {
    private static int player = 1; // 1 - first player, 2 - second player
    private static final Field field1 = new Field();
    private static final Field field2 = new Field();

    public static void main(String[] args) {
        placeShips(field1);
        passMove();
        placeShips(field2);
        passMove();
        System.out.println();
        System.out.println("The game starts!");
        System.out.println();
        while (true) {
            printTurnField();
            makeMove();
            if (!isGameEnded()) {
                passMove();
            } else {
                break;
            }
        }
        System.out.println();
    }

    private static boolean isGameEnded() {
        return field1.isGameEnded() || field2.isGameEnded();
    }

    private static void makeMove() {
        if (player == 1) {
            Point p = field2.readShootPoint();
            field2.makeShot(p, player);
        } else {
            Point p = field1.readShootPoint();
            field1.makeShot(p, player);
        }
    }

    private static void placeShips(Field field) {
        System.out.printf("Player %d, place your ships to the game field\n", player);
        System.out.println();
        field.printField();
        System.out.println();
        for (int i = 0; i < 5; ++i) {
            field.placeShip(i);
            System.out.println();
            field.printField();
            System.out.println();
        }
    }

    private static void printTurnField() {
        if (player == 1) {
            field2.printFogField();
            System.out.println("---------------------");
            field1.printField();
        } else {
            field1.printFogField();
            System.out.println("---------------------");
            field2.printField();
        }
        System.out.println();
        System.out.printf("Player %d, it's your turn:\n", player);
    }

    private static void passMove() {
        System.out.println("Press Enter and pass the move to another player");
        var sc = new Scanner(System.in);
        sc.nextLine();
        player = player == 1 ? 2 : 1;
    }
}
