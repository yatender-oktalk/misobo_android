package com.misohe.misohe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_loading.*

class LoadingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loading, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.bottomNavGroup?.visibility = View.GONE
        activity?.arcSeparator?.visibility = View.GONE
        activity?.arc?.visibility = View.GONE


        loadingAnimation.playAnimation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        loadingAnimation.cancelAnimation()
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.bottomNavGroup?.visibility = View.VISIBLE
        activity?.arcSeparator?.visibility = View.VISIBLE
        activity?.arc?.visibility = View.VISIBLE
    }
}