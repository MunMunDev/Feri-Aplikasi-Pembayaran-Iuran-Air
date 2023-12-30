package com.munmundev.feri_aplikasipembayaraniuranair.data.database.api

import com.munmundev.feri_aplikasipembayaraniuranair.data.model.BiayaModel
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.PembayaranModel
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.PerumahanModel
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.ResponseModel
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.UsersModel
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    //GET
    // User
    @GET("iuran-kebersihan/api/get.php")
    suspend fun cekUser(@Query("cek_user") cekUser: String,
                        @Query("username") username: String,
                        @Query("password") password: String
    ): ArrayList<UsersModel>

    @GET("iuran-kebersihan/api/get.php")
    suspend fun cekUserRegistrasi(@Query("cek_user_registrasi") cek_user_registrasi: String,
                          @Query("username") username: String
    ): ArrayList<UsersModel>

    @GET("iuran-kebersihan/api/get.php")
    suspend fun getPembayaranUser(@Query("user_pembayaran") user_pembayaran: String,
                                  @Query("id_user") id_user: String
    ): ArrayList<PembayaranModel>

    @GET("iuran-kebersihan/api/get.php")
    suspend fun getRiwayatPembayaranUser(@Query("riwayat_pembayaran_user") riwayat_pembayaran_user: String,
                                  @Query("id_user") id_user: String
    ): ArrayList<PembayaranModel>

    @GET("iuran-kebersihan/api/get.php")
    suspend fun getAllUser(@Query("all_user") allUser: String
    ): ArrayList<UsersModel>

    @GET("iuran-kebersihan/api/get.php")
    suspend fun getAllPerumahan(@Query("get_all_perumahan") get_all_perumahan: String
    ): ArrayList<PerumahanModel>

    @GET("iuran-kebersihan/api/get.php")
    suspend fun getBlokPerumahan(@Query("get_blok_perumahan") get_blok_perumahan: String,
                                 @Query("id_perumahan") id_perumahan: String
    ): ArrayList<PerumahanModel>

    @GET("iuran-kebersihan/api/get.php")
    suspend fun getAllBlokPerumahan(
        @Query("get_all_blok_perumahan") get_all_blok_perumahan: String
    ): ArrayList<PerumahanModel>

    @GET("iuran-kebersihan/api/get.php")
    suspend fun getAdminListUserPembayaran(@Query("get_user_by_blok_perumahan") get_user_by_blok_perumahan: String,
                                   @Query("id_blok") id_blok: String
    ): ArrayList<UsersModel>

    @GET("iuran-kebersihan/api/get.php")
    suspend fun getAdminBiaya(@Query("get_admin_biaya") get_admin_biaya: String
    ): ArrayList<BiayaModel>

