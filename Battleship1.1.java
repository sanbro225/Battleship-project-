import java.util.Random;
import java.util.Scanner;

public class Battleship {
    public static final int SIZE = 5;
    public static final int NUMBER_OF_SHIPS = 3;

    public static void main(String[] args) {
        char[][] playerBoard = new char[SIZE][SIZE];
        char[][] computerBoard = new char[SIZE][SIZE];
        boolean[][] playerShips = new boolean[SIZE][SIZE];
        boolean[][] computerShips = new boolean[SIZE][SIZE];

        initializeBoard(playerBoard);
        initializeBoard(computerBoard);

        Scanner scanner = new Scanner(System.in);

        // Player places ships
        System.out.println("Place your ships using WASD keys and enter key (W: Up, A: Left, S: Down, D: Right, Enter: Place):");
        placePlayerShips(playerShips, playerBoard, scanner);

        // Computer places ships
        placeShipsRandomly(computerShips, NUMBER_OF_SHIPS);

        int playerShipsRemaining = NUMBER_OF_SHIPS;
        int computerShipsRemaining = NUMBER_OF_SHIPS;

        while (playerShipsRemaining > 0 && computerShipsRemaining > 0) {
            // Player's turn
            System.out.println("Your Board:");
            printBoard(playerBoard);
            System.out.println("Computer's Board:");
            printBoardHidden(computerBoard);

            System.out.println("Enter your guess (row and column): ");
            int row = scanner.nextInt();
            int col = scanner.nextInt();

            if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
                System.out.println("Invalid input. Try again.");
                continue;
            }

            if (computerShips[row][col]) {
                computerBoard[row][col] = 'X';
                computerShips[row][col] = false;
                computerShipsRemaining--;
                System.out.println("Hit! " + computerShipsRemaining + " ships remaining.");
            } else {
                computerBoard[row][col] = 'O';
                System.out.println("Miss.");
            }

            // Computer's turn
            Random rand = new Random();
            int compRow, compCol;
            do {
                compRow = rand.nextInt(SIZE);
                compCol = rand.nextInt(SIZE);
            } while (playerBoard[compRow][compCol] == 'X' || playerBoard[compRow][compCol] == 'O');

            System.out.println("Computer guesses: " + compRow + " " + compCol);
            if (playerShips[compRow][compCol]) {
                playerBoard[compRow][compCol] = 'X';
                playerShips[compRow][compCol] = false;
                playerShipsRemaining--;
                System.out.println("Computer hit! " + playerShipsRemaining + " of your ships remaining.");
            } else {
                playerBoard[compRow][compCol] = 'O';
                System.out.println("Computer miss.");
            }
        }

        if (playerShipsRemaining == 0) {
            System.out.println("You lost. Better luck next time!");
        } else {
            System.out.println("Congratulations! You sunk all the computer's ships!");
        }

        scanner.close();
    }

    public static void initializeBoard(char[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = '~';
            }
        }
    }

    public static void placePlayerShips(boolean[][] ships, char[][] board, Scanner scanner) {
        int placedShips = 0;
        int currentRow = 0;
        int currentCol = 0;

        while (placedShips < NUMBER_OF_SHIPS) {
            board[currentRow][currentCol] = 'P'; // 'P' for the current position
            printBoard(board);

            System.out.println("Place ship " + (placedShips + 1) + ". Use WASD to move, Enter to place:");
            char move = scanner.next().charAt(0);
            board[currentRow][currentCol] = '~';

            switch (move) {
                case 'w': currentRow = Math.max(0, currentRow - 1); break;
                case 'a': currentCol = Math.max(0, currentCol - 1); break;
                case 's': currentRow = Math.min(SIZE - 1, currentRow + 1); break;
                case 'd': currentCol = Math.min(SIZE - 1, currentCol + 1); break;
                case '\n': // Enter key
                case '\r': // Enter key on some systems
                case ' ': // Space key for convenience
                    if (!ships[currentRow][currentCol]) {
                        ships[currentRow][currentCol] = true;
                        board[currentRow][currentCol] = 'S'; // 'S' for ship
                        placedShips++;
                    } else {
                        System.out.println("There is already a ship here. Try again.");
                    }
                    break;
                default:
                    System.out.println("Invalid move. Use WASD to move, Enter to place.");
                    break;
            }
        }

        // Clear the cursor after placing all ships
        board[currentRow][currentCol] = '~';
        printBoard(board);
    }

    public static void placeShipsRandomly(boolean[][] ships, int numberOfShips) {
        Random rand = new Random();
        int placedShips = 0;
        while (placedShips < numberOfShips) {
            int row = rand.nextInt(SIZE);
            int col = rand.nextInt(SIZE);
            if (!ships[row][col]) {
                ships[row][col] = true;
                placedShips++;
            }
        }
    }

    public static void printBoard(char[][] board) {
        System.out.print("  ");
        for (int i = 0; i < SIZE; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        for (int i = 0; i < SIZE; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void printBoardHidden(char[][] board) {
        System.out.print("  ");
        for (int i = 0; i < SIZE; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        for (int i = 0; i < SIZE; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == 'X' || board[i][j] == 'O') {
                    System.out.print(board[i][j] + " ");
                } else {
                    System.out.print("~ ");
                }
            }
            System.out.println();
        }
    }
}
