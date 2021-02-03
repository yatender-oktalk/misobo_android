package com.example.misohe.utils

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    private var response: Response? = null

    override fun intercept(chain: Interceptor.Chain) : Response{
        var request = chain.request()
        request = if (SharedPreferenceManager.getUser()?.data?.token != null) {
            request.newBuilder()
                .addHeader(
                    "token",
                    SharedPreferenceManager.getUser()?.data?.token ?: ""
                ).build()
        } else {
            request.newBuilder().build()
        }

        response = chain.proceed(request)
        return response!!
    }
}