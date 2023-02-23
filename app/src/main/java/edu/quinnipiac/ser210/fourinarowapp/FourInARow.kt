import kotlin.random.Random

class FourInARow : IGame
{
    /**
     * Fills board with empty strings.
     */
    private val board = Array(GameConstants.ROWS) { Array<String>(GameConstants.COLS){"_"} }

    /**
     * An array of strings from 0-35 for comparison and filling the board.
     */
    private val locations = Array(36) { i -> (i * 1).toString() }

    /**
     * Returns the board
     */
    override fun getBoard(): Array<String>
    {
        return locations
    }

    /**
     * Resets the board if replay is accepted.
     */
    override fun fillBoard()
    {
        var index = 0

        for (row in 0 until GameConstants.ROWS)
        {
            for (col in 0 until GameConstants.COLS)
            {
                board[row][col] = locations[index]
                index++
            }
        }
    }

    /**
     * Sets the player's marker onto the board based on chosen location. If
     * the player chooses a filled location, the program will prompt a different
     * answer. If the computer player chooses a filled location, it will be given
     * a new location. Once a valid location has been entered, it will be marked
     * on the board. Return coordinates of marked location to check for a winner.
     */
    override fun setMove(player:String, location:String?):Pair<Int, Int>
    {
        var isMoveSet = false
        var input:String? = location
        var locationRow = 0
        var locationCol = 0

        while (!isMoveSet)
        {
            for (row in 0 until GameConstants.ROWS)
            {
                for (col in 0 until GameConstants.COLS)
                {
                    if (board[row][col] == input)
                    {
                        board[row][col] = player
                        locationRow = row
                        locationCol = col
                        isMoveSet = true
                    }
                }
            }

            if (!isMoveSet && player == GameConstants.RED)
            {
                input = Random.nextInt(0, 35).toString()
            }
        }

        return locationRow to locationCol
    }

    /**
     * Chooses a random location for the computer player.
     */
    override fun computerMove():String
    {
        return Random.nextInt(0,35).toString()
    }

    /**
     * Checks if the board is full. If no more locations are available, returns
     * true, the game ends, otherwise false.
     */
    override fun isBoardFull():Boolean
    {
        for (row in 0 until GameConstants.ROWS)
        {
            for (col in 0 until GameConstants.COLS)
            {
                if (locations.contains(board[row][col]))
                {
                    return false
                }
            }
        }

        return true
    }

    /**
     * Checks for vertical streak.
     */
    override fun veritcal(player:String, col: Int):Boolean
    {
        var streak = 0

        for (row in 0 until GameConstants.ROWS - 1)
        {
            if (board[row][col] == player)
            {
                streak++

                if (streak >= 4)
                {
                    return true
                }
            }
            else if (board[row][col] != player)
            {
                streak = 0
            }
        }

        return false
    }

    /**
     * Checks for horizontal streak.
     */
    override fun horizontal(player:String, row:Int):Boolean
    {
        var streak = 0

        for (col in 0 until GameConstants.COLS - 1)
        {
            if (board[row][col] == player)
            {
                streak++

                if (streak >= 4)
                {
                    return true
                }
            }
            else if (board[row][col] != player)
            {
                streak = 0
            }
        }

        return false
    }

    /**
     * Checks for forward-slash (/) streak.
     */
    override fun forwardSlash(player:String, row:Int, col:Int): Boolean
    {
        var streak = 0

        for (i in 0 until GameConstants.ROWS)
        {
            var checkSlash = row + col - i

            if (checkSlash >= 0 && checkSlash < GameConstants.COLS)
            {
                if (board[i][checkSlash] == player)
                {
                    streak++

                    if (streak >= 4)
                    {
                        return true
                    }
                }
                else if (board[i][checkSlash] != player)
                {
                    streak = 0
                }
            }
        }

        return false
    }

    /**
     * Checks for back-slash (\) streak.
     */
    override fun backSlash(player:String, row:Int, col:Int):Boolean
    {
        var streak = 0

        for (i in 0 until GameConstants.ROWS)
        {
            var checkSlash = row - col + i

            if (checkSlash >= 0 && checkSlash < GameConstants.COLS)
            {
                if (board[checkSlash][i] == player)
                {
                    streak++

                    if (streak >= 4)
                    {
                        return true
                    }
                }
                else if (board[checkSlash][i] != player)
                {
                    streak = 0
                }
            }
        }

        return false
    }

    /**
     * Checks for vertical, horizontal, forward-slash, and back-slash streak
     * based on the last location entered. If a streak is found, return end
     * game state. If no streak was found, return continuing game state.
     */
    override fun checkForWinner(player:String, row:Int, col:Int): Int
    {
        var hasWon = false

        for (i in 0 until 4)
        {
            when(i)
            {
                0 -> hasWon = veritcal(player, col)
                1 -> hasWon = horizontal(player, row)
                2 -> hasWon = forwardSlash(player, row, col)
                3 -> hasWon = backSlash(player, row, col)
            }

            if (hasWon && player == GameConstants.BLUE)
            {
                return GameConstants.BLUE_WON
            }
            else if (hasWon && player == GameConstants.RED)
            {
                return GameConstants.RED_WON
            }
        }

        return GameConstants.PLAYING
    }

    /**
     * Print the game board.
     */
    fun printBoard()
    {
        println()

        for (row in 0 until GameConstants.ROWS)
        {
            for (col in 0 until GameConstants.COLS)
            {
                if (board[row][col].length < 2)
                {
                    print(" " + board[row][col] + "  ") //Formats one character cells
                }
                else
                {
                    print(" " + board[row][col] + " ") //Formats two characters cells
                }

                if (col != GameConstants.COLS - 1)
                {
                    print("|") //Vertical partition
                }
            }

            println()

            if (row != GameConstants.ROWS - 1)
            {
                println("----------------------------") //Horizontal partition
            }
        }

        println()
    }
}