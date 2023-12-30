package com.munmundev.feri_aplikasipembayaraniuranair.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class UsersModel(
    @SerializedName("id_user")
    var idUser: String? = null,

    @SerializedName("nama")
    var nama: String? = null,

    @SerializedName("id_blok")
    var idBlok: String? = null,

    @SerializedName("alamat")
    var alamat: String? = null,

    @SerializedName("nomor_hp")
    var nomorHp: String? = null,

    @SerializedName("username")
    var username: String? = null,

    @SerializedName("password")
    var password: String? = null,

    @SerializedName("sebagai")
    var sebagai: String? = null,

    @SerializedName("perumahan")
    var perumahan: ArrayList<PerumahanModel>? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(PerumahanModel.CREATOR)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(idUser)
        parcel.writeString(nama)
        parcel.writeString(idBlok)
        parcel.writeString(alamat)
        parcel.writeString(nomorHp)
        parcel.writeString(username)
        parcel.writeString(password)
        parcel.writeString(sebagai)
        parcel.writeTypedList(perumahan)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UsersModel> {
        override fun createFromParcel(parcel: Parcel): UsersModel {
            return UsersModel(parcel)
        }

        override fun newArray(size: Int): Array<UsersModel?> {
            return arrayOfNulls(size)
        }
    }
}