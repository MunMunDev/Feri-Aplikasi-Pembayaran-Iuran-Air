package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.munmundev.feri_aplikasipembayaraniuranair.databinding.ActivityRegisterBinding
import com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.login.LoginActivity
import com.munmundev.feri_aplikasipembayaraniuranair.utils.LoadingAlertDialog
import com.munmundev.feri_aplikasipembayaraniuranair.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    lateinit var registerBinding: ActivityRegisterBinding
    lateinit var loading: LoadingAlertDialog

    private val registerViewModel : RegisterViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)
        loading = LoadingAlertDialog(this@RegisterActivity)

        button()
        getCekUser()
        getResponseRegisterUser()

    }

    private fun button() {
        buttonRegistrasi()
        buttonMoveLogin()
    }

    private fun buttonMoveLogin() {
        registerBinding.tvLogin.setOnClickListener{
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun buttonRegistrasi() {
        registerBinding.apply {
            btnRegistrasi.setOnClickListener {
                if (etNama.text.isNotEmpty() && etAlamat.text.isNotEmpty()
                    && etNomorHp.text.isNotEmpty() && etUsername.text.isNotEmpty()
                    && etPassword.text.isNotEmpty()
                ) {
                    loading.alertDialogLoading()

                    Toast.makeText(this@RegisterActivity, "${etUsername.text}", Toast.LENGTH_SHORT).show()
                    fetchCekUsers(etUsername.text.toString())
                } else {
                    if (etNama.text.isEmpty()) {
                        etNama.error = "Nama Tidak Boleh Kosong"
                    }
                    if (etAlamat.text.isEmpty()) {
                        etAlamat.error = "Alamat Tidak Boleh Kosong"
                    }
                    if (etNomorHp.text.isEmpty()) {
                        etNomorHp.error = "Nomor HP Tidak Boleh Kosong"
                    }
                    if (etUsername.text.isEmpty()) {
                        etUsername.error = "Username Tidak Boleh Kosong"
                    }
                    if (etPassword.text.isEmpty()) {
                        etPassword.error = "Password Tidak Boleh Kosong"
                    }
                }
            }
        }
    }

    private fun fetchCekUsers(username: String){
        registerViewModel.cekUser(username)
    }

    private fun getCekUser(){
        registerViewModel.getCekUser().observe(this@RegisterActivity){cekUser ->
            when(cekUser){
                is UIState.Success ->{
                    if(cekUser.data.isNotEmpty()){
                        Toast.makeText(this@RegisterActivity, "Maaf, Username Telah Ada", Toast.LENGTH_SHORT).show()
                        loading.alertDialogCancel()
                    }
                    else{
                        registerUser()
                    }
                }
                is UIState.Failure ->{
                    Toast.makeText(this@RegisterActivity, "Gagal", Toast.LENGTH_SHORT).show()
                    loading.alertDialogCancel()
                }
            }
        }
    }

    private fun registerUser() {
        val nama = registerBinding.etNama.text.toString()
        val alamat = registerBinding.etAlamat.text.toString()
        val nomorHp = registerBinding.etNomorHp.text.toString()
        val username = registerBinding.etUsername.text.toString()
        val password = registerBinding.etPassword.text.toString()
        val sebagai = "user"

        registerViewModel.registerUser(nama, alamat, nomorHp, username, password, sebagai)
    }

    private fun getResponseRegisterUser(){
        registerViewModel.getRegisterUser().observe(this@RegisterActivity){response ->
            when(response){
                is UIState.Success ->{
                    if (!response.data.isNullOrEmpty()){
                        val r = response.data[0]
                        if (response.data[0].status == "0"){
                            Toast.makeText(this@RegisterActivity, "Berhasil melakukan registrasi", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(this@RegisterActivity, "Gagal registerasi", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(this@RegisterActivity, "Maaf gagal", Toast.LENGTH_SHORT).show()
                    }
                    loading.alertDialogCancel()
                }
                is UIState.Failure ->{
                    Toast.makeText(this@RegisterActivity, response.message, Toast.LENGTH_SHORT).show()
                    loading.alertDialogCancel()
                }
            }
        }
    }

//    private fun cekUsers(usersModel: UsersModel){
//        var username = ""
//        usersModel.username?.let {
//            username = it
//        }
//
//        ApiConfig2.getRetrofit().cekUserRegistrasi("", username)
//            .enqueue(object: Callback<ArrayList<UsersModel>> {
//                override fun onResponse(call: Call<ArrayList<UsersModel>>, response: Response<ArrayList<UsersModel>>) {
//                    if(response.body().isNullOrEmpty()){
//                        registerUser(usersModel)
//                    }
//                    else{
//                        Toast.makeText(this@RegisterActivity, "Username Sudah Ada", Toast.LENGTH_SHORT).show()
//                        registerBinding.etUsername.error = "Username Sudah Ada"
//                    }
//                }
//
//                override fun onFailure(call: Call<ArrayList<UsersModel>>, t: Throwable) {
//                    Toast.makeText(this@RegisterActivity, "Error Pada: ${t.message}", Toast.LENGTH_SHORT).show()
//                }
//
//            })
//    }

//    private fun registerUser(um: UsersModel){
//        ApiConfig2.getRetrofit().addUser("", um.nama!!, um.alamat!!, um.nomorHp!!, um.username!!, um.password!!, um.sebagai!!)
//            .enqueue(object: Callback<ResponseModel> {
//                override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
//                    Toast.makeText(this@RegisterActivity, "Berhasil Membuat Akun", Toast.LENGTH_SHORT).show()
//
//                    Handler(Looper.getMainLooper()).postDelayed({
//                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
//                    }, 1000)
//                }
//
//                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
//                    Toast.makeText(this@RegisterActivity, "Gagal Membuat Akun", Toast.LENGTH_SHORT).show()
//                }
//            })
//    }
}