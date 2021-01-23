package com.example.misobo.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.misobo.R
import com.example.misobo.bmi.BmiActivity
import com.example.misobo.bmi.BmiHomeFragment
import com.example.misobo.talkToExperts.TalkToExpertActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_bmi_home.*
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

        activity?.bottomNavigationView?.visibility = View.VISIBLE
        textView.setOnClickListener {
            startActivity(Intent(context, BmiActivity::class.java))
        }

        talkToExperts.setOnClickListener {
            startActivity(Intent(context, TalkToExpertActivity::class.java))
        }
        //bmiBackButton.setOnClickListener { activity?.onBackPressed() }
    }
}