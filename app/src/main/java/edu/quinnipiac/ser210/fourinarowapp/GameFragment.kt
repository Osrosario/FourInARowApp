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

val FourInARow = FourInARow()
var currentState = GameConstants.PLAYING
var player = GameConstants.BLUE

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

        turnText.text = "$name, choose a location"

        FourInARow.fillBoard()

        return view
    }

    override fun onClick(v: View?)
    {
        val idToString = v!!.id.toString()
        val buttonNum = idToString.filter { it.isDigit() }

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

                    var (row, col) = FourInARow.setMove(player, buttonNum)
                    setRow = row
                    setCol = col

                    currentState = FourInARow.checkForWinner(player, setRow, setCol)
                }

                GameConstants.RED -> {

                    var (row, col) = FourInARow.setMove(player, FourInARow.computerMove())
                    setRow = row
                    setCol = col

                    currentState = FourInARow.checkForWinner(player, setRow, setCol)
                }
            }

            val button = v as Button
            button.text = player
            button.isEnabled = false

            if (currentState == GameConstants.PLAYING)
            {
                when (player)
                {
                    GameConstants.BLUE -> player = GameConstants.RED
                    GameConstants.RED -> player = GameConstants.BLUE
                }
            }
        }
    }
}