package com.misohe.misohe.mind.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.misohe.misohe.mind.api.MindService
import com.misohe.misohe.mind.models.MusicResponseModel
import com.misohe.misohe.mind.models.OrderResponse
import com.misohe.misohe.mind.models.ProgressPayload
import com.misohe.misohe.mind.pagination.MusicDataSourceFactory
import com.misohe.misohe.myProfile.ProfileService
import com.misohe.misohe.utils.LiveSharedPreference
import com.misohe.misohe.utils.SharedPreferenceManager
import com.misohe.misohe.utils.SingleLiveEvent
import com.google.gson.JsonObject
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MindViewModel : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private var mindService = MindService.Creator.service
    val musicLiveData: SingleLiveEvent<MusicFetchState> = SingleLiveEvent()
    val playMusicLiveData: MutableLiveData<MusicResponseModel.MusicModel> = MutableLiveData()
    val coinsAcquiredLiveData: SingleLiveEvent<Pair<Boolean, Int>> = SingleLiveEvent()
    var selectedMusicId: Int = 0
    private val liveSharedPreference =
        LiveSharedPreference(SharedPreferenceManager.sharedPreferences!!)
    lateinit var musicPagedList: LiveData<PagedList<MusicResponseModel.MusicModel>>
    val packUnlockLiveData: MutableLiveData<Unit> = MutableLiveData()


    init {
        fetchAllMusic()
    }

    fun getCoinsLiveData() = liveSharedPreference

    fun fetchAllMusic() {

        val musicDataSourceFactory = MusicDataSourceFactory(
            compositeDisposable = compositeDisposable,
            networkService = mindService,
            userId = SharedPreferenceManager.getUserProfile()?.data?.id.toString())

        val config = PagedList.Config.Builder()
            .setPageSize(55)
            .setInitialLoadSizeHint(55)
            .setEnablePlaceholders(true)
            .build()

        musicPagedList = LivePagedListBuilder(musicDataSourceFactory, config).build()
    }

    fun updatePackUnlock(userId: Int, jsonObject: JsonObject) {
        compositeDisposable.add(
            ProfileService.Creator.service.updatePack(userId, jsonObject)
                .switchMap { ProfileService.Creator.service.getProfile(userId) }
                .map { profileResponse -> SharedPreferenceManager.setUserProfile(profileResponse)
                    packUnlockLiveData.postValue(Unit) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                },{})
        )
    }

    fun updateProgress(
        musicId: Int,
        progressPayload: ProgressPayload,
        karma: Int?
    ) {
        compositeDisposable.add(mindService.updateProgress(
            musicId,
            progressPayload = progressPayload
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                musicPagedList.value?.dataSource?.invalidate()
                coinsAcquiredLiveData.postValue(
                    Pair(
                        response.pointsAcquired ?: false,
                        karma ?: 100
                    )
                )
            })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}

sealed class MusicFetchState {
    data class Success(val musicEntries: List<MusicResponseModel.MusicModel>) : MusicFetchState()
    object Loading : MusicFetchState()
    data class Error(val message: String) : MusicFetchState()
}

sealed class OrderFetchState {
    data class Success(val orderResponse: OrderResponse) : OrderFetchState()
    object Loading : OrderFetchState()
    data class Error(val message: String) : OrderFetchState()
}