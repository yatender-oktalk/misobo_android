package com.example.misobo.mind.view

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.misobo.R
import com.example.misobo.mind.viewModels.MindViewModel
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.fragment_music_player.*

class MusicPlayerFragment : Fragment() {

    private val mindViewModel: MindViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_music_player, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mindViewModel.playMusicLiveData.observe(viewLifecycleOwner, Observer { musicModel ->

            musicModel.title?.let {
                songTitle.text = it
            }

            musicModel.productionName?.let { name ->
                productionName.text = name
            }

            //Set up exo Player
            exoPlayer.controllerShowTimeoutMs = 0
            exoPlayer.cameraDistance = 30f
            val simpleExoPlayer = SimpleExoPlayer.Builder(requireContext()).build()
            exoPlayer.player = simpleExoPlayer
            val dataSourceFactory =
                DefaultDataSourceFactory(
                    requireContext(),
                    Util.getUserAgent(requireContext(), "app")
                )

            //https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3
            val audioSource =
                ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(
                        Uri
                            .parse(musicModel.url)
                    )

            simpleExoPlayer.prepare(audioSource)
            breatheAnimation.playAnimation()
            simpleExoPlayer.clearVideoSurface()
            simpleExoPlayer.playWhenReady = true

            simpleExoPlayer.addListener(object :Player.DefaultEventListener(){
                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    //super.onPlayerStateChanged(playWhenReady, playbackState)
                    if (playWhenReady && playbackState == Player.STATE_READY) {
                        breatheAnimation.resumeAnimation()
                        // media actually playing
                    } else if (playWhenReady) {
                        breatheAnimation.pauseAnimation()
                        // might be idle (plays after prepare()),
                        // buffering (plays when data available)
                        // or ended (plays when seek away from end)
                    } else {
                        breatheAnimation.pauseAnimation()
                        // player paused in any state
                    }
                }
            })
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        breatheAnimation?.cancelAnimation()
        exoPlayer.player?.stop()

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}