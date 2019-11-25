package Minesweeper;

import java.util.Scanner;

//Initilize the board and start the game
public class Gameplay {
    //Show all the possible state
    public enum GameState {
        WON, LOST, RUNNING
    }

    private Board board;
    private int rows;
    private int columns;
    private int bombs;
    private GameState state;
    private String result;

    //Set the initial state as RUNNING
    public Gameplay(int rows, int columns, int bombs) {
        this.rows = rows;
        this.columns = columns;
        this.bombs = bombs;
        state = GameState.RUNNING;
    }

    public boolean intialize() {
        if(board == null) {
            board = new Board(rows, columns, bombs);
            return true;
        } else {
            System.out.println("Game has already been initialized");
            return false;
        }
    }

    public boolean start() {
        if(board == null) {
            intialize();
        }
        return playGame();
    }

    //Printout the result or number of cell and bombs remaining and also printout the board as player view if the state is RUNNING
    public void printGameState() {
        if(state == GameState.LOST) {
            board.printBoard(true);
            result = "LOST";
            System.out.println("LOST");
        } else if (state == GameState.WON) {
            board.printBoard(true);
            result = "WIN";
            System.out.println("WIN");
        } else {
            board.printBoard(false);
            System.out.println("Number remaining: " + board.getNumOfRemaining());
            System.out.println("Has covered bombs number: " + board.getNumOfCovered());
        }
    }

    //Actually playing the game to let player to pick the row and column and define
    // whether they want to flag the cell or expose the cell
    public boolean playGame() {
        Scanner scanner = new Scanner(System.in);
        printGameState();

        while(state == GameState.RUNNING) {
            System.out.println("Please enter the row and col within the board range separate by space such as '0 0': ");
            System.out.println("If you want to mark the uncover cell please put an 'M' in the front of the " +
                    "row number such as M0 0: ");
            String input = scanner.nextLine();
            if(input.equals("exit")) {
                scanner.close();
                return false;
            }

            //Parse the input and store the decision to the Player class
            Player player = Player.fromString(input);
            if(player == null) {
                continue;
            }
            //According to the player state to put action on the board to change the board state
            PlayerResult result = board.playAction(player);

            if(result.successfulPick()) {
                state = result.getState();
            } else {
                System.out.println("Could not pick this cell (" + player.getRow() + "," + player.getColumn() + ").");
            }
            //Printout the currenting state after one pick from the player and print the board from player point of view
            printGameState();
        }

        scanner.close();
        return true;
    }
    public GameState getState() {
        return state;
    }
    public void setState(GameState state) {
        this.state = state;
    }
    public Board getBoard() {
        return board;
    }
    public void setBoard(Board board) {
        this.board = board;
    }
    public String getResult() {
        return result;
    }
}
