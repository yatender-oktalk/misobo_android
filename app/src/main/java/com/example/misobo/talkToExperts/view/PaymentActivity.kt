package com.example.misobo.talkToExperts.view

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.misobo.R
import com.example.misobo.mind.models.OrderPayload
import com.example.misobo.mind.viewModels.OrderFetchState
import com.example.misobo.talkToExperts.viewModels.TalkToExpertsViewModel
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
            viewModel.createOrder(OrderPayload(200.25, OrderPayload.Note("jsfjfhhfs")))
        }

        viewModel.orderLiveData.observe(this, Observer { state->
            when(state){
                is OrderFetchState.Success->{
                    startPayment(state.orderResponse.data?.paymentGatewayOrderId)
                }
                is OrderFetchState.Loading->{

                }
                is OrderFetchState.Error->{

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

        co.setKeyID("rzp_test_dTvKn9msFUzE7U");
        try {
            val options = JSONObject()
            options.put("name", "Misobo Pvt Ltd")
            options.put("description", "Schedule a call")
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("theme.color", "#f1ab87");
            options.put("currency", "INR");
            options.put("order_id", paymentGatewayOrderId);
            options.put("amount", "200")//pass amount in currency subunits

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

    }

    override fun onPaymentSuccess(p0: String?) {

    }
}