package com.munmundev.feri_aplikasipembayaraniuranair.data.database.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig2 {
//    private const val BASE_URL = "https://aplikasitugas17.000webhostapp.com/"
//    private const val BASE_URL = "http://192.168.1.4/"
//    private const val BASE_URL = "http://192.168.1.7/"
//    private const val BASE_URL = "http://192.168.1.11/"
//    private const val BASE_URL = "http://192.168.1.18/"
//    private const val BASE_URL = "http://127.0.0.1/"
    private const val BASE_URL = "http://192.168.1.3"

    fun getRetrofit(): ApiService{
        val gson = GsonBuilder().create()
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(ApiService::class.java)
    }
}