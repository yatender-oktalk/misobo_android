package com.example.misobo.talkToExperts.pagination

import androidx.paging.PageKeyedDataSource
import com.example.misobo.talkToExperts.api.ExpertsService
import com.example.misobo.talkToExperts.models.UserBookings
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PageKeyedBookingsDataSource(
    private val compositeDisposable: CompositeDisposable,
    private val networkService: ExpertsService,
    private val userId: String
) : PageKeyedDataSource<Int, UserBookings.Entry>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, UserBookings.Entry>
    ) {
        //compositeDisposable.add(networkService.fetchBookings())
        compositeDisposable.add(networkService.fetchBookings(pageNumber = 1, userId = userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { bookings ->
                callback.onResult(
                    bookings.data?.entries?.toMutableList() ?: mutableListOf(),
                    null,
                    2
                )
                //userBookingsLiveData.postValue(it)
            })
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, UserBookings.Entry>
    ) {
        compositeDisposable.add(networkService.fetchBookings(
            pageNumber = params.key,
            userId = userId
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { bookings ->
                callback.onResult(
                    bookings.data?.entries?.toMutableList() ?: mutableListOf(),
                    params.key + 1
                )
                //userBookingsLiveData.postValue(it)
            })
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, UserBookings.Entry>
    ) {
    }
}