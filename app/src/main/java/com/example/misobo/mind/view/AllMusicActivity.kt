package com.example.misobo.mind.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.misobo.R
import com.example.misobo.mind.pagination.MusicGridListAdapter
import com.example.misobo.mind.viewModels.MindViewModel
import com.example.misobo.utils.SpacesItemDecoration
import com.example.misobo.utils.Util
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

        mindViewModel.musicPagedList.observe(this, Observer { pagedList ->
            musicListAdapter.submitList(pagedList)
        })
    }
}