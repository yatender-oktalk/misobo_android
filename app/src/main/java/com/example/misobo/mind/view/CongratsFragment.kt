package com.example.misobo.mind.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.misobo.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_congrats.*

class CongratsFragment : Fragment() {

    private val karmaCoin: String by lazy { requireArguments().getString("COINS", "101") }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_congrats, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.bottomNavigationView?.visibility = View.GONE

        karmaCoinsText.text = karmaCoin
        youEarnedCoins.text = "You earned $karmaCoin karma coins"
        comebackTommorowText.text = "Come back tomorrow to earn \n more $karmaCoin karma coins"

        crossIcon.setOnClickListener {
            activity?.onBackPressed()
        }
        okayButton.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.bottomNavigationView?.visibility = View.VISIBLE
    }
}