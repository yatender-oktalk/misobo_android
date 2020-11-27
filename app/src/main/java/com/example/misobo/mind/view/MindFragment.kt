package com.example.misobo.mind.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.misobo.R
import com.example.misobo.mind.items.HelloItem
import com.example.misobo.mind.items.TalkToTherapistItem
import com.example.misobo.mind.items.TasksForTheDayItems
import com.example.misobo.mind.items.UnlockItems
import com.example.misobo.mind.viewModels.MindViewModel
import com.example.misobo.mind.viewModels.MusicFetchState
import com.example.misobo.talkToExperts.view.TalkToExpertActivity
import com.example.misobo.talkToExperts.viewModels.ExpertListState
import com.example.misobo.talkToExperts.viewModels.TalkToExpertsViewModel
import com.example.misobo.utils.SharedPreferenceManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_mind.*

class MindFragment : Fragment() {

    private val mindViewModel: MindViewModel by activityViewModels()
    private val talkToExpertsViewModel by lazy { ViewModelProvider(this).get(TalkToExpertsViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mind, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = GroupAdapter<ViewHolder>()
        val helloSection = Section()
        val unlockSection = Section()
        val taskForTheDaySection = Section()
        val talkToExpertsSection = Section()
        mindHomeRecyclerView.adapter = adapter
        helloSection.add(HelloItem())
        unlockSection.add(UnlockItems())

        if (talkToExpertsViewModel.expertListLiveData.value == null)
            talkToExpertsViewModel.getAllExpertsList()
        talkToExpertsViewModel.expertListLiveData.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is ExpertListState.Success -> {
                    talkToExpertsSection.add(TalkToTherapistItem(state.expertList) {
                        startActivity(Intent(requireContext(), TalkToExpertActivity::class.java))
                    })
                }
                is ExpertListState.Fail -> {

                }
                is ExpertListState.Loading -> {
                }
            }
        })


        if (mindViewModel.musicLiveData.value == null)
            mindViewModel.fetchAllMusic()
        mindViewModel.musicLiveData.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is MusicFetchState.Success -> {
                    taskForTheDaySection.add(TasksForTheDayItems(state.musicEntries) { position ->
                        mindViewModel.playMusicLiveData.postValue(
                            state.musicEntries[position]
                        )
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.mindFrameContainer, MusicPlayerFragment())
                            ?.addToBackStack(null)?.commit()
                    })
                }
                is MusicFetchState.Loading -> {

                }
                is MusicFetchState.Error -> {

                }
            }
        })
        adapter.apply {
            add(helloSection)
            if (!SharedPreferenceManager.isMindUnlocked() || !SharedPreferenceManager.isBodyUnlocked())
                add(unlockSection)

            add(taskForTheDaySection)
            add(talkToExpertsSection)
        }
    }

}