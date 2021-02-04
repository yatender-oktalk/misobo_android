package com.misohe.misohe.onBoarding

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.misohe.misohe.R
import com.misohe.misohe.bmi.view.BmiActivity
import com.misohe.misohe.mind.view.MindFragment
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
        activity?.bottomNavGroup?.visibility = View.GONE
        activity?.arcSeparator?.visibility = View.GONE
        activity?.arc?.visibility = View.GONE

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
        activity?.bottomNavGroup?.visibility = View.VISIBLE
        activity?.arcSeparator?.visibility = View.VISIBLE
        activity?.arc?.visibility = View.VISIBLE
    }
}