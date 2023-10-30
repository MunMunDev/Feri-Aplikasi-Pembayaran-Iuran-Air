package com.munmundev.feri_aplikasipembayaraniuranair.data.model

import com.google.gson.annotations.SerializedName

class UsersModel(
    @SerializedName("id_user")
    var idUser: String? = null,

    @SerializedName("nama")
    var nama: String? = null,

    @SerializedName("alamat")
    var alamat: String? = null,

    @SerializedName("nomorHp")
    var nomorHp: String? = null,

    @SerializedName("username")
    var username: String? = null,

    @SerializedName("password")
    var password: String? = null,

    @SerializedName("sebagai")
    var sebagai: String? = null
)