package com.example.misohe.bmi.api

import com.example.misohe.BuildConfig
import com.example.misohe.bmi.models.BmiRequestBody
import com.example.misohe.bmi.models.BmiResponsebody
import com.example.misohe.utils.SharedPreferenceManager
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface BmiService {

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("api/user/{id}/bmi")
    fun saveBmi(
        @Body bmiRequestBody: BmiRequestBody,
        @Path(value = "id") registrationId: Int
    ): Observable<BmiResponsebody>

    object Creator {
        private val token = SharedPreferenceManager.getUser()?.data?.token ?: ""
        val service: BmiService
            get() {
                val retrofit = Retrofit.Builder()
                    .baseUrl(BuildConfig.MISOBO_BASE_URL)
                    .client(
                        OkHttpClient.Builder()
                            .addNetworkInterceptor(StethoInterceptor())
                            .addInterceptor {
                                it.proceed(
                                    it.request().newBuilder()
                                        .addHeader(
                                            "token",
                                            SharedPreferenceManager.getUser()?.data?.token ?: ""
                                        )
                                        .build()
                                )
                            }
                            .build()
                    )
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
                return retrofit.create(BmiService::class.java)
            }
    }
}