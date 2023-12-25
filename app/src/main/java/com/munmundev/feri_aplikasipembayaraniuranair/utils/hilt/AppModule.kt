package com.munmundev.feri_aplikasipembayaraniuranair.utils.hilt

import com.google.gson.GsonBuilder
import com.munmundev.feri_aplikasipembayaraniuranair.data.database.api.ApiService
import com.munmundev.feri_aplikasipembayaraniuranair.utils.Constant
import com.munmundev.feri_aplikasipembayaraniuranair.utils.HurufAcak
import com.munmundev.feri_aplikasipembayaraniuranair.utils.KonversiRupiah
import com.munmundev.feri_aplikasipembayaraniuranair.utils.TanggalDanWaktu
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun apiConfig(): ApiService = Retrofit.Builder()
        .baseUrl(Constant.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()
        .create(ApiService::class.java)

    @Provides
    @Singleton
    fun tanggalDanWaktu(): TanggalDanWaktu = TanggalDanWaktu()

    @Provides
    @Singleton
    fun rupiah(): KonversiRupiah = KonversiRupiah()

    @Provides
    fun hurufAcak() : HurufAcak = HurufAcak()

}