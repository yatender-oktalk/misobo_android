package com.example.misobo.myProfile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.format.DateFormat
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.misobo.Misobo
import com.example.misobo.R
import com.example.misobo.bmi.view.BmiActivity
import com.example.misobo.utils.AuthState
import com.example.misobo.utils.SharedPreferenceManager
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_my_profile.*
import java.text.SimpleDateFormat
import java.util.*

class MyProfileFragment : Fragment() {
    private val groupAdapter = GroupAdapter<ViewHolder>()
    private val profileViewModel by lazy { ViewModelProvider(this).get(ProfileViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dailyCheckinRecyclerView.adapter = groupAdapter
        val weekList = listOf("S", "M", "T", "W", "T", "F", "S")

        fillName()

        Glide.with(requireContext())
            .load(SharedPreferenceManager.getProfileImage())
            .placeholder(R.drawable.profile_placeholder)
            .error(R.drawable.profile_placeholder)
            .into(profileImage);

        if (SharedPreferenceManager.getUserProfile()?.data?.bmi != null)
            bmiScore.text = SharedPreferenceManager.getUserProfile()?.data?.bmi
        else
            bmiScoreLayout.visibility = View.GONE

        editName.setOnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode === KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                if (!editName.text.isNullOrEmpty()) {
                    SharedPreferenceManager.setName(editName.text.toString())
                    fillName()
                }
            }
            false
        }

        bmiScoreLayout.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    BmiActivity::class.java
                )
            )
        }

        rewardsTextView.setOnClickListener {
            activity?.bottomNavigationView?.selectedItemId = R.id.rewards
        }

        logoutTextView.setOnClickListener {
            SharedPreferenceManager.clear()
            Misobo.authRelay.onNext(AuthState.FAILED)
        }

        profileViewModel.getProfile(SharedPreferenceManager.getUser()?.data?.userId ?: -1)

        profileViewModel.profileLiveData.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is ProfileResponseAction.Success -> {
                    SharedPreferenceManager.setUserProfile(state.response)
                    karmaCoinsText.text = state.response.data?.karmaPoints ?: "0"
                    if (state.response.data?.bmi != null) {
                        bmiScore.text = state.response.data.bmi
                        if (state.response.data.bmiCheckedDate != null) {
                            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                            val parsedDate: Date = dateFormat.parse(state.response.data.bmiCheckedDate)
                            val date = DateFormat.format("dd", parsedDate)
                            val month = DateFormat.format("MMM", parsedDate)
                            val year = DateFormat.format("yy", parsedDate)
                            calculationDateTextView.text ="Calculated on ${date} ${month}'${year}"
                        }
                    } else
                        bmiScoreLayout.visibility = View.GONE
                    groupAdapter.clear()
                    val section = Section()
                    section.add(
                        DailyCheckinItem(
                            state.response.data?.loginStreak?.one ?: "",
                            "S"
                        )
                    )
                    section.add(
                        DailyCheckinItem(
                            state.response.data?.loginStreak?.two ?: "",
                            "M"
                        )
                    )
                    section.add(
                        DailyCheckinItem(
                            state.response.data?.loginStreak?.three ?: "",
                            "T"
                        )
                    )
                    section.add(
                        DailyCheckinItem(
                            state.response.data?.loginStreak?.four ?: "",
                            "W"
                        )
                    )
                    section.add(
                        DailyCheckinItem(
                            state.response.data?.loginStreak?.five ?: "",
                            "T"
                        )
                    )
                    section.add(
                        DailyCheckinItem(
                            state.response.data?.loginStreak?.six ?: "",
                            "F"
                        )
                    )
                    section.add(
                        DailyCheckinItem(
                            state.response.data?.loginStreak?.seven ?: "",
                            "S"
                        )
                    )

                    groupAdapter.add(section)
                }
                is ProfileResponseAction.Loading -> {

                }
                is ProfileResponseAction.Error -> {
                    Toast.makeText(context, state.error, Toast.LENGTH_SHORT).show()
                }
            }
        })

        profileImage.setOnClickListener {
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.OVAL)
                .start(requireContext(), this);
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                val resultUri: Uri = result.uri
                SharedPreferenceManager.setProfileImage(resultUri.toString())
                Glide.with(requireContext())
                    .load(resultUri)
                    .placeholder(R.drawable.profile_placeholder)
                    .error(R.drawable.profile_placeholder)
                    .into(profileImage);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

    private fun fillName() {
        if (SharedPreferenceManager.getName() != null) {
            editNameGroup.visibility = View.INVISIBLE
            nameTextView.text = SharedPreferenceManager.getName()
            nameTextView.visibility = View.VISIBLE
        } else {
            nameTextView.visibility = View.INVISIBLE
            editNameGroup.visibility = View.VISIBLE
        }
    }
}