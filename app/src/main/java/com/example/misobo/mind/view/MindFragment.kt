package com.example.misobo.mind.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.misobo.LoadingFragment
import com.example.misobo.R
import com.example.misobo.blogs.BlogDetailedFetchState
import com.example.misobo.blogs.BlogsDetailFragment
import com.example.misobo.blogs.BlogsFetchState
import com.example.misobo.blogs.BlogsViewModel
import com.example.misobo.bmi.view.BmiActivity
import com.example.misobo.mind.items.*
import com.example.misobo.mind.viewModels.MindViewModel
import com.example.misobo.mind.viewModels.MusicFetchState
import com.example.misobo.myProfile.FetchState
import com.example.misobo.myProfile.ProfileViewModel
import com.example.misobo.talkToExperts.view.BookASlotDialog
import com.example.misobo.talkToExperts.view.PaymentActivity
import com.example.misobo.talkToExperts.view.TalkToExpertActivity
import com.example.misobo.talkToExperts.viewModels.ExpertListState
import com.example.misobo.talkToExperts.viewModels.TalkToExpertsViewModel
import com.example.misobo.utils.SharedPreferenceManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_mind.*

class MindFragment : Fragment() {

    private val mindViewModel: MindViewModel by activityViewModels()
    private val blogsViewModel: BlogsViewModel by activityViewModels()
    private val talkToExpertsViewModel: TalkToExpertsViewModel by activityViewModels()
    private val profileViewModel by lazy { ViewModelProvider(this).get(ProfileViewModel::class.java) }
    private val loadingFragment = LoadingFragment()

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
        val blogsSection = Section()

        mindHomeRecyclerView.adapter = adapter

        fillName(helloSection)

        unlockSection.add(UnlockItems() {
            if (it)
                startActivity(Intent(context, BmiActivity::class.java))
            else {
                val jsonObject = JsonObject()
                jsonObject.addProperty("is_mind_pack_unlocked", true)
                mindViewModel.updatePackUnlock(
                    SharedPreferenceManager.getUser()?.data?.userId ?: 0,
                    jsonObject
                )
                showSnackBar()
            }
            adapter.remove(unlockSection)
        })

        mindViewModel.getCoinsLiveData().observe(viewLifecycleOwner, Observer { response ->
            fillName(helloSection)
        })

        profileViewModel.nameLiveData.observe(requireActivity(), Observer { state ->
            when (state) {
                is FetchState.Success -> {
                    activity?.supportFragmentManager?.popBackStack()

                    /* val fragment: Fragment? =
                         activity?.supportFragmentManager?.findFragmentByTag("LOADING")
                     if (fragment != null) activity?.supportFragmentManager?.beginTransaction()
                         ?.remove(fragment)?.commit()*/
                }
                is FetchState.Loading -> {
                    activity?.supportFragmentManager?.beginTransaction()
                        ?.replace(R.id.mainContainer, loadingFragment, "LOADING")
                        ?.addToBackStack(null)?.commit()
                }
                is FetchState.Error -> {
                    activity?.supportFragmentManager?.popBackStack()

                    /*val fragment: Fragment? =
                        activity?.supportFragmentManager?.findFragmentByTag("LOADING")
                    if (fragment != null) activity?.supportFragmentManager?.beginTransaction()
                        ?.remove(fragment)?.commit()*/
                }
            }
        })

        if (talkToExpertsViewModel.expertListLiveData.value == null)
            talkToExpertsViewModel.getAllExpertsList()
        talkToExpertsViewModel.expertListLiveData.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is ExpertListState.Success -> {
                    talkToExpertsSection.add(TalkToTherapistItem(state.expertList, {
                        startActivity(Intent(requireContext(), TalkToExpertActivity::class.java))
                    }, {
                        talkToExpertsViewModel.selectedExpertLiveDate.postValue(it)
                        val slotDialog =
                            BookASlotDialog()
                        val bundle = Bundle()
                        it.id?.let { it1 -> bundle.putInt("ID", it1) }
                        slotDialog.arguments = bundle
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.add(slotDialog, null)?.commit()
                    }))
                }
                is ExpertListState.Fail -> {

                }
                is ExpertListState.Loading -> {
                }
            }
        })

        //if (mindViewModel.musicLiveData.value == null)
        mindViewModel.fetchAllMusic()
        mindViewModel.musicLiveData.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is MusicFetchState.Success -> {
                    //adapter.remove(taskForTheDaySection)
                    taskForTheDaySection.add(TasksForTheDayItems(state.musicEntries) { position ->
                        mindViewModel.playMusicLiveData.postValue(
                            state.musicEntries[position]
                        )
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.mainContainer, MusicPlayerFragment())
                            ?.addToBackStack(null)?.commit()
                    })
                    //adapter.up(1,taskForTheDaySection)
                }
                is MusicFetchState.Loading -> {
                }
                is MusicFetchState.Error -> {
                }
            }
        })

        mindViewModel.congratsListenerLiveData.observe(viewLifecycleOwner, Observer { coins ->
            val bundle = Bundle()
            bundle.putString("COINS", coins.toString())
            val congratsFragment = CongratsFragment()
            congratsFragment.arguments = bundle
            activity?.supportFragmentManager?.beginTransaction()
                ?.add(
                    R.id.mainContainer,
                    congratsFragment
                )
                ?.addToBackStack(null)
                ?.commit()
        })

        if (blogsViewModel.blogLiveData.value == null)
            blogsViewModel.fetchBlogs()
        blogsViewModel.blogLiveData.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is BlogsFetchState.Success -> {
                    blogsSection.add(CuratedArticlesItem(state.blogsModel, {
                        blogsViewModel.detailedBlogLiveData.postValue(
                            BlogDetailedFetchState.Success(
                                state.blogsModel.data?.get(it)!!
                            )
                        )
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.mainContainer, BlogsDetailFragment.newInstance(it))
                            ?.addToBackStack(null)?.commit()
                    }, {
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.mainContainer, ArticlesFragment())
                            ?.addToBackStack(null)?.commit()
                    }))
                }
                is BlogsFetchState.Loading -> {
                }
                is BlogsFetchState.Error -> {
                }
            }
        })

        adapter.apply {
            add(helloSection)
            if (SharedPreferenceManager.getUserProfile()?.data?.isMindPackUnlocked == false || SharedPreferenceManager.getUserProfile()?.data?.isBodyPackUnlocked == false)
                add(unlockSection)

            add(taskForTheDaySection)
            add(talkToExpertsSection)
            add(blogsSection)
        }
    }

    fun showSnackBar() {
        val snackBar: Snackbar =
            Snackbar.make(topLevelView, "Mind Pack Unlocked", Snackbar.LENGTH_SHORT)
        val snackBarLayout = snackBar.view
        val textView =
            snackBarLayout.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_outlined_tick, 0, 0, 0)
        textView.compoundDrawablePadding =
            resources.getDimensionPixelOffset(R.dimen.snackbar_icon_padding)
        snackBar.show()
    }
}

fun fillName(helloSection: Section) {
    helloSection.removeHeader()
    helloSection.setHeader(HelloItem(SharedPreferenceManager.getName()) {
        SharedPreferenceManager.setName(it)
        fillName(helloSection)
        /*profileViewModel.updateName(
            SharedPreferenceManager.getUser()?.data?.userId ?: -1,
            namePayload = NamePayload(NamePayload.Data(it))
        )*/
    })
}

