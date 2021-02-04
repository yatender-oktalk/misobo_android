package com.example.misohe.home

import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.example.misohe.R
import com.example.misohe.mind.viewModels.MindViewModel
import com.example.misohe.myProfile.ProfileViewModel
import com.example.misohe.onBoarding.KarmaCoinsLayoutFragment
import com.example.misohe.utils.SharedPreferenceManager
import com.google.gson.JsonObject
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.concurrent.TimeUnit


class HomeFragment : Fragment() {

    private val mindViewModel: MindViewModel by activityViewModels()
    private val profileViewModel by lazy { ViewModelProvider(this).get(ProfileViewModel::class.java) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fillName()
        editName.setOnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode === KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                if (!editName.text.isNullOrEmpty()) {
                    profileViewModel.updateName(
                        SharedPreferenceManager.getUserProfile()?.data?.id ?: 0,
                        JsonObject().apply { addProperty("name", editName.text.toString()) }
                    )
                }
            }
            false
        }

        val dialog: AlertDialog.Builder =
            androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Misohe")
                .setMessage("Congratulations! You just earned 501 misohe coins for becoming a member")
        val alert: AlertDialog = dialog.create()
        alert.show()
        alert.window?.setLayout(alert.window?.attributes?.width!!, 500)

// Hide after some seconds

// Hide after some seconds
        val handler = Handler()
        val runnable = Runnable {
            if (alert.isShowing()) {
                alert.dismiss()
            }
        }

        alert.setOnDismissListener(DialogInterface.OnDismissListener {
            handler.removeCallbacks(
                runnable
            )
        })

        handler.postDelayed(runnable, 3000)

        profileViewModel.nameToast.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, "Name saved successfully", Toast.LENGTH_SHORT).show()
        })

        mindViewModel.getCoinsLiveData().observe(viewLifecycleOwner, Observer { profile ->
            fillName()
        })

        activity?.bottomNavGroup?.visibility = View.VISIBLE
        activity?.arcSeparator?.visibility = View.VISIBLE
        activity?.arc?.visibility = View.VISIBLE

        unlockButtonBody.setOnClickListener {

            val bundle = Bundle()
            bundle.putInt("TO", 2)
            val jsonObject = JsonObject()
            jsonObject.addProperty("is_body_pack_unlocked", true)
            mindViewModel.updatePackUnlock(
                SharedPreferenceManager.getUser()?.data?.userId ?: 0, jsonObject
            )
            val karmaCoinsFragment = KarmaCoinsLayoutFragment()
            karmaCoinsFragment.arguments = bundle
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.mainContainer, karmaCoinsFragment)
                ?.commit()
            //startActivity(Intent(context, BmiActivity::class.java))
        }

        unlockButtonMind.setOnClickListener {
            val jsonObject = JsonObject()
            jsonObject.addProperty("is_mind_pack_unlocked", true)
            mindViewModel.updatePackUnlock(
                SharedPreferenceManager.getUser()?.data?.userId ?: 0,
                jsonObject
            )
            val bundle = Bundle()
            bundle.putInt("TO", 2)
            val karmaCoinsFragment = KarmaCoinsLayoutFragment()
            karmaCoinsFragment.arguments = bundle
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.mainContainer, karmaCoinsFragment)
                ?.commit()
            //SharedPreferenceManager.setMindUnlock(true)
            /*activity?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(
                    R.id.mainContainer,
                    MindFragment()
                )
                ?.commit()*/
            //startActivity(Intent(context, MindActivity::class.java))
        }
    }

    private fun fillName() {
        if (SharedPreferenceManager.getUserProfile()?.data?.name != null) {
            editNameGroup.visibility = View.INVISIBLE
            nameTextView.text = SharedPreferenceManager.getUserProfile()?.data?.name
            nameTextView.visibility = View.VISIBLE
        } else {
            nameTextView.visibility = View.INVISIBLE
            editNameGroup.visibility = View.VISIBLE
        }
    }
}