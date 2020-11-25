package com.example.misobo.mind.view

import android.icu.util.TimeUnit
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.misobo.R
import com.example.misobo.arcseekbar.ProgressListener
import com.example.misobo.mind.viewModels.MindViewModel
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.custom_exo_player_controls.view.*
import kotlinx.android.synthetic.main.fragment_music_player.*

class MusicPlayerFragment : Fragment() {

    private val mindViewModel: MindViewModel by activityViewModels()
    private val compositeDisposable = CompositeDisposable()
    lateinit var simpleExoPlayer: SimpleExoPlayer

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
            simpleExoPlayer = SimpleExoPlayer.Builder(requireContext()).build()
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
            simpleExoPlayer.addListener(object : Player.DefaultEventListener() {
                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    if (playWhenReady && playbackState == Player.STATE_READY) {
                        breatheAnimation.resumeAnimation()
                        // media actually playing
                    } else {
                        breatheAnimation.pauseAnimation()
                        // player paused in any state
                    }
                }
            })

            var pause = true

            val playbackProgressObservable: Observable<Long> =
                Observable.interval(50, java.util.concurrent.TimeUnit.MILLISECONDS)
                    .filter { pause }
                    .map { simpleExoPlayer.currentPosition }

            compositeDisposable.add(playbackProgressObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    exoPlayer.arcSeekBar.progress = (it * 100 / simpleExoPlayer.duration).toInt()
                })

            val stopTrackingListener =
                ProgressListener { progress: Int ->
                    pause = true
                    simpleExoPlayer.seekTo(progress * simpleExoPlayer.duration / 100)
                }

            val startTrackingListener =
                ProgressListener { progress: Int ->
                    pause = false
                }

            val progressChangedListener = ProgressListener { progress ->
                val progressToMilli = progress * simpleExoPlayer.duration / 100
                val  currTime = String.format("%02d:%02d",
                java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(progressToMilli),
                java.util.concurrent.TimeUnit.MILLISECONDS.toSeconds(progressToMilli) -
                        java.util.concurrent.TimeUnit.MINUTES.toSeconds(java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(progressToMilli)))
                Log.i("currTime" , currTime)
                exoPlayer.exoPosition.text = currTime
            }

            exoPlayer.arcSeekBar.onStopTrackingTouch = stopTrackingListener
            exoPlayer.arcSeekBar.onStartTrackingTouch = startTrackingListener
            exoPlayer.arcSeekBar.onProgressChangedListener = progressChangedListener
        })

        crossIcon.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        breatheAnimation?.cancelAnimation()
        exoPlayer.player?.stop()
        compositeDisposable.dispose()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}