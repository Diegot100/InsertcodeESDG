package com.example.insertcodeesalasdg.network

import com.example.insertcodeesalasdg.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object SupabaseClient {

    val api: SupabaseApi by lazy {
        val http = OkHttpClient.Builder()
            .addInterceptor { cadena ->
                val original = cadena.request()
                val builder = original.newBuilder()
                    .header("apikey", BuildConfig.SUPABASE_ANON_KEY)
                val token = SesionSupabase.accessToken
                if (token != null && original.header("Authorization") == null) {
                    builder.header("Authorization", "Bearer $token")
                }
                cadena.proceed(builder.build())
            }
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(BuildConfig.SUPABASE_URL.trimEnd('/') + "/")
            .client(http)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SupabaseApi::class.java)
    }
}