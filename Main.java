package Minesweeper;

//Author: Baijun Zhu
//email: bjzhu@ucdavis.edu
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of rows we want(the number of rows should be equal to the number of columns): ");
        String numOfRows = scanner.nextLine();
        while(!numOfRows.matches("[0-9]+")) {
            System.out.println("Please enter the digit number only");
            numOfRows = scanner.nextLine();
        }
        int rows = Integer.parseInt(numOfRows);

        System.out.println("Enter the number of cols we want: ");
        String numOfCols = scanner.nextLine();
        while(!numOfCols.matches("[0-9]+")) {
            System.out.println("Please enter the digit number only");
            numOfCols = scanner.nextLine();
        }

        int cols = Integer.parseInt(numOfCols);
        System.out.println("Enter the number of bombs we want: ");
        String numOfBombs = scanner.nextLine();
        while(!numOfBombs.matches("[0-9]+")) {
            System.out.println("Please enter the digit number only");
            numOfBombs = scanner.nextLine();
        }

        int bombs = Integer.parseInt(numOfBombs);
        Gameplay play = new Gameplay(rows, cols, bombs);
        play.intialize();
        play.start();
    }
}
