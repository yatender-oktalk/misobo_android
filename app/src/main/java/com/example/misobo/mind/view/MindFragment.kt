package com.example.misobo.mind.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.misobo.R
import com.example.misobo.mind.items.HelloItem
import com.example.misobo.mind.items.TasksForTheDayItems
import com.example.misobo.mind.viewModels.MindViewModel
import com.example.misobo.mind.viewModels.MusicFetchState
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_mind.*

class MindFragment : Fragment() {

    private val mindViewModel: MindViewModel by activityViewModels()
    private val adapter = GroupAdapter<ViewHolder>()
    private val helloSection = Section()
    private val taskForTheDaySection = Section()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mind, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mindHomeRecyclerView.adapter = adapter
        helloSection.add(HelloItem())
        mindViewModel.fetchAllMusic()
        mindViewModel.musicLiveData.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is MusicFetchState.Success -> {
                    taskForTheDaySection.add(TasksForTheDayItems(state.musicEntries))
                    Log.i("state", state.musicEntries.entries?.size.toString())
                }
                is MusicFetchState.Loading -> {

                }
                is MusicFetchState.Error -> {

                }
            }
        })

        adapter.apply {
            add(helloSection)
            add(taskForTheDaySection)
        }
    }
}