package Minesweeper;

import Minesweeper.Gameplay.GameState;
import java.util.*;
public class Solver {
    private static Random random = new Random();
    private static int[] directionX = {0, 0, 1, -1, 1, 1, -1, -1};
    private static int[] directionY = {1, -1, 0, 0, 1, -1, 1, -1};
    //Create a lowestUnknown to minize the risk of click on the bomb if there is uncertainy of the unknown cell
    //countFlags is to count the number of covered cell we have used
    //countNonflagsClick is the number of the exposed cells without the covered cell number
    private static int row, column, temp, lowestUnknown, lowestRow, lowestCol, lowestFlag, lowestCenterNumber, countFlags,
            countNonflagsClick, countWins;
    private static boolean flag;
    public static void main(String[] args) {

        //Count the starting time
        long startTime = System.currentTimeMillis();

        countWins = 0;
        //Start the simulation for 100,000 times
        simulate();
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        //The number of puzzles successfully solved out of 100000 attempts: 74576
        //The total time to complete the 100000 attempts is : 996376
        System.out.println("The number of puzzles successfully solved out of 100000 attempts: " + countWins);
        System.out.println("The total time to complete the 100000 attempts is: "+ elapsedTime + " in milliseconds");
    }
    private static void simulate() {
        for(int i = 0; i < 100000; i++) {
            //For each loop we will initilize the board again and set the countFlags and countNonflagsClick back to zero
            Gameplay play = new Gameplay(10, 10, 10);
            countFlags = 0;
            countNonflagsClick = 0;
            play.setBoard(null);
            play.intialize();
            Board board = play.getBoard();
            play.printGameState();
            //Visited store the exposed cells location to avoid the duplicate
            boolean[][] visited = new boolean[10][10];
            //First pick we will just random pick an valid location on the board
            row = random.nextInt(10);
            column = random.nextInt(10);
            while(play.getState() == GameState.RUNNING) {
                //flag is to determine whether this pick is cover the cell or expose the cell
                flag = true;
                Cell[][] cells = board.getCells();
                if(countNonflagsClick == 0) {
                    //So first pick we will expose the cell
                    countNonflagsClick++;
                    flag = false;
                }
                else {
                    //We have to make sure that we have enough flag cell equal to the number of bombs
                    if(countNonflagsClick < 90) {
                        temp = 0;
                        lowestUnknown = 100;
                        lowestRow = 9;
                        lowestCol = 9;
                        lowestCenterNumber = 8;
                        lowestFlag = 8;

                        //Use double loop to find the exposed cell with positive center number
                        // so that we can choose if we want to cover the cell
                        // if the number of unknown cells plus the flag cells equal to cells and this center number
                        //Such as ? X 1
                        //        1 2 1
                        //        . . .
                        //Or the flag cells number equal to this center number
                        //Such as ? X 1
                        //        1 1 1
                        //        . . .
                        for (int r = 0; r < cells.length; r++) {
                            for (int c = 0; c < cells[0].length; c++) {
                                if (cells[r][c].isExposed() && cells[r][c].getNumber() >= 1) {
                                    //This function is to determine whether we should expose the cell or cover it
                                    coverOrClick(cells,
                                                r,
                                                c);
                                    //Once we made either of these decision we will immediately exit the loop
                                    if(temp == 1) {
                                        break;
                                    }
                                }
                            }
                            if (temp == 1) {
                                break;
                            }
                        }

                        //If there was not 100% sure we should flag or expose it, we will calculate
                        // the probability of picking the around bomb and random pick on the whole board
                        if(temp == 0) {
                            //We choose the lowestCenterNumber is because to lower the risk of picking an bomb on the unknown cell
                            double probabilityAroundPick = (double) (lowestCenterNumber - lowestFlag) / (lowestUnknown);
                            double probabilityRandomPick = (double)(10 - countFlags) / (100 - countNonflagsClick - countFlags);
                            //There is an edge case that when there is unKnown cells on the corner and surrounded by flaged cells
                            //We can't detect with above method because there is no number that can reach this cell,
                            // and our probabilityAroundPick will be 0 so that we have to random pick this cell location to expose it
                            //Such as ? X 2
                            //        X X 2
                            //        2 2 1
                            if(probabilityAroundPick >= probabilityRandomPick || probabilityAroundPick == 0) {
                                row = random.nextInt(10);
                                column = random.nextInt(10);
                                while(visited[row][column]) {
                                    row = random.nextInt(10);
                                    column = random.nextInt(10);
                                }
                            } else {
                                row = lowestRow;
                                column = lowestCol;
                            }


                            countNonflagsClick++;
                            flag = false;


                        }

                    }
                    //If there is 90 exposed cells without any covered cell,
                    // we will mark the rest of the cells to be covered cells
                    else {
                        row = random.nextInt(10);
                        column = random.nextInt(10);
                        while(visited[row][column]) {
                            row = random.nextInt(10);
                            column = random.nextInt(10);
                        }
                        countFlags++;
                    }
                }

                //Make this cell location to be true
                visited[row][column] = true;

                //Create the player and store the result into this class
                Player player = new Player(row, column, flag);
                if (player == null) {
                    continue;
                }
                //Put the action to change the cells state based on the player's input
                PlayerResult result = play.getBoard().playAction(player);

                //Show if we pick the unexposed cell
                if (result.successfulPick()) {
                    play.setState(result.getState());
                } else {
                    System.out.println("Could not pick this cell (" + player.getRow() + "," + player.getColumn() + ").");
                }

                //Print the current board state in player version
                play.printGameState();
            }
            if(play.getResult().equals("WIN")) {
                countWins++;
            }
        }
    }
    private static void coverOrClick(Cell[][] cells,
                                     int curR,
                                     int curC
                                     ) {
        int number = cells[curR][curC].getNumber();
        int tempFlags = 0;
        int unKnown = 0;
        for(int m = 0; m < 8; m++) {
            int newRow = curR + directionX[m];
            int newCol = curC + directionY[m];
            if(newRow < 0 || newRow >= cells.length || newCol < 0 || newCol >= cells[0].length) {
                continue;
            }
            if(!cells[newRow][newCol].isExposed() && !cells[newRow][newCol].isCoverOrNot()) {
                row = newRow;
                column = newCol;

                unKnown++;
            } else if(cells[newRow][newCol].isCoverOrNot()) {
                tempFlags++;
            }

        }
        if(unKnown == 0) {
            return;
        }
        if(lowestUnknown > unKnown) {
            lowestRow = row;
            lowestCol = column;
            lowestUnknown = unKnown;
            lowestCenterNumber = number;
            lowestFlag = tempFlags;
        }

        if((unKnown + tempFlags) == number) {
            temp = 1;
            countFlags++;

        }else if(tempFlags == number){
            temp = 1;
            countNonflagsClick++;
            flag = false;

        }
    }


}
