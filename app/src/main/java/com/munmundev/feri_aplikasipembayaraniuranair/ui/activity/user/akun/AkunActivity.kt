package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.user.akun

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.muharya_pengingatjadwalkeretaapi.utils.SharedPreferencesLogin
import com.munmundev.feri_aplikasipembayaraniuranair.R
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.UsersModel
import com.munmundev.feri_aplikasipembayaraniuranair.databinding.ActivityAkunBinding
import com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.user.main.MainActivity
import com.munmundev.feri_aplikasipembayaraniuranair.utils.KontrolNavigationDrawer
import com.munmundev.feri_aplikasipembayaraniuranair.utils.LoadingAlertDialog
import com.munmundev.feri_aplikasipembayaraniuranair.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AkunActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAkunBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private lateinit var sharedPreferences: SharedPreferencesLogin
    private lateinit var loading: LoadingAlertDialog
    private val viewModel: AkunViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAkunBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSharedPreferences()
        setNavigationDrawer()
        setLoading()
        setData()
        button()
        getData()
    }

    private fun setLoading() {
        loading = LoadingAlertDialog(this@AkunActivity)
    }

    private fun setSharedPreferences() {
        sharedPreferences = SharedPreferencesLogin(this@AkunActivity)
    }

    private fun setNavigationDrawer() {
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@AkunActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AkunActivity)
        }
    }

    private fun setData(){
        binding.apply {
            etNama.setText(sharedPreferences.getNama())
            etAlamat.setText(sharedPreferences.getAlamat())
            etNomorHp.setText(sharedPreferences.getNomorHp())
            etUsername.setText(sharedPreferences.getUsername())
            etPassword.setText(sharedPreferences.getPassword())
        }
    }

    private fun button() {
        binding.btnUbahData.setOnClickListener {
            val viewAlertDialog = View.inflate(this@AkunActivity, R.layout.alert_dialog_update_akun, null)

            val etEditNama = viewAlertDialog.findViewById<TextView>(R.id.etEditNama)
//            val etEditAlamat = viewAlertDialog.findViewById<TextView>(R.id.etEditAlamat)
            val etEditNomorHp = viewAlertDialog.findViewById<TextView>(R.id.etEditNomorHp)
            val etEditUsername = viewAlertDialog.findViewById<TextView>(R.id.etEditUsername)
            val etEditPassword = viewAlertDialog.findViewById<TextView>(R.id.etEditPassword)

            val btnSimpan = viewAlertDialog.findViewById<Button>(R.id.btnSimpan)
            val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

            etEditNama.text = sharedPreferences.getNama()
//            etEditAlamat.text = sharedPreferences.getAlamat()
            etEditNomorHp.text = sharedPreferences.getNomorHp()
            etEditUsername.text = sharedPreferences.getUsername()
            etEditPassword.text = sharedPreferences.getPassword()

            val alertDialog = AlertDialog.Builder(this@AkunActivity)
            alertDialog.setView(viewAlertDialog)
            val dialogInputan = alertDialog.create()
            dialogInputan.show()

            btnSimpan.setOnClickListener {
                loading.alertDialogLoading()
//                val userModel = UsersModel(sharedPreferences.getIdUser().toString(), etEditNama.text.toString(), etEditAlamat.text.toString(), etEditNomorHp.text.toString(), etEditUsername.text.toString(), etEditPassword.text.toString(), "user")
//                postUpdateData(dialogInputan, userModel)
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postUpdateData(dialog: AlertDialog, usersModel: UsersModel){
        viewModel.postData(usersModel)
        dialog.dismiss()
    }
    private fun getData(){
        viewModel.getData().observe(this@AkunActivity){result->
            when(result){
                is UIState.Success->{
                    if(result.data[0].idUser!!.isNotEmpty()){
                        Toast.makeText(this@AkunActivity, "Berhasil Update Akun", Toast.LENGTH_SHORT).show()
                        sharedPreferences.setLoginUsersModel(result.data[0])
                        setData()
                    } else{
                        Toast.makeText(this@AkunActivity, "Gagal dapat data", Toast.LENGTH_SHORT).show()
                    }
                    loading.alertDialogCancel()
                }
                is UIState.Failure-> {
                    Toast.makeText(this@AkunActivity, result.message, Toast.LENGTH_SHORT).show()
                    loading.alertDialogCancel()
                }
            }
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@AkunActivity, MainActivity::class.java))
        finish()
    }
}