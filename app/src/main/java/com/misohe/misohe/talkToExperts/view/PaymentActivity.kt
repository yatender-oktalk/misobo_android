package com.misohe.misohe.talkToExperts.view

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.misohe.misohe.R
import com.misohe.misohe.mind.models.OrderPayload
import com.misohe.misohe.mind.viewModels.OrderFetchState
import com.misohe.misohe.talkToExperts.models.CaptureOrderPayload
import com.misohe.misohe.talkToExperts.viewModels.CaptureOrderState
import com.misohe.misohe.talkToExperts.viewModels.TalkToExpertsViewModel
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.android.synthetic.main.activity_payment.*
import org.json.JSONObject

class PaymentActivity : AppCompatActivity(), PaymentResultListener {

    val viewModel: TalkToExpertsViewModel by lazy {
        ViewModelProvider(this).get(
            TalkToExpertsViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        Checkout.preload(this)

        button.setOnClickListener {
            viewModel.createOrder(OrderPayload(1.00, OrderPayload.Note("1")))
        }

        viewModel.orderLiveData.observe(this, Observer { state ->
            when (state) {
                is OrderFetchState.Success -> {
                    viewModel.currentOrder.postValue(state.orderResponse)
                    startPayment(state.orderResponse.data?.paymentGatewayOrderId)
                }
                is OrderFetchState.Loading -> {

                }
                is OrderFetchState.Error -> {

                }
            }
        })

        viewModel.captureOrderLiveData.observe(this, Observer { state ->
            when (state) {
                is CaptureOrderState.Success -> {
                    Log.i("captureOrder", state.response.data?.msg.toString())
                }
                is CaptureOrderState.Fail -> {

                }
                is CaptureOrderState.Loading -> {

                }
            }
        })
    }

    private fun startPayment(paymentGatewayOrderId: String?) {
        /*
        *  You need to pass current activity in order to let Razorpay create CheckoutActivity
        * */
        val activity: Activity = this
        val co = Checkout()
        co.setImage(R.drawable.misobo_icon);
        try {
            val options = JSONObject()
            options.put("name", "True Base Pvt Ltd")
            options.put("description", "Book a session")
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("theme.color", R.color.colorAccent);
            options.put("currency", "INR");
            options.put("order_id", paymentGatewayOrderId);
            options.put("amount", "1.00")//pass amount in currency subunits

            val prefill = JSONObject()
            prefill.put("email", "akshay@gmail.com")
            prefill.put("contact", "9876543210")
            options.put("prefill", prefill)
            co.open(activity, options)
        } catch (e: Exception) {
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Log.i("fail", p0.toString() + " " + p1)

    }

    override fun onPaymentSuccess(p0: String?) {
        Log.i("success", p0.toString())
        viewModel.captureOrder(CaptureOrderPayload(p0.toString(),"signature" , viewModel.currentOrder.value?.data?.orderId?:0,viewModel.currentOrder.value?.data?.transactionId?:0,1.00))
    }
}