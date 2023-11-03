package com.munmundev.feri_aplikasipembayaraniuranair.data.database.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
//    private const val BASE_URL = "https://aplikasitugas17.000webhostapp.com/"
//    private const val BASE_URL = "http://192.168.1.4/"
    private const val BASE_URL = "http://192.168.1.7/"

    fun getRetrofit(): ApiService{
        val gson = GsonBuilder().create()
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(ApiService::class.java)
    }
}