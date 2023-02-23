/**
 * IGame Interface.
 * @author osrosario
 * @date 2/23/2023
 */
interface IGame
{
    /**
     * Returns the board
     */
    fun getBoard(): Array<String>

    /**
     * Resets the board of all discs by setting all locations with strings
     */
    fun fillBoard()

    /**
     * Sets the given player at the given location on the game board.
     * The location must be available, or the board will not be changed.
     * @param player - HUMAN_PLAYER or COMPUTER_PLAYER
     * @param location - The location (0-35) to place the move
     */
    fun setMove(player: String, location: String?):Pair<Int, Int>

    /**
     * Returns the best move for the computer to make. You must call setMove()
     * to actually make the computer move to the location.
     * Start with a dumb implementation that returns the first non-occupied cell
     * @return the best move for the computer to make (0-35).
     */
    fun computerMove():String

    /**
     * Checks if board is full.
     */
    fun isBoardFull():Boolean

    /**
     * Checks for streak of four in all possible directions.
     */
    fun veritcal(player:String, col:Int):Boolean
    fun horizontal(player:String, row:Int):Boolean
    fun forwardSlash(player:String, row:Int, col:Int):Boolean
    fun backSlash(player:String, row:Int, col:Int):Boolean

    /**
     * Check for a winner and return a status value indicating who has won.
     * @return PLAYING if still playing, TIE if it's a tie, BLUE_WON if X won, or RED_WON if 0 won.
     */
    fun checkForWinner(player:String, row:Int, col:Int):Int
}

object GameConstants
{
    // Name-constants to represent the seeds and cell contents.
    const val BLUE = "B"
    const val RED = "R"

    // Name-constants to represent the various states of the game.
    const val PLAYING = 0
    const val TIE = 1
    const val RED_WON = 2
    const val BLUE_WON = 3

    const val ROWS = 6
    const val COLS = 6 // Number of rows and columns.
}