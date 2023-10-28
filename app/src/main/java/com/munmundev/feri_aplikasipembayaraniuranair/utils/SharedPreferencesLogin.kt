package com.example.muharya_pengingatjadwalkeretaapi.utils

import android.content.Context

class SharedPreferencesLogin(val context: Context) {
    val keyIdUser = "keyIdUser"
    val keyUsername = "keyUsername"
    val keyPassword = "keyPassword"
    val keySebagai = "keySebagai"

    var sharedPref = context.getSharedPreferences("sharedpreference_login", Context.MODE_PRIVATE)
    var editPref = sharedPref.edit()

    fun setLogin(id_user:Int, username:String, password:String, sebagai:String){
        editPref.apply{
            putInt(keyIdUser, id_user)
            putString(keyUsername, username)
            putString(keyPassword, password)
            putString(keySebagai, sebagai)
            apply()
        }
    }

    fun getId(): Int{
        val id_user = sharedPref.getInt(keyIdUser, 0)
        return id_user
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