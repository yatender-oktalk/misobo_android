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
        compositeDisposable.add(
            networkService.fetchBookings(pageNumber = 1, userId = userId)
                .subscribe({ bookings ->
                    callback.onResult(
                        bookings.data?.entries!!,
                        null,
                        if (bookings.data.pageNumber!! < bookings.data.totalPages!!) 2 else null

                    )
                    //userBookingsLiveData.postValue(it)
                }, {})
        )
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, UserBookings.Entry>
    ) {
        compositeDisposable.add(networkService.fetchBookings(
            pageNumber = params.key,
            userId = userId
        )
            .subscribe { bookings ->
                callback.onResult(
                    bookings.data?.entries!!,
                    if (bookings.data.pageNumber!! < bookings.data.totalPages!!) params.key + 1 else null
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