package com.misohe.misohe.mind.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.misohe.misohe.R
import com.misohe.misohe.mind.pagination.MusicGridListAdapter
import com.misohe.misohe.mind.viewModels.MindViewModel
import com.misohe.misohe.utils.SpacesItemDecoration
import com.misohe.misohe.utils.Util
import kotlinx.android.synthetic.main.activity_all_music.*

class AllMusicActivity : AppCompatActivity() {

    private val mindViewModel: MindViewModel by
    lazy { ViewModelProvider(this).get(MindViewModel::class.java) }

    private lateinit var musicListAdapter: MusicGridListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_music)

        musicListAdapter = MusicGridListAdapter { musicModel, position ->
            mindViewModel.playMusicLiveData.postValue(musicModel)

            supportFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, MusicPlayerFragment())
                .addToBackStack(null).commit()
        }
        allMusicRecycler.adapter = musicListAdapter

        allMusicRecycler.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            addItemDecoration(SpacesItemDecoration(Util.convertDpToPixels(8F, context), 2));
        }

        mindViewModel.coinsAcquiredLiveData.observe(this, Observer { coins ->
            if (coins.first) {
                val bundle = Bundle()
                bundle.putString("COINS", coins.second.toString())
                val congratsFragment = CongratsFragment()
                congratsFragment.arguments = bundle
                supportFragmentManager.beginTransaction()
                    .add(
                        R.id.mainContainer,
                        congratsFragment
                    )
                    .addToBackStack(null)
                    .commit()
            }
        })

        backIcon.setOnClickListener { onBackPressed() }
        mindViewModel.musicPagedList.observe(this, Observer { pagedList ->
            musicListAdapter.submitList(pagedList)
        })
    }
}