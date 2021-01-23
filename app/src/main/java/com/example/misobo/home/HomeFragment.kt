package com.example.misobo.home

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.example.misobo.R
import com.example.misobo.bmi.view.BmiActivity
import com.example.misobo.mind.view.MindFragment
import com.example.misobo.utils.SharedPreferenceManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fillName()

        editName.setOnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode === KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                if (!editName.text.isNullOrEmpty()) {
                    SharedPreferenceManager.setName(editName.text.toString())
                    fillName()
                }
            }
            false
        }

        fillName()

        activity?.bottomNavigationView?.visibility = View.VISIBLE
        unlockButtonBody.setOnClickListener {
            startActivity(Intent(context, BmiActivity::class.java))
        }

        unlockButtonMind.setOnClickListener {
            SharedPreferenceManager.setMindUnlock(true)
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(
                    R.id.mainContainer,
                    MindFragment()
                )
                ?.commit()
            //startActivity(Intent(context, MindActivity::class.java))
        }
    }

    private fun fillName() {
        if (SharedPreferenceManager.getName() != null) {
            editNameGroup.visibility = View.INVISIBLE
            nameTextView.text = SharedPreferenceManager.getName()
            nameTextView.visibility = View.VISIBLE
        } else {
            nameTextView.visibility = View.INVISIBLE
            editNameGroup.visibility = View.VISIBLE
        }
    }
}