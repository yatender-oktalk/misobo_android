package com.example.misobo.home

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.misobo.R
import com.example.misobo.bmi.view.BmiActivity
import com.example.misobo.mind.view.MindFragment
import com.example.misobo.mind.viewModels.MindViewModel
import com.example.misobo.myProfile.ProfileViewModel
import com.example.misobo.onBoarding.KarmaCoinsLayoutFragment
import com.example.misobo.utils.SharedPreferenceManager
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONObject

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

        profileViewModel.nameToast.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context,"Name saved successfully",Toast.LENGTH_SHORT).show()
        })

        mindViewModel.getCoinsLiveData().observe(viewLifecycleOwner, Observer { profile ->
            fillName()
        })

        activity?.bottomNavigationView?.visibility = View.VISIBLE
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