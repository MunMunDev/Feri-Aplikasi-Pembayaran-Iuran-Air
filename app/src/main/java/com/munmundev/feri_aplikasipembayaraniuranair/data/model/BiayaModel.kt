package com.munmundev.feri_aplikasipembayaraniuranair.data.model

import com.google.gson.annotations.SerializedName

class BiayaModel(
    @SerializedName("id_biaya")
    var id_biaya: String? = null,

    @SerializedName("biaya")
    var biaya: String? = null,

    @SerializedName("denda")
    var denda: String? = null,

    @SerializedName("biaya_admin")
    var biaya_admin: String? = null
)