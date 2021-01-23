package com.example.misobo.myProfile

import com.example.misobo.mind.api.MindService
import com.example.misobo.mind.models.MusicResponseModel
import com.example.misobo.utils.SharedPreferenceManager
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ProfileService {

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("api/user/{userId}")
    fun getProfile(
        @Path(value = "userId") userId: Int
    ): Observable<ProfileResponseModel>

    object Creator {
        private const val url: String = "http://143.110.176.70:4000/"
        private val token = SharedPreferenceManager.getUser()?.data?.token ?: ""
        val service: ProfileService
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
                return retrofit.create(ProfileService::class.java)
            }
    }
}