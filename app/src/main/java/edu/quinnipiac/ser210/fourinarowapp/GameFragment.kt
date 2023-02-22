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