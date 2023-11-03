package com.munmundev.feri_aplikasipembayaraniuranair.data.database.api

import com.munmundev.feri_aplikasipembayaraniuranair.data.model.ResponseModel
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.UsersModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    //GET
    @GET("iuran-kebersihan/api/get.php")
    fun cekUser(@Query("cek_user") cekUser: String,
                @Query("username") username: String,
                @Query("password") password: String
    ): Call<ArrayList<UsersModel>>

    @GET("iuran-kebersihan/api/get.php")
    fun cekUserRegistrasi(@Query("cek_user_registrasi") cek_user_registrasi: String,
                          @Query("username") username: String
    ): Call<ArrayList<UsersModel>>

    @GET("iuran-kebersihan/api/get.php")
    fun getAllUser(@Query("all_user") allUser: String
    ): Call<ArrayList<UsersModel>>

    // POST
    @FormUrlEncoded
    @POST("iuran-kebersihan/api/post.php")
    fun addUser(@Field("addUser") addUser:String,
                @Field("nama") nama:String,
                @Field("alamat") alamat:String,
                @Field("nomorHp") nomorHp:String,
                @Field("username") username:String,
                @Field("password") password:String,
                @Field("sebagai") sebagai:String
    ):Call<ResponseModel>

    @FormUrlEncoded
    @POST("iuran-kebersihan/api/post.php")
    fun postAdminUpdateUser(@Field("update_akun") update_akun: String,
                            @Field("id_user") id_user: String,
                            @Field("nama") nama: String,
                            @Field("alamat") alamat: String,
                            @Field("nomor_hp") nomor_hp: String,
                            @Field("username") username: String,
                            @Field("password") password: String
    ):Call<ResponseModel>

    @FormUrlEncoded
    @POST("iuran-kebersihan/api/post.php")
    fun postAdminHapusUser(@Field("admin_hapus_user") admin_hapus_user: String,
                           @Field("id_user") id_user: String
    ):Call<ResponseModel>

}