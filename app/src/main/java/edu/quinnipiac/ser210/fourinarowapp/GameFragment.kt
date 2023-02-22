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
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_game, container, false)
        val turnText = view.findViewById<TextView>(R.id.turn_text)
        val name = GameFragmentArgs.fromBundle(requireArguments()).name
        playerName = name

        turnText.text = "$playerName, choose a location"

        for (i in 0..35)
        {
           val button = view.findViewWithTag<Button>("$i")
           button.setOnClickListener(this);
           buttons.add(button)
        }

        FourInARow.fillBoard()

        return view
    }

    override fun onClick(v: View?)
    {
        val button = v!!.findViewById<Button>(v!!.id)
        setBoard(button)
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
                        FourInARow.locations.removeAt(buttonNum.toInt())

                        val turnText = view?.findViewById<TextView>(R.id.turn_text)
                        turnText?.text = "$playerName, choose a location"

                        var (row, col) = FourInARow.setMove(player, buttonNum)
                        setRow = row
                        setCol = col

                        button.text = player
                        button.isEnabled = false
                    }

                    GameConstants.RED -> {

                        val location = FourInARow.locations[Random.nextInt(0, FourInARow.locations.size - 1)]

                        var (row, col) = FourInARow.setMove(player, location)
                        setRow = row
                        setCol = col

                        val otherButton = button.findViewWithTag<Button>(location)
                        otherButton.text = player
                        otherButton.isEnabled = false
                    }
                }

                currentState = FourInARow.checkForWinner(player, setRow, setCol)

                if (currentState == GameConstants.PLAYING)
                {
                    when (player)
                    {
                        GameConstants.BLUE -> player = GameConstants.RED

                        GameConstants.RED -> {
                            player = GameConstants.BLUE
                            botHasGone = true
                        }
                    }
                }
            }
        }


    }
}