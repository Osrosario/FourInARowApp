/**
 * Four in a Row: 6x6 Tic-Tac-Toe Game.
 *
 * GameFragment contains the game board and utilizes functions from the FourInARow class as backend
 * code. To play, the user must click any available button to place their marker onto the board.
 * Once a button is clicked, the chosen button will be disabled and the player's marker will replace
 * the text within the button. To record the player's chosen location, the position will be recorded
 * into a 2D array provided by the FourInARow class. The game will check for a streak of 4, in all
 * possible directions, based on the player's last chosen location. The first player to achieve a
 * streak of 4 wins the game. Ties between players are allowed. The game will enable a reset button
 * if the player wishes to play again.
 *
 * @author osrosario
 * @date 2/23/2023
 */

package edu.quinnipiac.ser210.fourinarowapp

import FourInARow
import GameConstants
import IGame
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import org.w3c.dom.Text
import java.math.BigDecimal
import javax.crypto.EncryptedPrivateKeyInfo
import kotlin.random.Random

val FourInARow = FourInARow()
var currentState = GameConstants.PLAYING
var player = GameConstants.BLUE
var playerName = ""
val buttons = mutableListOf<Button>()
val buttonState = mutableListOf<Boolean>()

class GameFragment : Fragment(), View.OnClickListener
{
    lateinit var turnText: TextView
    lateinit var resetButton: Button

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_game, container, false)
        turnText = view.findViewById<TextView>(R.id.turn_text) as TextView
        resetButton = view.findViewById<Button>(R.id.reset_button) as Button

        val name = GameFragmentArgs.fromBundle(requireArguments()).name
        playerName = name

        turnText.text = "$playerName, choose a location"

        for (i in 0..35)
        {
            val button = view.findViewWithTag<Button>("$i")
            button.setOnClickListener(this);
            buttons.add(button)
        }

        resetButton.setOnClickListener(this)

        FourInARow.fillBoard()

        return view
    }

    override fun onClick(v: View?)
    {
        when (v!!.id)
        {
            R.id.reset_button -> {

                for (button in buttons)
                {
                    button.isEnabled = true
                    button.text = "_"
                }

                FourInARow.fillBoard()
                turnText.text = "$playerName, choose a location"
                resetButton.isEnabled = false
            }

            else -> {

                val button = v!!.findViewById<Button>(v!!.id)
                setBoard(button)
            }
        }
    }

    /*
    override fun onSaveInstanceState(outState: Bundle)
    {
        super.onSaveInstanceState(outState)
        outState.putString("turnText", turnText.toString())
        outState.putStringArray("locations", FourInARow.getBoard())
        outState.putBooleanArray("states", buttonState.toBooleanArray())
        outState.putBoolean("resetButton", resetButton.isEnabled)
    }
    */

    /**
     * The setBoard function utilizes functions in the FourInARow class to record the position of
     * each player. If a button is clicked, the tag of that button is retrieved and used as the
     * location to search in the 2D array, representing the board. The button becomes disabled once
     * the player clicks on it. The bulk of the code is set within a while loop that executes
     * a different body of code that chooses a random button and location for the computer player. All
     * buttons are disabled and reset button is enabled if there is a tie or a player wins.
     */
    private fun setBoard(button: Button)
    {
        var botHasGone = false

        while (!botHasGone)
        {
            if (FourInARow.isBoardFull())
            {
                currentState = GameConstants.TIE
            }
            else
            {
                var setRow = 0
                var setCol = 0

                when (player)
                {
                    GameConstants.BLUE -> {

                        val buttonNum = button.tag.toString()

                        var (row, col) = FourInARow.setMove(player, buttonNum)
                        setRow = row
                        setCol = col

                        button.text = player
                        button.isEnabled = false
                        buttonState[buttonNum.toInt()] = button.isEnabled
                    }

                    GameConstants.RED -> {

                        val location = FourInARow.computerMove()

                        turnText.text = "Opponent is choosing location..."

                        var (row, col) = FourInARow.setMove(player, location)
                        setRow = row
                        setCol = col

                        val cpuButton = buttons[location.toInt()]
                        cpuButton.text = player
                        cpuButton.isEnabled = false
                        buttonState[location.toInt()] = cpuButton.isEnabled
                    }
                }

                currentState = FourInARow.checkForWinner(player, setRow, setCol)

                when (currentState)
                {
                    GameConstants.PLAYING -> {

                        when (player)
                        {
                            GameConstants.BLUE -> {

                                player = GameConstants.RED
                            }

                            GameConstants.RED -> {

                                player = GameConstants.BLUE
                                turnText.text = "$playerName, choose a location"
                                botHasGone = true
                            }
                        }
                    }

                    GameConstants.BLUE_WON -> {

                        for (button in buttons)
                        {
                            button.isEnabled = false
                        }

                        turnText.text = "$playerName won!"
                        resetButton.isEnabled = true
                        botHasGone = true
                    }

                    GameConstants.RED_WON -> {

                        for (button in buttons)
                        {
                            button.isEnabled = false
                        }

                        turnText.text = "Computer has won!"
                        resetButton.isEnabled = true
                        botHasGone = true
                    }

                    GameConstants.TIE -> {

                        for (button in buttons)
                        {
                            button.isEnabled = false
                        }

                        turnText.text = "It's a tie!"
                        resetButton.isEnabled = true
                        botHasGone = true
                    }
                }
            }
        }
    }
}