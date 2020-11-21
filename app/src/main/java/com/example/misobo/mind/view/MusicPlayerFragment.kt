package com.example.misobo.mind.view

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.misobo.R
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.fragment_music_player.*

class MusicPlayerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_music_player, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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
                        .parse("https://s3.ap-south-1.amazonaws.com/misobo.music.mp3/audio_b91b7d4444.mp3")
                )

        simpleExoPlayer.prepare(audioSource)
        simpleExoPlayer.clearVideoSurface()
        simpleExoPlayer.playWhenReady = true
    }
}