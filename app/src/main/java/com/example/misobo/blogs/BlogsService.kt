package com.example.misobo.blogs

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

interface BlogsService {

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("api/blogs")
    fun fetchAllBlogs(
    ): Observable<BlogsModel>

    object Creator {
        private const val url: String = "http://143.110.176.70:4000/"
        private val token = SharedPreferenceManager.getUser()?.data?.token ?: ""
        val service: BlogsService
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
                return retrofit.create(BlogsService::class.java)
            }
    }

}