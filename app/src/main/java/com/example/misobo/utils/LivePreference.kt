package com.example.misobo.utils

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class LivePreference<T> constructor(
    private val updates: Observable<String>,
    private val preference: SharedPreferences,
    private val key: String
) : MutableLiveData<T>() {

    private var disposable: Disposable? = null

    override fun onActive() {
        super.onActive()

        value = (preference.all[key] as? T)

        disposable = updates.filter { t -> t == key }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<String>() {
                override fun onComplete() {
                }

                override fun onNext(t: String) {
                    postValue((preference.all[t] as T))
                }

                override fun onError(e: Throwable) {
                }
            })
    }

    override fun onInactive() {
        super.onInactive()
        disposable?.dispose()
    }
}