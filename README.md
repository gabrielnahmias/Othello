<h1>Othello</h1>
---

**Othello** (a.k.a., *Reversi*) is a game based on a grid with eight rows and eight columns played between you and the computer by adding pieces of two colors: black and white. At the beginning of the game, there are 4 pieces on the grid—the player with the black pieces is the first one to place his piece on the board. Each player must place a piece in a position such that there exists at least one straight (horizontal, vertical, or diagonal) line between the new piece and another piece of the same color. There must also be one or more contiguous opposite pieces between them.

##Other Information

###The Game

When you start **Othello**, you must make the first move (as the black player), then the computer will make the next move.

**Command-line Usage:** `java Othello`

The game menu gives you the following possibilities:

- **Options** ►
	- **New:** New board.
	- **Undo:** Undo the last move.
	- **Hint:** The computer will move on behalf of you.
	- **Level:** Select the level of game, between 2 (easy) and 6 (difficult).
	- **Theme:** Select your favorite theme.
- **Help** ►
	- **About Othello:** Displays a small amount of pertinent information about the game.
	- **Documentation:** Shows the HTML version of `README.md`.

###Source Files
- **Othello.java:** The main class for the program, containing the `GamePanel` class as well.
- **GameBoard.java:** The class governing the game's board.
- **OthelloApplet.java:** Applet version of the game.
- **Test.java:** Command-line program that performs a benchmark of **Othello**.
