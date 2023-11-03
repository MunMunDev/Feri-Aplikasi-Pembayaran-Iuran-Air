package com.munmundev.feri_aplikasipembayaraniuranair.data.model

import com.google.gson.annotations.SerializedName

class PembayaranModel (
    @SerializedName("id_pembayaran")
    var idPembayaran: String? = null,

    @SerializedName("id_user")
    var idUser: String? = null,

    @SerializedName("nama")
    var nama: String? = null,

    @SerializedName("alamat")
    var alamat: String? = null,

    @SerializedName("harga")
    var harga: String? = null,

    @SerializedName("denda")
    var denda: String? = null,

    @SerializedName("biaya_admin")
    var biayaAdmin: String? = null,

    @SerializedName("tenggat_waktu")
    var tenggatWaktu: String? = null,

    @SerializedName("waktu_pembayaran")
    var waktuPembayaran: String? = null
)