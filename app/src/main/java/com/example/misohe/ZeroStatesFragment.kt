package com.example.misohe

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_zero_states.*

class ZeroStatesFragment : Fragment() {

    private lateinit var mCallback: OnCtaClicked

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_zero_states, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCtaClicked) {
            mCallback = context
        } else {
            throw RuntimeException(context.toString()
                    + " must implement CtaClicked");
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        retryConnectionText.setOnClickListener { mCallback.retry() }
    }

    interface OnCtaClicked {
        fun retry()
    }
}