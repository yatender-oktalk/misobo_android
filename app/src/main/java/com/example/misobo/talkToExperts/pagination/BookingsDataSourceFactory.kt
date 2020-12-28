package com.example.misobo.talkToExperts.pagination

import androidx.paging.DataSource
import com.example.misobo.talkToExperts.api.ExpertsService
import com.example.misobo.talkToExperts.models.UserBookings
import io.reactivex.disposables.CompositeDisposable

class BookingsDataSourceFactory(
    private val compositeDisposable: CompositeDisposable,
    private val networkService: ExpertsService,
    private val userId: String
) : DataSource.Factory<Int, UserBookings.Entry>() {

    override fun create(): DataSource<Int, UserBookings.Entry> {
        return PageKeyedBookingsDataSource(
            compositeDisposable = compositeDisposable,
            networkService = networkService,
            userId = userId
        )
    }
}