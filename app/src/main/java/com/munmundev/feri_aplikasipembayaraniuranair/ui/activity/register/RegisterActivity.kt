package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.munmundev.feri_aplikasipembayaraniuranair.data.database.api.ApiConfig
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.ResponseModel
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.UsersModel
import com.munmundev.feri_aplikasipembayaraniuranair.databinding.ActivityRegisterBinding
import com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.login.LoginActivity
import com.munmundev.feri_aplikasipembayaraniuranair.utils.LoadingAlertDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    lateinit var registerBinding: ActivityRegisterBinding
    lateinit var loading: LoadingAlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)

        loading = LoadingAlertDialog(this@RegisterActivity)
        registerBinding.apply {
            btnRegistrasi.setOnClickListener{
                if(etNama.text.isNotEmpty() && etAlamat.text.isNotEmpty()
                    && etNomorHp.text.isNotEmpty() && etUsername.text.isNotEmpty()
                    && etPassword.text.isNotEmpty())
                {
                    val usersModel = UsersModel(
                        "", etNama.text.toString(), etAlamat.text.toString(),
                        etNomorHp.text.toString(), etUsername.text.toString(), etPassword.text.toString(), "user"
                    )
                    cekUsers(usersModel)
                }
                else{
                    if(etNama.text.isEmpty()){
                        etNama.error = "Nama Tidak Boleh Kosong"
                    }
                    if(etAlamat.text.isEmpty()){
                        etAlamat.error = "Alamat Tidak Boleh Kosong"
                    }
                    if(etNomorHp.text.isEmpty()){
                        etNomorHp.error = "Nomor HP Tidak Boleh Kosong"
                    }
                    if(etUsername.text.isEmpty()){
                        etUsername.error = "Username Tidak Boleh Kosong"
                    }
                    if(etPassword.text.isEmpty()){
                        etPassword.error = "Password Tidak Boleh Kosong"
                    }
                }
            }
        }
    }

    private fun cekUsers(usersModel: UsersModel){
        var username = ""
        usersModel.username?.let {
            username = it
        }

        ApiConfig.getRetrofit().cekUserRegistrasi("", username)
            .enqueue(object: Callback<ArrayList<UsersModel>> {
                override fun onResponse(call: Call<ArrayList<UsersModel>>, response: Response<ArrayList<UsersModel>>) {
                    if(response.body().isNullOrEmpty()){
                        registerUser(usersModel)
                    }
                    else{
                        Toast.makeText(this@RegisterActivity, "Username Sudah Ada", Toast.LENGTH_SHORT).show()
                        registerBinding.etUsername.error = "Username Sudah Ada"
                    }
                }

                override fun onFailure(call: Call<ArrayList<UsersModel>>, t: Throwable) {
                    Toast.makeText(this@RegisterActivity, "Error Pada: ${t.message}", Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun registerUser(um: UsersModel){
        ApiConfig.getRetrofit().registerUser("", um.nama, um.alamat, um.nomorHp, um.username, um.password, um.sebagai)
            .enqueue(object: Callback<ResponseModel> {
                override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                    Toast.makeText(this@RegisterActivity, "Berhasil Membuat Akun", Toast.LENGTH_SHORT).show()

                    Handler(Looper.getMainLooper()).postDelayed({
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    }, 1000)
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    Toast.makeText(this@RegisterActivity, "Gagal Membuat Akun", Toast.LENGTH_SHORT).show()
                }
            })
    }
}