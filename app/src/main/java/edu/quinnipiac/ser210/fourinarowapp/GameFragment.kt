package edu.quinnipiac.ser210.fourinarowapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import javax.crypto.EncryptedPrivateKeyInfo

/**
 * A simple [Fragment] subclass.
 * Use the [GameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameFragment : Fragment()
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
        val bot_turn = "Opponent is choosing a location..."
        val player_turn = ", choose a location"

        val name = GameFragmentArgs.fromBundle(requireArguments()).name
        turnText.text = "$name $player_turn"

        return view
    }
}