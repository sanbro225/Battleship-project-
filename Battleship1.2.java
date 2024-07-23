/** 
* @author Sanchitha Herath
* @date 01/07/24
* @verstion 1.2
* My code is a game based on battleship.
* This game is a two player game, which means it evolves two players to play this game in this game i used two players as computer and human. 

*/
import java.util.Random;//Keyboard scanner
import java.util.Scanner;

public class Battleship {
    public static final int SIZE = 10; // Adjust the board size if needed
    public static final int[] SHIP_SIZES = {2, 3, 3, 4, 5}; // Different ship sizes

    public static void main(String[] args) {
        char[][] playerBoard = new char[SIZE][SIZE];
        char[][] computerBoard = new char[SIZE][SIZE];
        boolean[][] playerShips = new boolean[SIZE][SIZE];
        boolean[][] computerShips = new boolean[SIZE][SIZE];

        initializeBoard(playerBoard);
        initializeBoard(computerBoard);

        Scanner scanner = new Scanner(System.in);

        // Player places ships
        System.out.println("Place your ships using WASD keys and enter key (W: Up, A: Left, S: Down, D: Right, T: Place):");
        placePlayerShips(playerShips, playerBoard, scanner);

        // Computer places ships
        placeShipsRandomly(computerShips, SHIP_SIZES);

        int playerShipsRemaining = SHIP_SIZES.length;
        int computerShipsRemaining = SHIP_SIZES.length;

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
        for (int size : SHIP_SIZES) {
            boolean placed = false;
            while (!placed) {
                int currentRow = 0;
                int currentCol = 0;
                boolean horizontal = true;
                boolean valid = false;
                while (!valid) {
                    board[currentRow][currentCol] = 'P'; // 'P' for the current position
                    printBoard(board);

                    System.out.println("Place ship of size " + size + ". Use WASD to move, T to place, R to rotate:");
                    char move = scanner.next().charAt(0);
                    board[currentRow][currentCol] = '~';

                    switch (move) {
                        case 'w': currentRow = Math.max(0, currentRow - 1); break;
                        case 'a': currentCol = Math.max(0, currentCol - 1); break;
                        case 's': currentRow = Math.min(SIZE - 1, currentRow + 1); break;
                        case 'd': currentCol = Math.min(SIZE - 1, currentCol + 1); break;
                        case 'r': horizontal = !horizontal; break;
                        case 't': // Enter key
                        case ' ': // Space key for convenience
                            valid = isValidPlacement(ships, currentRow, currentCol, size, horizontal);
                            if (valid) {
                                placeShip(ships, board, currentRow, currentCol, size, horizontal);
                                placed = true;
                            } else {
                                System.out.println("Invalid placement. Try again.");
                            }
                            break;
                        default:
                            System.out.println("Invalid move. Use WASD to move, T to place, R to rotate.");
                            break;
                    }
                }
            }
        }
    }

    public static void placeShipsRandomly(boolean[][] ships, int[] shipSizes) {
        Random rand = new Random();
        for (int size : shipSizes) {
            boolean placed = false;
            while (!placed) {
                int row = rand.nextInt(SIZE);
                int col = rand.nextInt(SIZE);
                boolean horizontal = rand.nextBoolean();
                if (isValidPlacement(ships, row, col, size, horizontal)) {
                    placeShip(ships, null, row, col, size, horizontal);
                    placed = true;
                }
            }
        }
    }

    public static boolean isValidPlacement(boolean[][] ships, int row, int col, int size, boolean horizontal) {
        if (horizontal) {
            if (col + size > SIZE) return false;
            for (int i = col; i < col + size; i++) {
                if (ships[row][i]) return false;
            }
        } else {
            if (row + size > SIZE) return false;
            for (int i = row; i < row + size; i++) {
                if (ships[i][col]) return false;
            }
        }
        return true;
    }

    public static void placeShip(boolean[][] ships, char[][] board, int row, int col, int size, boolean horizontal) {
        if (horizontal) {
            for (int i = col; i < col + size; i++) {
                ships[row][i] = true;
                if (board != null) board[row][i] = 'S';
            }
        } else {
            for (int i = row; i < row + size; i++) {
                ships[i][col] = true;
                if (board != null) board[i][col] = 'S';
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