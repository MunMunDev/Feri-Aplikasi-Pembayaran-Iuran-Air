package com.munmundev.feri_aplikasipembayaraniuranair.data.model

import android.os.Parcel
import android.os.Parcelable
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
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(idPembayaran)
        parcel.writeString(idUser)
        parcel.writeString(nama)
        parcel.writeString(alamat)
        parcel.writeString(harga)
        parcel.writeString(denda)
        parcel.writeString(biayaAdmin)
        parcel.writeString(tenggatWaktu)
        parcel.writeString(waktuPembayaran)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PembayaranModel> {
        override fun createFromParcel(parcel: Parcel): PembayaranModel {
            return PembayaranModel(parcel)
        }

        override fun newArray(size: Int): Array<PembayaranModel?> {
            return arrayOfNulls(size)
        }
    }
}