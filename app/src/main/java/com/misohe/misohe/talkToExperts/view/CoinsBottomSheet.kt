package com.misohe.misohe.talkToExperts.view

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.misohe.misohe.Misohe
import com.misohe.misohe.R
import com.misohe.misohe.mind.models.OrderPayload
import com.misohe.misohe.mind.viewModels.OrderFetchState
import com.misohe.misohe.myProfile.ProfileViewModel
import com.misohe.misohe.talkToExperts.items.PacksItem
import com.misohe.misohe.talkToExperts.models.CaptureOrderPayload
import com.misohe.misohe.talkToExperts.viewModels.CaptureOrderState
import com.misohe.misohe.talkToExperts.viewModels.PacksFetchState
import com.misohe.misohe.talkToExperts.viewModels.TalkToExpertsViewModel
import com.misohe.misohe.utils.SharedPreferenceManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.coins_bottom_sheet.*
import org.json.JSONObject

class CoinsBottomSheet : BottomSheetDialogFragment(), PaymentResultListener {

    val viewModel: TalkToExpertsViewModel by activityViewModels()
    val profileViewModel: ProfileViewModel by lazy { ViewModelProvider(this).get(ProfileViewModel::class.java) }
    val compositeDisposable = CompositeDisposable()
    private val groupAdapter = GroupAdapter<ViewHolder>()
    private val type by lazy { requireArguments().getString("TYPE", "CALL") }
    private val rewardId: Int? by lazy { arguments?.getInt("REWARD_ID") }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.coins_bottom_sheet, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Checkout.preload(Misohe.instance.applicationContext)

        when (type) {
            "CALL" -> {
                needMoreCoins.text = "You need more coins to\n book this session"
            }
            "REWARDS" -> {
                needMoreCoins.text = "You have less coins to\n unlock this reward"
            }
        }

        Checkout.preload(requireContext())

        crossButton.setOnClickListener {
            this.dismiss()
        }
        viewModel.getPacks()
        karmaCoinsText.text = SharedPreferenceManager.getUserProfile()?.data?.karmaPoints

        KarmaCoinsRecycler.adapter = groupAdapter
        viewModel.packsLiveData.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is PacksFetchState.Success -> {
                    groupAdapter.clear()
                    val section = Section()
                    state.packsList.forEach { pack ->
                        section.add(PacksItem(pack) {
                            viewModel.paymentAmount = pack.amount?.toDouble() ?: 1.00
                            viewModel.pack = pack.karmaCoins ?: 0
                            viewModel.createOrder(
                                OrderPayload(
                                    pack.amount?.toDouble() ?: 1.00,
                                    OrderPayload.Note(pack.id.toString())
                                )
                            )
                        })
                    }
                    groupAdapter.add(section)
                }
            }
        })

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
                    val bundle = Bundle()
                    bundle.putString("TYPE", type)
                    if (rewardId != null)
                        bundle.putInt("REWARD_ID", rewardId ?: -1)
                    val coinsAdditionSuccess = CoinsAdditionSuccess()
                    coinsAdditionSuccess.arguments = bundle
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.add(coinsAdditionSuccess, null)?.commit()

                    profileViewModel.getProfile(
                        SharedPreferenceManager.getUser()?.data?.userId ?: 0
                    )
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
        val activity: Activity = requireActivity()
        val co = Checkout()
        co.setImage(R.drawable.misobo_icon);
        try {
            val options = JSONObject()
            options.put("name", "True Base Pvt Ltd")
            options.put("description", "Book a session")
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("theme.color", "#4d426c");
            options.put("currency", "INR");
            options.put("order_id", paymentGatewayOrderId);
            options.put(
                "amount",
                viewModel.paymentAmount.toString()
            )//pass amount in currency subunits

            val prefill = JSONObject()
            //prefill.put("email", "akshay@gmail.com")
            //prefill.put("contact", "9876543210")
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

    override fun setupDialog(dialog: Dialog, style: Int) {
        val contentView =
            View.inflate(context, R.layout.coins_bottom_sheet, null)
        dialog.setContentView(contentView)
        (contentView.parent as View).setBackgroundColor(
            ContextCompat.getColor(requireContext(), android.R.color.transparent)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}