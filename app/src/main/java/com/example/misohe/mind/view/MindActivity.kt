package com.example.misohe.mind.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.misohe.R
import com.example.misohe.mind.viewModels.MindViewModel
import com.example.misohe.talkToExperts.models.CaptureOrderPayload
import com.example.misohe.talkToExperts.viewModels.TalkToExpertsViewModel
import com.razorpay.PaymentResultListener

class MindActivity : AppCompatActivity(), PaymentResultListener {

    private val talkToExpertsViewModel: TalkToExpertsViewModel by lazy {
        ViewModelProvider(this).get(
            TalkToExpertsViewModel::class.java
        )
    }

    private val viewModel: MindViewModel by lazy { ViewModelProvider(this).get(MindViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mind)

        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.mindFrameContainer,
                MindFragment()
            )
            .commit()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Log.i("fail", p0.toString() + " " + p1)
    }

    override fun onPaymentSuccess(p0: String?) {
        Log.i("success", p0.toString())
        talkToExpertsViewModel.captureOrder(
            CaptureOrderPayload(
                p0.toString(),
                "signature",
                talkToExpertsViewModel.currentOrder.value?.data?.orderId ?: 0,
                talkToExpertsViewModel.currentOrder.value?.data?.transactionId ?: 0,
                talkToExpertsViewModel.paymentAmount
            )
        )
    }
}