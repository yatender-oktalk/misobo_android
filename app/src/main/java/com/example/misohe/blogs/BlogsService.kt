package com.example.misohe.blogs

import com.example.misohe.BuildConfig
import com.example.misohe.utils.SharedPreferenceManager
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface BlogsService {

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("api/blogs")
    fun fetchAllBlogs(): Observable<BlogsModel>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("api/blogs/{blog-id}")
    fun getDetailBlog(@Path("blog-id") blogId: Int): Observable<BlogsModel.Blogs>

    object Creator {
        private val token = SharedPreferenceManager.getUser()?.data?.token ?: ""
        val service: BlogsService
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
                return retrofit.create(BlogsService::class.java)
            }
    }

}