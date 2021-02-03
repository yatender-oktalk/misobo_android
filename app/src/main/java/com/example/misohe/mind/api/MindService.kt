package com.example.misohe.mind.api

import com.example.misohe.BuildConfig
import com.example.misohe.mind.models.*
import com.example.misohe.utils.SharedPreferenceManager
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface MindService {

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("api/music")
    fun fetchAllMusic(
        @Query(value = "page") page: Int
    ): Observable<MusicResponseModel>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @PATCH("api/music/{musicId}/progress")
    fun updateProgress(
        @Path(value = "musicId") musicId: Int,
        @Body progressPayload: ProgressPayload
    ): Observable<ProgressResponse>

    object Creator {
        private val token = SharedPreferenceManager.getUser()?.data?.token ?: ""
        val service: MindService
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
                return retrofit.create(MindService::class.java)
            }
    }
}