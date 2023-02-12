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

/**
 * A simple [Fragment] subclass.
 * Use the [WelcomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WelcomeFragment : Fragment()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)
        val editTextName = view.findViewById<EditText>(R.id.name_editText)
        val buttonStart = view.findViewById<Button>(R.id.start_button)

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
            view.findNavController()
                .navigate(R.id.action_welcomeFragment_to_gameFragment)
        }

        return view
    }
}