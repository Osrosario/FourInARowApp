/**
 * Four in a Row: A 6x6 Tic-Tac-Toe Game.
 *
 * WelcomeFragment primarily displays information for the user. This fragment displays the title, a
 * subtitle, an image to fill the white space, the rules of the game, an EditText view, and a button
 * to navigate to the next fragment. WelcomeFragment uses a TextWatcher to detect changes within the
 * EditText's to enable the start button. This feature prevents the user from navigating to the next
 * Fragment without entering a name.
 *
 * @author osrosario
 * @date 2/23/2023
 */

package edu.quinnipiac.ser210.fourinarowapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.findNavController

class WelcomeFragment : Fragment()
{
    lateinit var editTextName: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)
        editTextName = view.findViewById<EditText>(R.id.name_editText)
        val buttonStart = view.findViewById<Button>(R.id.start_button)

        /**
        Executes when the text in the EditText is changed. The start button within this view is
        disabled. When the user starts typing, the TextWatcher will check the EditText. The
        start button will be enabled as long as the EditText is not empty.
        */
        val nameWatcher = object:TextWatcher
        {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)
            {
                val nameInput = editTextName.text.toString().trim()
                buttonStart.setEnabled(nameInput.isNotEmpty())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
        }

        editTextName.addTextChangedListener(nameWatcher)

        buttonStart.setOnClickListener {

            val name = editTextName.text.toString()
            val action = WelcomeFragmentDirections.actionWelcomeFragmentToGameFragment(name)

            view.findNavController().navigate(action)
        }

        return view
    }
}