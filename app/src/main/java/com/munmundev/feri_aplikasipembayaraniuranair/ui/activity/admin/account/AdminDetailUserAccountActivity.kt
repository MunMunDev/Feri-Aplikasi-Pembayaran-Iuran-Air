package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.admin.account

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.munmundev.feri_aplikasipembayaraniuranair.R
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.PerumahanModel
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.ResponseModel
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.UsersModel
import com.munmundev.feri_aplikasipembayaraniuranair.databinding.ActivityAdminDetailUserAccountBinding
import com.munmundev.feri_aplikasipembayaraniuranair.utils.LoadingAlertDialog
import com.munmundev.feri_aplikasipembayaraniuranair.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import kotlin.collections.ArrayList

@AndroidEntryPoint
class AdminDetailUserAccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminDetailUserAccountBinding
    private lateinit var user: UsersModel
    private lateinit var userTemp: UsersModel
    private lateinit var loading: LoadingAlertDialog
    private val viewModel: AccountViewModel by viewModels()
    private lateinit var listPerumahan: ArrayList<PerumahanModel>
    private lateinit var listBlokPerumahan: ArrayList<PerumahanModel>
    private lateinit var listNamaPerumahan: ArrayList<String>
    private lateinit var listIdPerumahan: ArrayList<String>
    private lateinit var listNamaBlokPerumahan: ArrayList<String>
    private lateinit var listIdBlokPerumahan: ArrayList<String>
    private var idPerumahan = "0"
    private var idBlokPerumahan = "0"
    private var valueIdBlokPerumahan = "0"
    private var valueIdPerumahan = "0"
    private var valueNamaPerumahan = "0"
    private var valueNamaBlokPerumahan = "0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDetailUserAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDataSebelumnya()
        setLoading()
        setData(user)
        setButton()
        fetchDataPerumahan()
        getDataPerumahan()
        fetchDataBlokPerumahan()
        getDataBlokPerumahan()
        getPostUpdateData()
        getPostHapusUser()
    }

    private fun setLoading() {
        loading = LoadingAlertDialog(this@AdminDetailUserAccountActivity)
    }

    private fun setDataSebelumnya() {
        user = UsersModel()
        try{
            val intent = intent.getParcelableExtra<UsersModel>("data")
            user = intent!!
        } catch (ex: Exception){
            Toast.makeText(this@AdminDetailUserAccountActivity, "Gagal", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setData(user: UsersModel) {
        binding.apply {
            etNama.text = user.nama
            etPerumahan.text = user.perumahan!![0].nama_perumahan
            etBlokPerumahan.text = user.perumahan!![0].blok_perumahan
            etNoAlamat.text = user.perumahan!![0].alamat
            etNomorHp.text = user.nomorHp
            etUsername.text = user.username
            etPassword.text = user.password
        }
    }

    private fun setButton() {
        binding.apply {
            btnBack.setOnClickListener {
                finish()
            }
            btnUbahData.setOnClickListener {
                dialogUbahData()
            }
            btnHapusData.setOnClickListener {
                dialogHapusUser(user.idUser!!, user.nama!!)
            }
        }
    }

    private fun fetchDataPerumahan() {
        viewModel.fetchDataPerumahan()
    }

    private fun getDataPerumahan() {
        viewModel.getDataPerumahan().observe(this@AdminDetailUserAccountActivity){result->
            when(result){
                is UIState.Success-> setDataSuccessPerumahan(result.data)
                is UIState.Failure-> setDataFailurePerumahan(result.message)
            }
        }
    }

    private fun setDataSuccessPerumahan(data: ArrayList<PerumahanModel>) {
        listPerumahan = arrayListOf()
        listPerumahan = data

        listNamaPerumahan = arrayListOf()
        listIdPerumahan = arrayListOf()
        for(value in data){
            if(value.jumlah_blok != "0"){
                listNamaPerumahan.add(value.nama_perumahan!!)
                listIdPerumahan.add(value.id_perumahan!!)
            }
        }

    }

    private fun setDataFailurePerumahan(message: String) {
        listNamaPerumahan = arrayListOf()
    }

    private fun fetchDataBlokPerumahan() {
        viewModel.fetchDataBlokPerumahan()
    }

    private fun getDataBlokPerumahan() {
        viewModel.getDataBlokPerumahan().observe(this@AdminDetailUserAccountActivity){result->
            when(result){
                is UIState.Success-> setDataSuccessBlokPerumahan(result.data)
                is UIState.Failure-> setDataFailureBlokPerumahan(result.message)
            }
        }
    }

    private fun setDataSuccessBlokPerumahan(data: ArrayList<PerumahanModel>) {
        listBlokPerumahan = arrayListOf()
        if(data.isNotEmpty()){
            listBlokPerumahan = data
            setBlokPerumahan()
        }
    }

    private fun setBlokPerumahan() {
        listNamaBlokPerumahan = arrayListOf()
        listIdBlokPerumahan = arrayListOf()

        for(value in listBlokPerumahan){
            if(value.id_blok==user.perumahan!![0].id_blok){
                idPerumahan = value.id_perumahan!!
                idBlokPerumahan = value.id_blok!!
                valueIdPerumahan = value.id_perumahan!!
                valueIdBlokPerumahan = value.id_blok!!
                Log.d("DetailTAG", "blok2: ${idPerumahan} dan ${idBlokPerumahan}")
            }
        }
        for(value in listBlokPerumahan){
            if(value.id_perumahan == idPerumahan){
                listNamaBlokPerumahan.add(value.blok_perumahan!!)
                listIdBlokPerumahan.add(value.id_blok!!)
                Log.d("DetailTAG", "blok: ${value.id_blok}")
            }
        }

    }

    private fun setValueIdPerumahan(idPerumahan:String){
        listNamaBlokPerumahan = arrayListOf()
        listIdBlokPerumahan = arrayListOf()

        for(value in listBlokPerumahan){
            if(value.id_perumahan == idPerumahan){
                listNamaBlokPerumahan.add(value.blok_perumahan!!)
                listIdBlokPerumahan.add(value.id_blok!!)
                Log.d("DetailTAG", "setValueIdPerumahan: $idPerumahan dan ${value.id_perumahan}")
            }
        }
    }

    private fun setDataFailureBlokPerumahan(message: String) {
        listBlokPerumahan = arrayListOf()
    }
    private fun dialogUbahData() {
        val viewAlertDialog = View.inflate(this@AdminDetailUserAccountActivity, R.layout.alert_dialog_update_akun, null)

        val etEditNama = viewAlertDialog.findViewById<EditText>(R.id.etEditNama)
        val spPerumahan = viewAlertDialog.findViewById<Spinner>(R.id.spPerumahan)
        val spBlokPerumahan = viewAlertDialog.findViewById<Spinner>(R.id.spBlokPerumahan)
        val etEditNoAlamat = viewAlertDialog.findViewById<EditText>(R.id.etEditNoAlamat)
        val etEditNomorHp = viewAlertDialog.findViewById<EditText>(R.id.etEditNomorHp)
        val etEditUsername = viewAlertDialog.findViewById<EditText>(R.id.etEditUsername)
        val etEditPassword = viewAlertDialog.findViewById<EditText>(R.id.etEditPassword)
        val btnSimpan = viewAlertDialog.findViewById<Button>(R.id.btnSimpan)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

        var valueIdNoPerumahan = "0"

        etEditNama.setText(user.nama)
        etEditNomorHp.setText(user.nomorHp)
        etEditNoAlamat.setText(user.perumahan!![0].alamat)
        etEditUsername.setText(user.username)
        etEditPassword.setText(user.password)

        val arrayAdapter = ArrayAdapter(this@AdminDetailUserAccountActivity, android.R.layout.simple_spinner_item, listNamaPerumahan)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        Log.d("DetailTAG", "v $idPerumahan")
        Log.d("DetailTAG", "v ${user.perumahan!![0].id_perumahan}")
        Log.d("DetailTAG", "v ${user.perumahan!![0].id_blok}")

        var id = 0
        for((no, value) in listIdPerumahan.withIndex()){
            if(value == idPerumahan){
                id = no
                Log.d("DetailTAG", "val $no: $value")
            }
        }
        spPerumahan.adapter = arrayAdapter
        spPerumahan.setSelection(id)

        spPerumahan.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val numberPosition = spPerumahan.selectedItemPosition

                valueIdPerumahan = listIdPerumahan[numberPosition]
                valueNamaPerumahan = listNamaPerumahan[numberPosition]
                Log.d("DetailLagiTAG", "onItemSelected: $valueIdNoPerumahan")

                idPerumahan = valueIdPerumahan
                setValueIdPerumahan(valueIdPerumahan)
                setSpinnerBlokPerumahan(spBlokPerumahan, listNamaBlokPerumahan, listIdBlokPerumahan)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        setSpinnerBlokPerumahan(spBlokPerumahan, listNamaBlokPerumahan, listIdBlokPerumahan)

        val alertDialog = AlertDialog.Builder(this@AdminDetailUserAccountActivity)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        btnSimpan.setOnClickListener {
            var cek = false
            if(etEditNama.toString().isEmpty()){
                etEditNama.error = "Tidak Boleh Kosong"
                cek = true
            }
            if(etEditNoAlamat.toString().isEmpty()){
                etEditNoAlamat.error = "Tidak Boleh Kosong"
                cek = true
            }
            if(etEditNomorHp.toString().isEmpty()){
                etEditNomorHp.error = "Tidak Boleh Kosong"
                cek = true
            }
            if(etEditUsername.toString().isEmpty()){
                etEditUsername.error = "Tidak Boleh Kosong"
                cek = true
            }
            if(etEditPassword.toString().isEmpty()){
                etEditPassword.error = "Tidak Boleh Kosong"
                cek = true
            }

            if(!cek){
                Toast.makeText(this@AdminDetailUserAccountActivity, "Update", Toast.LENGTH_SHORT).show()
                postUpdateData(
                    user.idUser!!,
                    etEditNama.text.toString(),
                    valueIdBlokPerumahan,
                    etEditNoAlamat.text.toString(),
                    etEditNomorHp.text.toString(),
                    etEditUsername.text.toString(),
                    etEditPassword.text.toString(),
                    user.username!!
                )
                loading.alertDialogLoading()
                val arr = arrayListOf<PerumahanModel>()
                arr.add(
                    PerumahanModel(
                        valueIdPerumahan,
                        valueIdBlokPerumahan,
                        valueNamaPerumahan,
                        valueNamaBlokPerumahan,
                        "",
                        etEditNoAlamat.text.toString(),
                        "",
                        ""
                    )
                )
                Log.d("DetailTAG", "dialogUbahData: ${arr.size}")
                userTemp = UsersModel(
                    idUser = user.idUser!!,
                    nama = etEditNama.text.toString(),
                    nomorHp = etEditNomorHp.text.toString(),
                    username = etEditUsername.text.toString(),
                    password = etEditPassword.text.toString(),
                    perumahan = arr
                )
                dialogInputan.dismiss()
            }

        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }

    private fun setSpinnerBlokPerumahan(
        spBlokPerumahan: Spinner,
        listNamaBlokPerumahan: ArrayList<String>,
        listIdBlokPerumahan: ArrayList<String>
    ) {
        val arrayAdapterBlok = ArrayAdapter(
            this@AdminDetailUserAccountActivity,
            android.R.layout.simple_spinner_item,
            listNamaBlokPerumahan
        )
        arrayAdapterBlok.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        var id = 0
        for((no, value) in listIdBlokPerumahan.withIndex()){
            if(value == idBlokPerumahan){
                id = no
            }
        }
        spBlokPerumahan.adapter = arrayAdapterBlok
        spBlokPerumahan.setSelection(id)

        spBlokPerumahan.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val numberPosition = spBlokPerumahan.selectedItemPosition

                valueIdBlokPerumahan = listIdBlokPerumahan[numberPosition]
                valueNamaBlokPerumahan = listNamaBlokPerumahan[numberPosition]
                Log.d("DetailTAG", "id blok: $valueIdBlokPerumahan")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun postUpdateData(idUser:String, nama: String, idBlokPerumahan:String, noAlamat: String, nomorHp: String, username: String, password: String, usernameLama:String){
        viewModel.postUpdateUser(idUser, nama, idBlokPerumahan, noAlamat, nomorHp, username, password, usernameLama)
    }

    private fun getPostUpdateData(){
        viewModel.getUpdateData().observe(this@AdminDetailUserAccountActivity){result->
            when(result){
                is UIState.Success-> setSuccessUpdateData(result.data[0])
                is UIState.Failure-> setFailureUpdateData()
            }
        }
    }

    private fun setFailureUpdateData() {
        Toast.makeText(this@AdminDetailUserAccountActivity, "Gagal", Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessUpdateData(responseModel: ResponseModel) {
        if(responseModel.status == "0"){
            user = userTemp
            setData(user)
            Toast.makeText(this@AdminDetailUserAccountActivity, "Berhasil", Toast.LENGTH_SHORT).show()
        } else{
            Toast.makeText(this@AdminDetailUserAccountActivity, "Gagal", Toast.LENGTH_SHORT).show()
        }
        loading.alertDialogCancel()
    }

    private fun dialogHapusUser(idUser: String, namaUser:String){
        val viewAlertDialog = View.inflate(this@AdminDetailUserAccountActivity, R.layout.alert_dialog_konfirmasi, null)

        val tvTitleKonfirmasi = viewAlertDialog.findViewById<TextView>(R.id.tvTitleKonfirmasi)
        val tvBodyKonfirmasi = viewAlertDialog.findViewById<TextView>(R.id.tvBodyKonfirmasi)

        val btnKonfirmasi = viewAlertDialog.findViewById<Button>(R.id.btnKonfirmasi)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

        tvTitleKonfirmasi.text = "Hapus $namaUser?"
        tvBodyKonfirmasi.text = "Menghapus Data User Akan menghapus menghilangkan Akun Ini. Yakin Hapus?"

        val alertDialog = AlertDialog.Builder(this@AdminDetailUserAccountActivity)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        btnKonfirmasi.setOnClickListener {
            dialogInputan.dismiss()
            postHapusUser(idUser)
            loading.alertDialogLoading()
        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }

    private fun postHapusUser(idUser: String){
        viewModel.postHapusUser(idUser)
    }

    private fun getPostHapusUser(){
        viewModel.getHapusUser().observe(this@AdminDetailUserAccountActivity){result->
            when(result){
                is UIState.Success-> setDataSuccessHapusUser(result.data)
                is UIState.Failure-> setDataFailureHapusUser(result.message)
            }
        }
    }

    private fun setDataSuccessHapusUser(data: ArrayList<ResponseModel>) {
        if(data[0].status == "0"){
            Toast.makeText(this@AdminDetailUserAccountActivity, "Berhasil Hapus", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@AdminDetailUserAccountActivity, AdminUsersActivity::class.java))
            finish()
        } else{
            Toast.makeText(this@AdminDetailUserAccountActivity, "Gagal hapus", Toast.LENGTH_SHORT).show()
        }
        loading.alertDialogCancel()
    }

    private fun setDataFailureHapusUser(message: String) {
        Toast.makeText(this@AdminDetailUserAccountActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }
}