package com.munmundev.feri_aplikasipembayaraniuranair.data.database.api

import com.munmundev.feri_aplikasipembayaraniuranair.data.model.ResponseModel
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.UsersModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    //GET
    @GET("iuran-kebersihan/api/get.php")
    fun cekUser(@Query("cek_user") cekUser: String?=null,
                @Query("username") username: String?=null,
                @Query("password") password: String?=null
    ): Call<ArrayList<UsersModel>>

    @GET("iuran-kebersihan/api/get.php")
    fun cekUserRegistrasi(@Query("cek_user_registrasi") cek_user_registrasi: String?=null,
                          @Query("username") username: String?=null
    ): Call<ArrayList<UsersModel>>

    @GET("iuran-kebersihan/api/get.php")
    fun getAllUser(@Query("all_user") allUser: String?=null
    ): UsersModel



    // POST
    @FormUrlEncoded
    @POST("iuran-kebersihan/api/post.php")
    fun registerUser(@Field("registerUser") registerUser:String?=null,
                     @Field("nama") nama:String?=null,
                     @Field("alamat") alamat:String?=null,
                     @Field("nomorHp") nomorHp:String?=null,
                     @Field("username") username:String?=null,
                     @Field("password") password:String?=null,
                     @Field("sebagai") sebagai:String?=null
    )
    :Call<ResponseModel>

}