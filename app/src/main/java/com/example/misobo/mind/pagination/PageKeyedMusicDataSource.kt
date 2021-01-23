package com.example.misobo.mind.pagination

import androidx.paging.PageKeyedDataSource
import com.example.misobo.mind.api.MindService
import com.example.misobo.mind.models.MusicResponseModel
import io.reactivex.disposables.CompositeDisposable

class PageKeyedMusicDataSource(
    private val compositeDisposable: CompositeDisposable,
    private val networkService: MindService,
    private val userId: String
) : PageKeyedDataSource<Int, MusicResponseModel.MusicModel>() {

    override fun loadInitial(
        params: PageKeyedDataSource.LoadInitialParams<Int>,
        callback: PageKeyedDataSource.LoadInitialCallback<Int, MusicResponseModel.MusicModel>
    ) {
        //compositeDisposable.add(networkService.fetchBookings())
        compositeDisposable.add(
            networkService.fetchAllMusic(page = 1)
                .subscribe({ music ->
                    callback.onResult(
                        music.entries?.toMutableList()!!,
                        null,
                        if (music.pageNumber!! < music.totalPages!!) 2 else null
                    )
                    //userBookingsLiveData.postValue(it)
                }, {})
        )
    }

    override fun loadAfter(
        params: PageKeyedDataSource.LoadParams<Int>,
        callback: PageKeyedDataSource.LoadCallback<Int, MusicResponseModel.MusicModel>
    ) {
        compositeDisposable.add(networkService.fetchAllMusic(page = params.key)
            .subscribe { music ->
                callback.onResult(
                    music.entries?.toMutableList()!!,
                    if (music.pageNumber!! < music.totalPages!!) params.key + 1 else null
                )
                //userBookingsLiveData.postValue(it)
            })
    }

    override fun loadBefore(
        params: PageKeyedDataSource.LoadParams<Int>,
        callback: PageKeyedDataSource.LoadCallback<Int, MusicResponseModel.MusicModel>
    ) {
    }
}

