package com.example.misohe.talkToExperts.pagination

import androidx.paging.PageKeyedDataSource
import com.example.misohe.talkToExperts.api.ExpertsService
import com.example.misohe.talkToExperts.models.UserBookings
import io.reactivex.disposables.CompositeDisposable
import java.text.SimpleDateFormat
import java.util.*

class PageKeyedBookingsDataSource(
    private val compositeDisposable: CompositeDisposable,
    private val networkService: ExpertsService,
    private val userId: String,
    private val showAllBookings: Boolean
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
                        if (showAllBookings) bookings.data?.entries!!
                        else
                            getFilteredList(bookings.data?.entries!!),
                        null,
                        if (bookings.data.pageNumber!! < bookings.data.totalPages!!) 2 else null
                    )
                }, {})
        )
    }

    private fun getFilteredList(entries: List<UserBookings.Entry>): MutableList<UserBookings.Entry> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        val currentDateTime = Date().time
        val filteredEntries = mutableListOf<UserBookings.Entry>()
        entries.forEach {
            val callCompleted =
                currentDateTime.compareTo(dateFormat.parse(it?.endTime).time)
            if ((callCompleted != 1)) {
                filteredEntries.add(it)
            }
        }
        return filteredEntries
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
                    getFilteredList(
                        if (showAllBookings) bookings.data?.entries!!
                        else bookings.data?.entries!!
                    ),
                    if (bookings.data.pageNumber!! < bookings.data.totalPages!!) params.key + 1 else null
                )
            })
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, UserBookings.Entry>
    ) {
    }
}