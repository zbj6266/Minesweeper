# Minesweeper
Setup and Run the code Steps:
First to git clone the code https://github.com/zbj6266/Minesweeper.git
Second go to the src folder cd Minesweeper/Minesweeper/src
Third javac Minesweeper/Main.java
Fourth java Minesweeper/Main
This will run the code and please do the same step three and step four for Solver.java:
javac Minesweeper/Solver.java
java Minesweeper/Solver


First Step: Please download the whole file and import into the java IDE as an project.

Second Step: Please run the Main.java file under src/Minesweeper file
On the terminal it will show the context "Enter the number of rows we want(the number of rows should be equal to the number of columns): "
So enter the number of rows we would like and press Enter

Then on the terminal it will show the context "Enter the number of cols we want: "
So enter the number of cols we would like and press Enter

Then on the terminal it will show the context "Enter the number of bombs we want: "
So enter the number of mines we would like and press Enter

And then it will automatically printout the initial board on the terminal.

After that we should enter the coordinate within the rows and cols range. It will also show the guidance on the terminal 
"Please enter the row and col within the board range separate by space such as '0 0': 
If you want to mark the uncover cell please put an 'M' in the front of the row number such as M0 0: "

so we should enter an valid row by col seperated by a space like 0 0, if we want to flag the uncover cell we put big M in the front to
indicate that cell as a mine. 

If we enter an invalid coordinates or unrecognize format we will ask the player to re-enter it again with the guidance and examples until we receive an valid input.

It will also printout the updated board after every time we enter an valid input on the terminal.

Third Step: After testing with the Main.java, please go to Solver.java to run the auto-generate 100,000 times with 10*10 board with 10 mines 
and will print the final result about the winning times and time cost on the terminal
