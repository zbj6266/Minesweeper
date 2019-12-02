package Minesweeper;

//Store the player decision from each pick whether they want to expose or cover the cell
public class Player {
    private int row;
    private int column;
    private boolean isCoverOrNot;

    public Player(int row, int column, boolean isCoverOrNot) {
        setRow(row);
        setColumn(column);
        this.isCoverOrNot = isCoverOrNot;
    }
    //Parse the input if there is an M in the front we should mark this cell has covered
    public static Player fromString(String input) {
        boolean isCoverOrNot = false;

        if(input.length() > 0 && input.charAt(0) == 'M') {
            isCoverOrNot = true;
            input = input.substring(1);
        }
        if(!input.matches("\\d* \\d+")) {
            return null;
        }
        String[] parts = input.split(" ");
        //Store the row and col information to the player
        try{
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);
            return new Player(row, col, isCoverOrNot);

        } catch (NumberFormatException e) {
            return null;
        }


    }
    public boolean isCoverOrNot() {
        return isCoverOrNot;
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

}
