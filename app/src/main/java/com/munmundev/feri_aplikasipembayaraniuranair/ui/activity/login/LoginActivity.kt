package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.muharya_pengingatjadwalkeretaapi.utils.SharedPreferencesLogin
import com.munmundev.feri_aplikasipembayaraniuranair.data.database.api.ApiConfig
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.UsersModel
import com.munmundev.feri_aplikasipembayaraniuranair.databinding.ActivityLoginBinding
import com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.main.MainActivity
import com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.register.RegisterActivity
import com.munmundev.feri_aplikasipembayaraniuranair.utils.LoadingAlertDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginActivityTAG"
    private lateinit var loginBinding: ActivityLoginBinding
    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin
    private lateinit var loading : LoadingAlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        sharedPreferencesLogin = SharedPreferencesLogin(this@LoginActivity)
        loading = LoadingAlertDialog(this@LoginActivity)

        loginBinding.apply {
            tvDaftar.setOnClickListener{
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }
            btnLogin.setOnClickListener{
                if(etUsername.text.isNotEmpty() && etPassword.text.isNotEmpty()){
                    loading.alertDialogLoading()
                    cekUsers(etUsername.text.toString(), etPassword.text.toString())
                }
                else{
                    if(etUsername.text.isEmpty()){
                        etUsername.error = "Masukkan Username"
                    }
                    if(etPassword.text.isEmpty()){
                        etPassword.error = "Masukkan Password"
                    }
                }
            }

        }
    }

    private fun cekUsers(username:String, password:String){
        ApiConfig.getRetrofit().cekUser("", username, password)
            .enqueue(object: Callback<ArrayList<UsersModel>> {
                override fun onResponse(call: Call<ArrayList<UsersModel>>, response: Response<ArrayList<UsersModel>>) {
                    if(!response.body().isNullOrEmpty()){
                        response.body()?.let {userModel ->
                            var valueIdUser = ""
                            var valueUsername = ""
                            var valuePassword = ""
                            var valueSebagai= ""
                            userModel[0].idUser?.let {
                                valueIdUser = it
                            }
                            userModel[0].username?.let {
                                valueUsername = it
                            }
                            userModel[0].password?.let {
                                valuePassword = it
                            }
                            userModel[0].sebagai?.let {
                                valueSebagai = it
                            }

                            try{
                                Toast.makeText(this@LoginActivity, "Login Berhasil", Toast.LENGTH_SHORT).show()
//                                sharedPreferencesLogin.setLogin(valueIdUser.toInt(), valueUsername, valuePassword, valueSebagai)
                                if(valueSebagai=="user"){
                                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                } else{
                                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                }
                            } catch (ex: Exception){
                                Toast.makeText(this@LoginActivity, "gagal: $ex", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    else{
                        Toast.makeText(this@LoginActivity, "Username dan Password tidak ditemukan", Toast.LENGTH_LONG).show()
                    }
                    loading.alertDialogCancel()
                }

                override fun onFailure(call: Call<ArrayList<UsersModel>>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Login Gagal", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "login gagal : ${t.message} ")

                    loading.alertDialogCancel()
                }

            })
    }
}