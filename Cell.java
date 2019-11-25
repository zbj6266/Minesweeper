package Minesweeper;


//Identify each cell state and locations
public class Cell {
    private int row;
    private int column;
    private boolean isBomb;
    //Number is the number of current cell 8-directional surrounding bombs' total number
    private int number;
    //isExposed means that if we have already clicked on that cell
    private boolean isExposed = false;
    //isCoverorNot means that if this cell is a covered cell or not
    private boolean isCoverOrNot = false;
    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
        isBomb = false;
        number = 0;
    }
    public void setRow(int r) {
        row = r;
    }
    public int getRow() {
        return row;
    }
    public void setColumn(int c) {
        column = c;
    }
    public int getColumn() {
        return column;
    }

    public void setBomb(boolean bomb) {
        isBomb = bomb;
        number = -1;
    }
    public boolean isBomb() {
        return isBomb;
    }

    //Identify if it's a empty cell
    public boolean isEmpty() {
        return number == 0;
    }

    public void incrementNumber() {
        number++;
    }
    public boolean isExposed() {
        return isExposed;
    }
    public boolean isCoverOrNot() {
        return isCoverOrNot;
    }
    //check this cell has just been clicked
    public boolean checked() {
        isExposed = true;
        return !isBomb;
    }
    //Trigger the cover cell function to mark this cell as a flag and change the state isCoverOrNot to be true
    public boolean toggleDecisionToCoverOrNot() {
        if(!isExposed) {
            isCoverOrNot = !isCoverOrNot;
        }
        return isCoverOrNot;
    }

    //The version of the player point of view of the board
    public String playerVersionBoardState() {
        if(isExposed) {
            return realBoardState();
        } else if(isCoverOrNot) {
            return "X ";
        } else {
            return "? ";
        }
    }
    public int getNumber() {
        return number;
    }
    //The version will show the final result after the player has won or lost
    public String realBoardState() {
        if(isBomb) {
            return "M ";
        } else if(number > 0) {
            return String.valueOf(number) + " ";
        }else {
            return ". ";
        }
    }
}