//    // POST
//    // Akun
    @FormUrlEncoded
    @POST("iuran-kebersihan/api/post.php")
    suspend fun addUser(
        @Field("add_user") addUser:String,
        @Field("nama") nama:String,
        @Field("id_blok") idBlokPerumahan:String,
        @Field("alamat") alamat:String,
        @Field("nomor_hp") nomorHp:String,
        @Field("username") username:String,
        @Field("password") password:String,
        @Field("sebagai") sebagai:String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("iuran-kebersihan/api/post.php")
    suspend fun postUpdateUser(
        @Field("update_akun") update_akun: String,
        @Field("id_user") id_user: String,
        @Field("nama") nama: String,
        @Field("id_blok") id_blok: String,
        @Field("no_alamat") no_alamat: String,
        @Field("nomor_hp") nomor_hp: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("username_lama") usernameLama: String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("iuran-kebersihan/api/post.php")
    suspend fun postHapusUser(
        @Field("hapus_user") hapus_user: String,
        @Field("id_user") id_user: String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("iuran-kebersihan/api/post.php")
    suspend fun postAdminHapusPembayaran(@Field("admin_hapus_pembayaran") admin_hapus_pembayaran: String,
                                         @Field("id_pembayaran") id_pembayaran: String
    ):ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("iuran-kebersihan/api/post.php")
    suspend fun postAdminUpdatePembayaran(
        @Field("admin_update_pembayaran") admin_update_pembayaran: String,
        @Field("id_pembayaran") id_pembayaran: String,
        @Field("harga") harga: String,
        @Field("denda") denda: String,
        @Field("biaya_admin") biaya_admin: String,
        @Field("tenggat_waktu") tenggat_waktu: String,
        @Field("waktu_pembayaran") waktu_pembayaran: String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("iuran-kebersihan/api/post.php")
    suspend fun postAdminKonfirmasiPembayaran(
        @Field("admin_konfirmasi_pembayaran") admin_konfirmasi_pembayaran: String,
        @Field("id_pembayaran") id_pembayaran: String,
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("iuran-kebersihan/api/post.php")
    suspend fun postAdminKonfirmasiAllPembayaran(
        @Field("admin_konfirmasi_all_pembayaran") admin_konfirmasi_all_pembayaran: String,
        @Field("id_user") id_user: String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("iuran-kebersihan/api/post.php")
    suspend fun postAdminTambahPembayaran(
        @Field("post_admin_tambah_pembayaran") post_admin_tambah_pembayaran:String,
        @Field("id_user") id_user:String,
        @Field("harga") harga:String,
        @Field("denda") denda:String,
        @Field("biaya_admin") biaya_admin:String,
        @Field("tenggat_waktu") tenggat_waktu:String,
        @Field("waktu_pembayaran") waktu_pembayaran:String
    ):ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("iuran-kebersihan/api/post.php")
    suspend fun postAdminTambahSemuaPembayaranBulanIni(
        @Field("admin_tambah_pembayaran_bulan_ini") admin_tambah_pembayaran_bulan_ini: String
    ):ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("iuran-kebersihan/api/post.php")
    suspend fun postRegistrasiPembayaran(
        @Field("registrasi_pembayaran") registrasiPembayaran:String,
        @Field("order_id") order_id:String,
        @Field("id_user") id_user:String,
        @Field("keterangan") keterangan:String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("iuran-kebersihan/api/post.php")
    suspend fun postDefaultPembayaran(
        @Field("default_pembayaran_user") default_pembayaran_user:String,
        @Field("id_user") id_user:String
        ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("iuran-kebersihan/api/post.php")
    suspend fun postEnamBulanPembayaran(
        @Field("enam_bulan_pembayaran_user") enam_bulan_pembayaran_user:String,
        @Field("id_user") id_user:String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("iuran-kebersihan/api/post.php")
    suspend fun postDuaBelasBulanPembayaran(
        @Field("duabelas_bulan_pembayaran_user") duabelas_bulan_pembayaran_user:String,
        @Field("id_user") id_user:String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("iuran-kebersihan/api/post.php")
    suspend fun postTambahPerumahan(
        @Field("tambah_perumahan") tambah_perumahan:String,
        @Field("nama_perumahan") nama_perumahan:String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("iuran-kebersihan/api/post.php")
    suspend fun postUpdatePerumahan(
        @Field("update_perumahan") update_perumahan:String,
        @Field("id_perumahan") id_perumahan:String,
        @Field("nama_perumahan") nama_perumahan:String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("iuran-kebersihan/api/post.php")
    suspend fun postHapusPerumahan(
        @Field("hapus_perumahan") hapus_perumahan:String,
        @Field("id_perumahan") id_perumahan:String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("iuran-kebersihan/api/post.php")
    suspend fun postTambahBlokPerumahan(
        @Field("tambah_blok_perumahan") tambah_blok_perumahan:String,
        @Field("id_perumahan") id_perumahan:String,
        @Field("nama_blok_perumahan") nama_blok_perumahan:String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("iuran-kebersihan/api/post.php")
    suspend fun postUpdateBlokPerumahan(
        @Field("update_blok_perumahan") update_blok_perumahan:String,
        @Field("id_perumahan") id_perumahan:String,
        @Field("id_blok") id_blok:String,
        @Field("nama_blok_perumahan") nama_blok_perumahan:String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("iuran-kebersihan/api/post.php")
    suspend fun postHapusBlokPerumahan(
        @Field("hapus_blok_perumahan") hapus_blok_perumahan:String,
        @Field("id_blok") id_blok:String
    ): ArrayList<ResponseModel>

    @FormUrlEncoded
    @POST("iuran-kebersihan/api/post.php")
    suspend fun postUpdateBiaya(
        @Field("update_biaya") update_biaya:String,
        @Field("biaya") biaya:String,
        @Field("denda") denda:String,
        @Field("biaya_admin") biaya_admin:String
    ): ArrayList<ResponseModel>
}