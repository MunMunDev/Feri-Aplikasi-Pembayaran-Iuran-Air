package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.admin

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.munmundev.feri_aplikasipembayaraniuranair.R
import com.munmundev.feri_aplikasipembayaraniuranair.adapter.admin.AdminUsersAdapter
import com.munmundev.feri_aplikasipembayaraniuranair.data.database.api.ApiConfig
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.ResponseModel
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.UsersModel
import com.munmundev.feri_aplikasipembayaraniuranair.databinding.ActivityAdminUsersBinding
import com.munmundev.feri_aplikasipembayaraniuranair.utils.KontrolNavigationDrawer
import com.munmundev.feri_aplikasipembayaraniuranair.utils.LoadingAlertDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminUsersActivity : Activity() {
    private val TAG = "AdminUsersActivityTAG"
    private lateinit var binding: ActivityAdminUsersBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private lateinit var loading: LoadingAlertDialog
    private lateinit var listUser: ArrayList<UsersModel>
    private lateinit var adapter: AdminUsersAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminUsersActivity)
        loading = LoadingAlertDialog(this@AdminUsersActivity)
        loading.alertDialogLoading()


        getData()

        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminUsersActivity)

            btnTambah.setOnClickListener{
                dialogTambahData()
            }
        }

        binding.apply {
            this.btnTambah.setOnClickListener {
                dialogTambahData()
            }
        }
    }

    private fun getData(){
        ApiConfig.getRetrofit().getAllUser("")
            .enqueue(object : Callback<ArrayList<UsersModel>>{
                override fun onResponse(
                    call: Call<ArrayList<UsersModel>>,
                    response: Response<ArrayList<UsersModel>>
                ) {
                    listUser = arrayListOf()
                    Log.d(TAG, "onResponse: ${response.body()}")
                    if(!response.body()!!.isNullOrEmpty()){
                        listUser = response.body()!!
                        
                        adapter = AdminUsersAdapter(listUser, object: AdminUsersAdapter.ClickItemListener{
                            override fun onClickSetting(user: UsersModel, position: Int, it: View) {
                                val popupMenu = PopupMenu(this@AdminUsersActivity, it)
                                popupMenu.inflate(R.menu.popup_menu)
                                popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
                                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                                        when (menuItem!!.itemId) {
                                            R.id.edit -> {
                                                Toast.makeText(this@AdminUsersActivity, "Edit", Toast.LENGTH_SHORT).show()
                                                dialogUpdateData(user)
                                                return true
                                            }

                                            R.id.hapus -> {
                                                Toast.makeText(this@AdminUsersActivity, "Hapus", Toast.LENGTH_SHORT).show()
                                                dialogHapusData(user)
                                                return true
                                            }
                                        }
                                        return true
                                    }

                                })
                                popupMenu.show()
                            }

                        })

                        binding.rvAkun.layoutManager = LinearLayoutManager(this@AdminUsersActivity)
                        binding.rvAkun.adapter = adapter
                    }
                    else{
                        Toast.makeText(this@AdminUsersActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
                    }
                    loading.alertDialogCancel()
                }

                override fun onFailure(call: Call<ArrayList<UsersModel>>, t: Throwable) {
                    Toast.makeText(this@AdminUsersActivity, "", Toast.LENGTH_SHORT).show()
                    loading.alertDialogCancel()
                }

            })
    }

    private fun dialogTambahData(){
        val viewAlertDialog = View.inflate(this@AdminUsersActivity, R.layout.alert_dialog_admin_akun, null)

        val etNama = viewAlertDialog.findViewById<EditText>(R.id.etNama)
        val etAlamat = viewAlertDialog.findViewById<EditText>(R.id.etAlamat)
        val etNomorHp = viewAlertDialog.findViewById<EditText>(R.id.etNomorHp)
        val etUsername = viewAlertDialog.findViewById<EditText>(R.id.etUsername)
        val etPassword = viewAlertDialog.findViewById<EditText>(R.id.etPassword)

        val btnSimpan = viewAlertDialog.findViewById<Button>(R.id.btnSimpan)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

        val alertDialog = AlertDialog.Builder(this@AdminUsersActivity)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        btnSimpan.setOnClickListener {
            dialogInputan.dismiss()
            val nama = etNama.text.toString()
            val alamat = etAlamat.text.toString()
            val nomorHp = etNomorHp.text.toString()
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            postTambahData(nama, alamat, nomorHp, username, password)
        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }

    private fun postTambahData(nama: String, alamat: String, nomorHp: String, username: String, password: String){

        ApiConfig.getRetrofit().addUser("", nama, alamat, nomorHp, username, password, "user")
            .enqueue(object: Callback<ResponseModel> {
                override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                    Toast.makeText(this@AdminUsersActivity, "Berhasil Tambah", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "onResponse: $nama, $alamat, $nomorHp, $username, $password")
                    getData()
//                    loading.alertDialogCancel()
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    Toast.makeText(this@AdminUsersActivity, "Gagal Tambah", Toast.LENGTH_SHORT).show()
                    getData()
//                    loading.alertDialogCancel()
                }

            })
    }


    fun dialogUpdateData(user: UsersModel){
        val viewAlertDialog = View.inflate(this@AdminUsersActivity, R.layout.alert_dialog_admin_akun, null)

        val etNama = viewAlertDialog.findViewById<EditText>(R.id.etNama)
        val etAlamat = viewAlertDialog.findViewById<EditText>(R.id.etAlamat)
        val etNomorHp = viewAlertDialog.findViewById<EditText>(R.id.etNomorHp)
        val etUsername = viewAlertDialog.findViewById<EditText>(R.id.etUsername)
        val etPassword = viewAlertDialog.findViewById<EditText>(R.id.etPassword)

        val btnSimpan = viewAlertDialog.findViewById<Button>(R.id.btnSimpan)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

        etNama.setText(user.nama)
        etAlamat.setText(user.alamat)
        etNomorHp.setText(user.nomorHp)
        etUsername.setText(user.username)
        etPassword.setText(user.password)

        val alertDialog = AlertDialog.Builder(this@AdminUsersActivity)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        btnSimpan.setOnClickListener {
            dialogInputan.dismiss()

            val nama = etNama.text.toString()
            val alamat = etAlamat.text.toString()
            val nomorHp = etNomorHp.text.toString()
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            user.idUser?.let {
                postUpdateData(it, nama, alamat, nomorHp, username, password)
            }
        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }

    fun postUpdateData(idUser:String, nama: String, alamat: String, nomorHp: String, username: String, password: String){

        ApiConfig.getRetrofit().postAdminUpdateUser("", idUser, nama, alamat, nomorHp, username, password)
            .enqueue(object: Callback<ResponseModel> {
                override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                    Toast.makeText(this@AdminUsersActivity, "Berhasil Update Data", Toast.LENGTH_SHORT).show()
                    response.body()?.response.let {
                        if(it == "success"){
                            response.body()!!.message_response.let {message->
                                Toast.makeText(this@AdminUsersActivity, "$message update data", Toast.LENGTH_SHORT).show()
                            }
                        }
                        else{
                            Toast.makeText(this@AdminUsersActivity, "$it", Toast.LENGTH_SHORT).show()
                        }
                    }
                    getData()
//                    loading.alertDialogCancel()
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    Toast.makeText(this@AdminUsersActivity, "Gagal Update Data", Toast.LENGTH_SHORT).show()
                    getData()
//                    loading.alertDialogCancel()
                }

            })
    }



    @SuppressLint("SetTextI18n")
    fun dialogHapusData(user: UsersModel){
        val viewAlertDialog = View.inflate(this@AdminUsersActivity, R.layout.alert_dialog_admin_hapus, null)

        val tvHapus = viewAlertDialog.findViewById<TextView>(R.id.tvHapus)

        val btnHapus = viewAlertDialog.findViewById<Button>(R.id.btnHapus)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

        tvHapus.text = "Hapus Akun \n ${user.nama} ?"

        val alertDialog = AlertDialog.Builder(this@AdminUsersActivity)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        btnHapus.setOnClickListener {
            dialogInputan.dismiss()
            user.idUser?.let {
                postHapusData(it)
            }
        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }

    fun postHapusData(idUser:String){
        ApiConfig.getRetrofit().postAdminHapusUser("", idUser)
            .enqueue(object: Callback<ResponseModel> {
                override fun onResponse(call: Call<ResponseModel>, response: Response<ResponseModel>) {
                    Toast.makeText(this@AdminUsersActivity, "Berhasil Hapus Data", Toast.LENGTH_SHORT).show()
                    getData()
//                    loading.alertDialogCancel()
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    Toast.makeText(this@AdminUsersActivity, "Gagal Hapus Data", Toast.LENGTH_SHORT).show()
                    getData()
//                    loading.alertDialogCancel()
                }

            })
    }


    override fun onBackPressed() {
        startActivity(Intent(this@AdminUsersActivity, AdminMainActivity::class.java))
        finish()
        super.onBackPressed()
    }
}