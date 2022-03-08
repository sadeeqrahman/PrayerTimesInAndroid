package com.anushka.retrofitdemo

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit


class RetrofitInstanceClient {

    companion object {
        var get_token: String = ""
        val BASE_URL: String = "https://api.aladhan.com/"
        val interceptor = HttpLoggingInterceptor().apply {

            this.level = HttpLoggingInterceptor.Level.BODY

        }

        var logInter = HttpLoggingInterceptor()
        val client = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
                .connectTimeout(180, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .writeTimeout(180, TimeUnit.SECONDS)

        }.build()



        val token_client = OkHttpClient.Builder().apply {
            this.addInterceptor(HeaderInterceptor())
                .addNetworkInterceptor(interceptor)
                .addNetworkInterceptor(logInter)
                .protocols(Arrays.asList(Protocol.HTTP_1_1))
                .connectTimeout(180, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .writeTimeout(180, TimeUnit.SECONDS)

        }.build()


        fun getRetrofitInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
        }

        fun getHeader_RetrofitInstance(token: String): Retrofit {
            this.get_token =  token
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(token_client)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()

        }


    }

    class HeaderInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val request: Request = chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer $get_token")
                .build()
            return chain.proceed(request)
        }
    }




}