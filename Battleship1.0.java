public class Battleship {
    public static final int SIZE = 9;
    public static final int NUMBER_OF_SHIPS = 4;
    public static final int SHIP_SIZE = 3; // Define the size of the ships

    public static void main(String[] args) {
        char[][] playerBoard = new char[SIZE][SIZE];
        char[][] computerBoard = new char[SIZE][SIZE];
        boolean[][] playerShips = new boolean[SIZE][SIZE];
        boolean[][] computerShips = new boolean[SIZE][SIZE];

        initializeBoard(playerBoard);
        initializeBoard(computerBoard);

        Scanner scanner = new Scanner(System.in);

        // Player places ships
        System.out.println("Place your ships using WASD keys and T to Place the ships (W: Up, A: Left, S: Down, D: Right, T: Place):");
        placePlayerShips(playerShips, playerBoard, scanner);

        // Computer places ships
        placeShipsRandomly(computerShips, NUMBER_OF_SHIPS);

        int playerShipsRemaining = NUMBER_OF_SHIPS * SHIP_SIZE;
        int computerShipsRemaining = NUMBER_OF_SHIPS * SHIP_SIZE;

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
                System.out.println("Hit! " + computerShipsRemaining + " parts of ships remaining.");
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
                System.out.println("Computer hit! " + playerShipsRemaining + " parts of your ships remaining.");
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

        while (placedShips < NUMBER_OF_SHIPS) {
            int currentRow = 0;
            int currentCol = 0;
            boolean placed = false;
            while (!placed) {
                boolean validPlacement = true;

                System.out.println("Placing ship " + (placedShips + 1) + " of size " + SHIP_SIZE);
                System.out.println("Choose direction (W: Up, A: Left, S: Down, D: Right) to place from (" + currentRow + ", " + currentCol + "): ");
                char direction = scanner.next().charAt(0);

                int endRow = currentRow;
                int endCol = currentCol;

                switch (direction) {
                    case 'w': endRow = currentRow - SHIP_SIZE + 1; break;
                    case 'a': endCol = currentCol - SHIP_SIZE + 1; break;
                    case 's': endRow = currentRow + SHIP_SIZE - 1; break;
                    case 'd': endCol = currentCol + SHIP_SIZE - 1; break;
                    default:
                        System.out.println("Invalid direction. Use WASD to move.");
                        continue;
                }

                if (endRow < 0 || endRow >= SIZE || endCol < 0 || endCol >= SIZE) {
                    System.out.println("Invalid placement. Ship goes out of bounds.");
                    continue;
                }

                for (int i = 0; i < SHIP_SIZE; i++) {
                    int r = currentRow;
                    int c = currentCol;
                    switch (direction) {
                        case 'w': r -= i; break;
                        case 'a': c -= i; break;
                        case 's': r += i; break;
                        case 'd': c += i; break;
                    }
                    if (r < 0 || r >= SIZE || c < 0 || c >= SIZE || ships[r][c]) {
                        validPlacement = false;
                        break;
                    }
                }

                if (validPlacement) {
                    for (int i = 0; i < SHIP_SIZE; i++) {
                        int r = currentRow;
                        int c = currentCol;
                        switch (direction) {
                            case 'w': r -= i; break;
                            case 'a': c -= i; break;
                            case 's': r += i; break;
                            case 'd': c += i; break;
                        }
                        ships[r][c] = true;
                        board[r][c] = 'S';
                    }
                    placed = true;
                    placedShips++;
                    printBoard(board);
                } else {
                    System.out.println("Invalid placement. Overlapping with another ship or out of bounds.");
                }
            }
        }
    }

    public static void placeShipsRandomly(boolean[][] ships, int numberOfShips) {
        Random rand = new Random();
        int placedShips = 0;

        while (placedShips < numberOfShips) {
            boolean placed = false;
            while (!placed) {
                int row = rand.nextInt(SIZE);
                int col = rand.nextInt(SIZE);
                int direction = rand.nextInt(4); // 0: up, 1: left, 2: down, 3: right
                int endRow = row;
                int endCol = col;
                boolean validPlacement = true;

                switch (direction) {
                    case 0: endRow = row - SHIP_SIZE + 1; break;
                    case 1: endCol = col - SHIP_SIZE + 1; break;
                    case 2: endRow = row + SHIP_SIZE - 1; break;
                    case 3: endCol = col + SHIP_SIZE - 1; break;
                }

                if (endRow < 0 || endRow >= SIZE || endCol < 0 || endCol >= SIZE) {
                    validPlacement = false;
                } else {
                    for (int i = 0; i < SHIP_SIZE; i++) {
                        int r = row;
                        int c = col;
                        switch (direction) {
                            case 0: r -= i; break;
                            case 1: c -= i; break;
                            case 2: r += i; break;
                            case 3: c += i; break;
                        }
                        if (r < 0 || r >= SIZE || c < 0 || c >= SIZE || ships[r][c]) {
                            validPlacement = false;
                            break;
                        }
                    }
                }

                if (validPlacement) {
                    for (int i = 0; i < SHIP_SIZE; i++) {
                        int r = row;
                        int c = col;
                        switch (direction) {
                            case 0: r -= i; break;
                            case 1: c -= i; break;
                            case 2: r += i; break;
                            case 3: c += i; break;
                        }
                        ships[r][c] = true;
                    }
                    placed = true;
                    placedShips++;
                }
            }
        }
    }

    public static void printBoard(char[][] board) {
        System.out.print(" ");
        for (int i = 0; i < SIZE; i++) {
            System.out.print(i + " ");
        }
        System.out
