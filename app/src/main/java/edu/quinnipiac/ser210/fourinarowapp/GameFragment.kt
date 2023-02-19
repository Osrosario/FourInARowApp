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
var playerName = ""

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
            view.findViewWithTag<Button>("button$i").setOnClickListener(this)
        }

        FourInARow.fillBoard()

        return view
    }

    override fun onClick(v: View?)
    {
        when(v!!.id)
        {
            R.id.button0 -> {
                val button = v!!.findViewById<Button>(R.id.button0)
                setBoard(v!!, button)
            }
        }
    }

    private fun setBoard(view: View?, button: Button)
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

                        val buttonNum = button.tag.toString().filter { it.isDigit() }

                        val turnText = view?.findViewById<TextView>(R.id.turn_text)
                        turnText?.text = "$playerName, choose a location"

                        var (row, col) = FourInARow.setMove(player, buttonNum)
                        setRow = row
                        setCol = col

                        button.text = player
                        button.isEnabled = false
                    }

                    GameConstants.RED -> {

                        val button = view?.findViewWithTag<Button>("button${FourInARow.computerMove()}")
                        val buttonNum = button?.tag.toString().filter { it.isDigit() }

                        val turnText = view?.findViewById<TextView>(R.id.turn_text)
                        turnText?.text = "Opponent is choosing a location..."

                        var (row, col) = FourInARow.setMove(player, buttonNum)
                        setRow = row
                        setCol = col

                        button?.text = player
                        button?.isEnabled = false
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