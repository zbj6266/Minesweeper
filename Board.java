package Minesweeper;

import Minesweeper.Gameplay.GameState;

import java.util.*;

//Initialize the cells and put the random bombs in the board
// and also play action to change the state base on the player's decision
public class Board {
    private int rows;
    private int columns;
    private int numberOfBombs = 0;
    private Cell[][] cells;
    private int numOfRemaining;
    private int numOfCovered;
    //Visited is to store the bomb location after we random place our bombs
    private boolean[][] visited;
    private int[] directionX = {0, 0, 1, -1, 1, 1, -1, -1};
    private int[] directionY = {1, -1, 0, 0, 1, -1, 1, -1};

    public Board(int rows, int columns, int numberOfBombs) {
        this.rows = rows;
        this.columns = columns;
        this.numberOfBombs = numberOfBombs;
        intializeBoard();
        setNumberedCells();
        numOfRemaining = rows * columns;
        numOfCovered = 0;

    }

    //Setup the cells and place the random bombs
    private void intializeBoard() {
        cells = new Cell[rows][columns];
        Random random = new Random();
        visited = new boolean[rows][columns];

        for(int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                cells[i][j] = new Cell(i, j);

            }
        }
        for(int i = 0; i < numberOfBombs; i++) {
            int row = random.nextInt(rows);

            int col = random.nextInt(columns);
            while(visited[row][col]) {
                row = random.nextInt(rows);
                col = random.nextInt(columns);
            }

            visited[row][col] = true;
            cells[row][col].setBomb(true);

        }
    }

    //Set the number of this cell according to the number of surrounding bombs around from 8-direction
    private void setNumberedCells() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (cells[i][j].isBomb()) {
                    for (int z = 0; z < 8; z++) {
                        int newR = i + directionX[z];
                        int newC = j + directionY[z];
                        if (isInBound(newR, newC)) {
                            cells[newR][newC].incrementNumber();
                        }
                    }
                }
            }
        }
    }

    //Check our pick is on the board range or not
    private boolean isInBound(int x, int y) {
        if(x < 0 || x >= cells.length || y < 0 || y >= cells[0].length) {
            return false;
        }
        return true;
    }

    //Play the action to see if our pick is trying to cover the cell or pick the cell
    // and change the player result according to that
    public PlayerResult playAction(Player player) {
        Cell cell = getCell(player);
        if(cell == null) {
            return new PlayerResult(false, GameState.RUNNING);
        }
        if(player.isCoverOrNot()) {
            boolean result = cell.toggleDecisionToCoverOrNot();
            if(visited[cell.getRow()][cell.getColumn()]) {
                visited[cell.getRow()][cell.getColumn()] = false;
            }
            numOfCovered++;
            numOfRemaining--;
            if(numOfRemaining == 0 && numberOfBombs == numOfCovered) {

                return new PlayerResult(result, GameState.WON);
            }
            //This happens when we place wrong flag or too many flag on the board,
            // we will warn the player and choose to change the extra covered cells state
            else if(numOfRemaining == 0){
                System.out.println();
                System.out.print("You input wrong flag which didn't cover the mine");
                numOfRemaining++;
                return new PlayerResult(result, GameState.RUNNING);
            }
            return new PlayerResult(result, GameState.RUNNING);

        }

        boolean result = checkCell(cell);

        if(cell.isBomb()) {
            return new PlayerResult(result, GameState.LOST);
        }

        //This happen after player has change the covered cell state and place few flags
        if(numOfRemaining == 0 && cell.isCoverOrNot()) {
            numOfCovered--;

        }

        //After player click on the empty cell we will also
        //check if this cell surrounding 8-direction has empty cells and expose them too
        //Until we find all numbered cells which are around and embrace all these empty cells
        if(cell.isEmpty() && !cell.isCoverOrNot()) {
            expandRegion(cell);
        }


        //The player won only if they put the exactly position and number of flags to cover all the bombs
        // and exposed all the cells
        if(numOfRemaining == 0 && numberOfBombs == numOfCovered) {

            return new PlayerResult(result, GameState.WON);
        } else if(numOfRemaining == 0 && numberOfBombs != numOfCovered){
            System.out.println();
            System.out.print("You input wrong flag which didn't cover the mine");
            numOfRemaining++;
        }

        return new PlayerResult(result, GameState.RUNNING);
    }


    //I use queue to find the next empty cell and loop it
    private void expandRegion(Cell cell) {

        Queue<Cell> queue = new LinkedList<>();
        queue.offer(cell);
        while(!queue.isEmpty()) {
            int size = queue.size();
            for(int i = 0; i < size; i++) {
                Cell temp = queue.poll();
                int row = temp.getRow();
                int col = temp.getColumn();
                for(int j = 0; j < 8; j++) {
                    int newRow = row + directionX[j];
                    int newCol = col + directionY[j];
                    if(!isInBound(newRow, newCol) ||
                            cells[newRow][newCol].isExposed() || cells[newRow][newCol].isCoverOrNot()) {
                        continue;

                    }
                    if(cells[newRow][newCol].isEmpty()) {
                        cells[newRow][newCol].checked();
                        numOfRemaining--;
                        queue.offer(cells[newRow][newCol]);
                    } else if(!cells[newRow][newCol].isEmpty()) {
                        cells[newRow][newCol].checked();
                        numOfRemaining--;
                    }
                }
            }
        }
    }

    //Print the board and show real or player version board during different conditons
    public void printBoard(boolean showRealBoard) {
        System.out.println();
        System.out.print("   ");
        for(int i = 0; i < columns; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        for(int i = 0; i < columns; i++) {
            System.out.print("--");
        }
        System.out.println();
        for(int r = 0; r < rows; r++) {
            System.out.print(r + "| ");
            for(int c = 0; c < columns; c++) {
                if(showRealBoard) {
                    System.out.print(cells[r][c].realBoardState());
                } else {
                    System.out.print(cells[r][c].playerVersionBoardState());
                }
            }
            System.out.println();
        }
    }

    //Check the cell if it hasn't been exposed and then change the state of that cell to exposed and
    // decreasing the total number of cells unexposed
    private boolean checkCell(Cell cell) {
        if(!cell.isExposed()) {
            cell.checked();
            numOfRemaining--;
            return true;
        }
        return false;
    }

    public Cell getCell(Player player) {
        int row = player.getRow();
        int col = player.getColumn();
        if(!isInBound(row, col)) {
            return null;
        }
        return cells[row][col];
    }

    public Cell[][] getCells() {
        return cells;
    }

    public int getNumOfRemaining() {
        return numOfRemaining;
    }
    public int getNumOfCovered() {
        return numOfCovered;
    }

}
