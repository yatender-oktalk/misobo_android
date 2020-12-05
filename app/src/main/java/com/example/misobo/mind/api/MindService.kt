package com.example.misobo.mind.api

import com.example.misobo.mind.models.MusicResponseModel
import com.example.misobo.mind.models.ProgressPayload
import com.example.misobo.mind.models.ProgressResponse
import com.example.misobo.utils.SharedPreferenceManager
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
    ): Observable<List<MusicResponseModel.MusicModel>>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @PATCH("api/music/{musicId}/progress")
    fun updateProgress(
        @Path(value = "musicId") musicId: Int,
        @Body progressPayload: ProgressPayload
    ): Observable<ProgressResponse>

    object Creator {
        private const val url: String = "http://143.110.176.70:4000/"
        private val token = SharedPreferenceManager.getUser()?.data?.token ?: ""
        val service: MindService
            get() {
                val retrofit = Retrofit.Builder()
                    .baseUrl(url)
                    .client(
                        OkHttpClient.Builder()
                            .addNetworkInterceptor(StethoInterceptor())
                            .addInterceptor {
                                it.proceed(
                                    it.request().newBuilder()
                                        .addHeader(
                                            "token",
                                            token
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