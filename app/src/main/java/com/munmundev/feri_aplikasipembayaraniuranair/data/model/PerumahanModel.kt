package com.munmundev.feri_aplikasipembayaraniuranair.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class PerumahanModel(
    @SerializedName("id_perumahan")
    var id_perumahan: String?,

    @SerializedName("id_blok")
    var id_blok: String?,

    @SerializedName("nama_perumahan")
    var nama_perumahan: String?,

    @SerializedName("blok_perumahan")
    var blok_perumahan: String?,

    @SerializedName("jumlah_blok")
    var jumlah_blok: String?,

    @SerializedName("alamat")
    var alamat: String?,

    @SerializedName("id_user")
    var id_user: String?,

    @SerializedName("nama")
    var nama: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
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
        parcel.writeString(id_blok)
        parcel.writeString(id_perumahan)
        parcel.writeString(nama_perumahan)
        parcel.writeString(blok_perumahan)
        parcel.writeString(jumlah_blok)
        parcel.writeString(alamat)
        parcel.writeString(id_user)
        parcel.writeString(nama)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PerumahanModel> {
        override fun createFromParcel(parcel: Parcel): PerumahanModel {
            return PerumahanModel(parcel)
        }

        override fun newArray(size: Int): Array<PerumahanModel?> {
            return arrayOfNulls(size)
        }
    }
}

data class Perumahan(
    @SerializedName("id_perumahan")
    var id_perumahan: String,

    @SerializedName("nama_perumahan")
    var nama_perumahan: String
)

data class BlokPerumahan(
    @SerializedName("id_blok")
    var id_blok: String,

    @SerializedName("id_perumahan")
    var id_perumahan: String,

    @SerializedName("blok_perumahan")
    var blok_perumahan: String
)