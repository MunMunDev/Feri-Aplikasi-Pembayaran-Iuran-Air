package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.example.muharya_pengingatjadwalkeretaapi.utils.SharedPreferencesLogin
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.UsersModel
import com.munmundev.feri_aplikasipembayaraniuranair.databinding.ActivityLoginBinding
import com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.register.RegisterActivity
import com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.admin.main.AdminMainActivity
import com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.user.main.MainActivity
import com.munmundev.feri_aplikasipembayaraniuranair.utils.LoadingAlertDialog
import com.munmundev.feri_aplikasipembayaraniuranair.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginActivityTAG"
    private lateinit var loginBinding: ActivityLoginBinding
    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin
    private lateinit var loading : LoadingAlertDialog

    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        konfigurationUtils()
        button()
        getData()
    }

    private fun konfigurationUtils() {
        sharedPreferencesLogin = SharedPreferencesLogin(this@LoginActivity)
        loading = LoadingAlertDialog(this@LoginActivity)
    }

    private fun button(){
        btnDaftar()
        btnLogin()
    }

    private fun btnLogin() {
        loginBinding.apply {
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

    private fun btnDaftar() {
        loginBinding.tvDaftar.setOnClickListener{
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }

    private fun cekUsers(username: String, password: String) {
        loginViewModel.fetchDataUser(username, password)
    }

    private fun getData(){
        loginViewModel.getDataUser().observe(this@LoginActivity){user ->
            when(user){
                is UIState.Success ->{
                    if(!user.data.isNullOrEmpty()){
                        successFetchLogin(user.data)
                    } else{
                        failureFetchLogin()
                    }
                }
                is UIState.Failure ->{
                    Toast.makeText(this@LoginActivity, "Gagal bang ${user.message}", Toast.LENGTH_SHORT).show()
                    loading.alertDialogCancel()
                }
            }
        }
    }

    private fun failureFetchLogin() {
        Toast.makeText(this@LoginActivity, "Data tidak ditemukan \nPastikan Username dan Password ada", Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun successFetchLogin(userModel: ArrayList<UsersModel>){
        var valueIdUser = 0
        userModel[0].idUser?.let {
            valueIdUser = it.toInt()
        }
        val valueNama = userModel[0].nama.toString()
//        val valueIdBlok = userModel[0].idBlok.toString()
        val valueAlamat = userModel[0].alamat.toString()
        val valueNomorHp = userModel[0].nomorHp.toString()
        val valueUsername = userModel[0].username.toString()
        val valuePassword = userModel[0].password.toString()
        val valueSebagai= userModel[0].sebagai.toString()

        Log.d(TAG, "successFetchLogin: $valueNama, $valueAlamat")

        try{
            Toast.makeText(this@LoginActivity, "Login Berhasil", Toast.LENGTH_SHORT).show()
//            sharedPreferencesLogin.setLogin(valueIdUser, valueNama, valueIdBlok, valueAlamat, valueNomorHp, valueUsername, valuePassword, valueSebagai, )
            sharedPreferencesLogin.setLogin(valueIdUser, valueNama, valueAlamat, valueNomorHp, valueUsername, valuePassword, valueSebagai)
            if(valueSebagai=="user"){
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            } else{
                startActivity(Intent(this@LoginActivity, AdminMainActivity::class.java))
            }
        } catch (ex: Exception){
            Toast.makeText(this@LoginActivity, "gagal: $ex", Toast.LENGTH_SHORT).show()
        }
        Log.d(TAG, "respon data: $valueIdUser, $valueNama, $valueAlamat, $valueUsername, $valueNomorHp $valuePassword, $valueSebagai")
        loading.alertDialogCancel()
    }



//    private fun cekUsers2(username:String, password:String){
//        ApiConfig2.getRetrofit().cekUser("", username, password)
//            .enqueue(object: Callback<ArrayList<UsersModel>> {
//                override fun onResponse(call: Call<ArrayList<UsersModel>>, response: Response<ArrayList<UsersModel>>) {
//                    if(!response.body().isNullOrEmpty()){
//                        response.body()?.let {userModel ->
//                            var valueIdUser = ""
//                            var valueUsername = ""
//                            var valuePassword = ""
//                            var valueSebagai= ""
//                            userModel[0].idUser?.let {
//                                valueIdUser = it
//                            }
//                            userModel[0].username?.let {
//                                valueUsername = it
//                            }
//                            userModel[0].password?.let {
//                                valuePassword = it
//                            }
//                            userModel[0].sebagai?.let {
//                                valueSebagai = it
//                            }
//
//                            try{
//                                Toast.makeText(this@LoginActivity, "Login Berhasil", Toast.LENGTH_SHORT).show()
//                                sharedPreferencesLogin.setLogin(valueIdUser.trim().toInt(), valueUsername, valuePassword, valueSebagai)
//                                if(valueSebagai=="user"){
//                                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
//                                } else{
//                                    startActivity(Intent(this@LoginActivity, AdminMainActivity::class.java))
//                                }
//                            } catch (ex: Exception){
//                                Toast.makeText(this@LoginActivity, "gagal: $ex", Toast.LENGTH_SHORT).show()
//                            }
//                            Log.d(TAG, "respon data: ${valueIdUser.trim()}, $valueUsername, $valuePassword, $valueSebagai")
//
//                        }
//                    }
//                    else{
//                        Toast.makeText(this@LoginActivity, "Username dan Password tidak ditemukan", Toast.LENGTH_LONG).show()
//                    }
//                    loading.alertDialogCancel()
//                }
//
//                override fun onFailure(call: Call<ArrayList<UsersModel>>, t: Throwable) {
//                    Toast.makeText(this@LoginActivity, "Login Gagal", Toast.LENGTH_SHORT).show()
//                    Log.d(TAG, "login gagal : ${t.message} ")
//
//                    loading.alertDialogCancel()
//                }
//
//            })
//    }

    var tapDuaKali = false
    override fun onBackPressed() {
        if (tapDuaKali){
            super.onBackPressed()
        }
        tapDuaKali = true
        Toast.makeText(this@LoginActivity, "Tekan Sekali Lagi untuk keluar", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({
            tapDuaKali = false
        }, 2000)

    }
}