package com.example.misobo.mind.view

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
import com.example.misobo.mind.models.ProgressPayload
import com.example.misobo.mind.viewModels.MindViewModel
import com.example.misobo.utils.SharedPreferenceManager
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_exo_player_controls.view.*
import kotlinx.android.synthetic.main.fragment_music_player.*
import java.util.concurrent.TimeUnit

class MusicPlayerFragment : Fragment() {

    private val mindViewModel: MindViewModel by activityViewModels()
    private val compositeDisposable = CompositeDisposable()
    lateinit var simpleExoPlayer: SimpleExoPlayer
    var musicId: Int? = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_music_player, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.bottomNavigationView?.visibility = View.GONE
        activity?.arcSeparator?.visibility = View.GONE
        activity?.arc?.visibility = View.GONE

        mindViewModel.playMusicLiveData.observe(viewLifecycleOwner, Observer { musicModel ->
            mindViewModel.selectedMusicId = musicModel.id ?: 0
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
                    exoPlayer.progressBar.progress = (it * 100 / simpleExoPlayer.duration).toInt()
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
                val currTime = String.format(
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(progressToMilli),
                    TimeUnit.MILLISECONDS.toSeconds(progressToMilli) -
                            TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(
                                    progressToMilli
                                )
                            )
                )
                Log.i("currTime", currTime)
                exoPlayer.exoPosition.text = currTime
            }
            exoPlayer.progressBar.onStopTrackingTouch = stopTrackingListener
            exoPlayer.progressBar.onStartTrackingTouch = startTrackingListener
            exoPlayer.progressBar.onProgressChangedListener = progressChangedListener
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
        updateProgress()
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.bottomNavigationView?.visibility = View.VISIBLE
        activity?.arcSeparator?.visibility = View.VISIBLE
        activity?.arc?.visibility = View.VISIBLE
    }

    private fun updateProgress() {
        val progressToMilli = simpleExoPlayer.currentPosition
        val progressInSec = TimeUnit.MILLISECONDS.toSeconds(progressToMilli)
        val durationInSec = TimeUnit.MILLISECONDS.toSeconds(simpleExoPlayer.duration)
        val progress =
            progressInSec.toFloat().div(durationInSec.toFloat()).times(100)
        mindViewModel.updateProgress(
            mindViewModel.selectedMusicId,
            ProgressPayload(
                userId = SharedPreferenceManager.getUser()?.data?.userId,
                progress = TimeUnit.MILLISECONDS.toSeconds(progressToMilli)
            ),mindViewModel.playMusicLiveData.value?.karma
        )
        //if (progress > 95)
            //mindViewModel.congratsListenerLiveData.postValue(mindViewModel.playMusicLiveData.value?.karma)
    }
}