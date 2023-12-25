package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.admin.account

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.munmundev.feri_aplikasipembayaraniuranair.R
import com.munmundev.feri_aplikasipembayaraniuranair.adapter.admin.AdminUsersAdapter
import com.munmundev.feri_aplikasipembayaraniuranair.data.database.api.ApiConfig2
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.ResponseModel
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.UsersModel
import com.munmundev.feri_aplikasipembayaraniuranair.databinding.ActivityAdminUsersBinding
import com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.admin.main.AdminMainActivity
import com.munmundev.feri_aplikasipembayaraniuranair.utils.KontrolNavigationDrawer
import com.munmundev.feri_aplikasipembayaraniuranair.utils.LoadingAlertDialog
import com.munmundev.feri_aplikasipembayaraniuranair.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class AdminUsersActivity : AppCompatActivity() {
    private val TAG = "AdminUsersActivityTAG"
    private lateinit var binding: ActivityAdminUsersBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private lateinit var loading: LoadingAlertDialog
    private lateinit var adapter: AdminUsersAdapter
    private val viewModel: AccountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawerSetting()
        setLoadingSetting()
        button()
        fetchDataUsers()
        getDataUsers()
        getTambahData()
    }

    private fun button() {
        binding.btnTambah.setOnClickListener{
            dialogTambahData()
        }
    }

    private fun setKontrolNavigationDrawerSetting() {
        kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminUsersActivity)
        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminUsersActivity)
        }
    }

    private fun setLoadingSetting() {
        loading = LoadingAlertDialog(this@AdminUsersActivity)
        loading.alertDialogLoading()
    }

    private fun getDataUsers() {
        viewModel.getDataUsers().observe(this@AdminUsersActivity){result->
            when(result){
                is UIState.Success->{
                    setDataSuccess(result.data)
                }
                is UIState.Failure->{
                    setDataFailure(result.message)
                }
            }
            loading.alertDialogCancel()
        }
    }

    private fun setDataSuccess(data: ArrayList<UsersModel>) {
        adapter = AdminUsersAdapter(data, object: AdminUsersAdapter.ClickItemListener{
            override fun onClickItem(user: UsersModel, it: View) {
                Toast.makeText(this@AdminUsersActivity, "${user.idBlok}", Toast.LENGTH_SHORT).show()
                val i = Intent(this@AdminUsersActivity, AdminDetailUserAccountActivity::class.java)
                i.putExtra("data", user)
                startActivity(i)
            }
        })

        binding.rvAkun.layoutManager = LinearLayoutManager(this@AdminUsersActivity, LinearLayoutManager.VERTICAL, false)
        binding.rvAkun.adapter = adapter
    }

    private fun setDataFailure(message: String) {
        Toast.makeText(this@AdminUsersActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun fetchDataUsers() {
        viewModel.fetchDataUsers()
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
        viewModel.postTambahUser(nama, alamat, nomorHp, username, password, "user")
    }

    private fun getTambahData(){
        viewModel.getTambahData().observe(this@AdminUsersActivity){result->
            when(result){
                is UIState.Success-> setTambahDataSuccess(result.data[0])
                is UIState.Failure-> setTambahDataFailure(result.message)
            }
        }
    }

    private fun setTambahDataSuccess(responseModel: ResponseModel) {
        if(responseModel.response=="0"){
            Toast.makeText(this@AdminUsersActivity, "Berhasil Tambah Data", Toast.LENGTH_SHORT).show()
        } else{
            Toast.makeText(this@AdminUsersActivity, "Gagal Tambah Data", Toast.LENGTH_SHORT).show()
        }
        fetchDataUsers()
    }

    private fun setTambahDataFailure(message: String) {
        Toast.makeText(this@AdminUsersActivity, message, Toast.LENGTH_SHORT).show()
        fetchDataUsers()
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

    }


    override fun onBackPressed() {
        startActivity(Intent(this@AdminUsersActivity, AdminMainActivity::class.java))
        finish()
        super.onBackPressed()
    }
}