package com.example.misohe.talkToExperts.pagination

import androidx.paging.DataSource
import com.example.misohe.talkToExperts.api.ExpertsService
import com.example.misohe.talkToExperts.models.UserBookings
import io.reactivex.disposables.CompositeDisposable

class BookingsDataSourceFactory(
    private val compositeDisposable: CompositeDisposable,
    private val networkService: ExpertsService,
    private val userId: String,
    private val showAllBookings:Boolean
) : DataSource.Factory<Int, UserBookings.Entry>() {

    override fun create(): DataSource<Int, UserBookings.Entry> {
        return PageKeyedBookingsDataSource(
            compositeDisposable = compositeDisposable,
            networkService = networkService,
            userId = userId,
            showAllBookings = showAllBookings
        )
    }
}