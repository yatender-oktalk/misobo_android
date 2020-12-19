package com.example.misobo.utils

import com.example.misobo.Misobo

object ErrorHandler {

    fun handleError(error: Throwable): String {
        return when (error) {
            is com.jakewharton.retrofit2.adapter.rxjava2.HttpException -> {
                when (error.code()) {
                    401, 403 -> {
                        SharedPreferenceManager.clear()
                        Misobo.authRelay.onNext(AuthState.FAILED)
                    }
                }
                "Please try again"
            }
            else -> {
                "Please try again"
            }
        }
    }
}