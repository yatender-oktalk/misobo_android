package com.example.misohe.talkToExperts.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.misohe.R
import com.example.misohe.talkToExperts.models.CaptureOrderPayload
import com.example.misohe.talkToExperts.viewModels.TalkToExpertsViewModel
import com.razorpay.PaymentResultListener

class TalkToExpertActivity : AppCompatActivity(), PaymentResultListener {

    val viewModel: TalkToExpertsViewModel by lazy {
        ViewModelProvider(this).get(TalkToExpertsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_talk_to_expert)

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.expertsFrameContainer,
                TalktoExpertsHomeFragment()
            )
            .commit()
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Log.i("fail", p0.toString() + " " + p1)
    }

    override fun onPaymentSuccess(p0: String?) {
        Log.i("success", p0.toString())
        viewModel.captureOrder(
            CaptureOrderPayload(
                p0.toString(),
                "signature",
                viewModel.currentOrder.value?.data?.orderId ?: 0,
                viewModel.currentOrder.value?.data?.transactionId ?: 0,
                viewModel.paymentAmount
            )
        )
    }
}