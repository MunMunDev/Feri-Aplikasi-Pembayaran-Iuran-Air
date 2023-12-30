package com.example.muharya_pengingatjadwalkeretaapi.utils

import android.content.Context
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.UsersModel

class SharedPreferencesLogin(val context: Context) {
    val keyIdUser = "keyIdUser"
    val keyNama = "keyNama"
    val keyIdBlok = "keyIdBlok"
    val keyAlamat = "keyAlamat"
    val keyNomorHp = "keyNomorHp"
    val keyUsername = "keyUsername"
    val keyPassword = "keyPassword"
    val keySebagai = "keySebagai"

    var sharedPref = context.getSharedPreferences("sharedpreference_login", Context.MODE_PRIVATE)
    var editPref = sharedPref.edit()

    fun setLogin(id_user:Int, idBlok:String,  nama:String, alamat:String, nomorHp:String, username:String, password:String, sebagai:String){
        editPref.apply{
            putInt(keyIdUser, id_user)
            putString(keyNama, nama)
            putString(keyIdBlok, idBlok)
            putString(keyAlamat, alamat)
            putString(keyNomorHp, nomorHp)
            putString(keyUsername, username)
            putString(keyPassword, password)
            putString(keySebagai, sebagai)
            apply()
        }
    }

//    fun setLogin(id_user:Int, nama:String, idBlok:String, alamat:String, nomorHp:String, username:String, password:String, sebagai:String){
//        editPref.apply{
//            putInt(keyIdUser, id_user)
//            putString(keyNama, nama)
//            putString(keyIdBlok, idBlok)
//            putString(keyAlamat, alamat)
//            putString(keyNomorHp, nomorHp)
//            putString(keyUsername, username)
//            putString(keyPassword, password)
//            putString(keySebagai, sebagai)
//            apply()
//        }
//    }

    fun setLoginUsersModel(user: UsersModel){
        editPref.apply{
            putInt(keyIdUser, user.idUser!!.toInt())
            putString(keyNama, user.nama)
//            putString(keyIdBlok, user.idBlok)
            putString(keyAlamat, user.alamat)
            putString(keyNomorHp, user.nomorHp)
            putString(keyUsername, user.username)
            putString(keyPassword, user.password)
            putString(keySebagai, user.sebagai)
            apply()
        }
    }

    fun getIdUser(): Int{
        return sharedPref.getInt(keyIdUser, 0)
    }
    fun getNama():String{
        return sharedPref.getString(keyNama, "").toString()
    }
    fun getIdBlok(): String{
        return sharedPref.getString(keyIdBlok, "").toString()
    }
    fun getAlamat():String{
        return sharedPref.getString(keyAlamat, "").toString()
    }
    fun getNomorHp():String{
        return sharedPref.getString(keyNomorHp, "").toString()
    }
    fun getUsername():String{
        return sharedPref.getString(keyUsername, "").toString()
    }
    fun getPassword(): String{
        return sharedPref.getString(keyPassword, "").toString()
    }
    fun getSebagai(): String{
        return sharedPref.getString(keySebagai, "").toString()
    }
}