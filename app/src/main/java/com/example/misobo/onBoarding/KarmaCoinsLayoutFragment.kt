package com.example.misobo.onBoarding

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.misobo.R
import com.example.misobo.bmi.view.BmiActivity
import com.example.misobo.mind.view.MindFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.karma_coins_layout.*

class KarmaCoinsLayoutFragment : Fragment() {

    val redirectTo by lazy { requireArguments().getInt("TO",1) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.karma_coins_layout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.bottomNavigationView?.visibility = View.GONE

        registerText.setOnClickListener {
            if (redirectTo == 1){
                startActivity(Intent(context, BmiActivity::class.java))
            }
            else
            {
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.replace(
                        R.id.mainContainer,
                        MindFragment()
                    )
                    ?.commit()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.bottomNavigationView?.visibility = View.VISIBLE
    }
}