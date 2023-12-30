package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.register

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
import com.munmundev.feri_aplikasipembayaraniuranair.databinding.ActivityRegisterBinding
import com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.login.LoginActivity
import com.munmundev.feri_aplikasipembayaraniuranair.utils.LoadingAlertDialog
import com.munmundev.feri_aplikasipembayaraniuranair.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    lateinit var loading: LoadingAlertDialog
    private val viewModel : RegisterViewModel by viewModels()
    private lateinit var listPerumahan: ArrayList<PerumahanModel>
    private var listBlokPerumahan: ArrayList<PerumahanModel> = arrayListOf()
    private lateinit var listNamaPerumahan: ArrayList<String>
    private lateinit var listIdPerumahan: ArrayList<String>
    private lateinit var listNamaBlokPerumahan: ArrayList<String>
    private lateinit var listIdBlokPerumahan: ArrayList<String>
    private var idBlokPerumahan: String = "0"
    private var valueIdBlokPerumahan: String = "0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loading = LoadingAlertDialog(this@RegisterActivity)

        button()
        setNullArrayList()
        fetchDataPerumahan()
        fetchDataBlokPerumahan()
        getDataPerumahan()
        getDataBlokPerumahan()
        setDataSpinner()
        getTambahData()
        getResponseRegisterUser()

    }

    private fun setNullArrayList() {
        listIdPerumahan = arrayListOf()
        listIdBlokPerumahan = arrayListOf()
        listNamaPerumahan = arrayListOf()
        listNamaBlokPerumahan = arrayListOf()
    }

    private fun setDataSpinner() {
        setSpinnerPerumahan()
        setSpinnerBlokPerumahan(binding.spBlokPerumahan, listNamaBlokPerumahan, listIdBlokPerumahan)
    }

    private fun setSpinnerPerumahan(){
        val arrayAdapter = ArrayAdapter(this@RegisterActivity, android.R.layout.simple_spinner_item, listNamaPerumahan)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        var id = 0

        binding.apply {
            spPerumahan.adapter = arrayAdapter
            spPerumahan.setSelection(id)

            spPerumahan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val numberPosition = spPerumahan.selectedItemPosition

                    val valueIdPerumahan = listIdPerumahan[numberPosition]

                    setValueIdPerumahan(valueIdPerumahan)
                    setSpinnerBlokPerumahan(
                        spBlokPerumahan,
                        listNamaBlokPerumahan,
                        listIdBlokPerumahan
                    )
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
        }
    }

    private fun setSpinnerBlokPerumahan(
        spBlokPerumahan: Spinner,
        listNamaBlokPerumahan: ArrayList<String>,
        listIdBlokPerumahan: ArrayList<String>
    ) {
        val arrayAdapterBlok = ArrayAdapter(
            this@RegisterActivity,
            android.R.layout.simple_spinner_item,
            listNamaBlokPerumahan
        )
        arrayAdapterBlok.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spBlokPerumahan.adapter = arrayAdapterBlok
        spBlokPerumahan.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val numberPosition = spBlokPerumahan.selectedItemPosition

                valueIdBlokPerumahan = listIdBlokPerumahan[numberPosition]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun button() {
        buttonRegistrasi()
        buttonMoveLogin()
    }

    private fun buttonMoveLogin() {
        binding.tvLogin.setOnClickListener{
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun buttonRegistrasi() {
        binding.apply {
            btnRegistrasi.setOnClickListener {
                var cek = false
                if (etEditNama.toString().isEmpty()) {
                    etEditNama.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if (etEditNoAlamat.toString().isEmpty()) {
                    etEditNoAlamat.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if (etEditNomorHp.toString().isEmpty()) {
                    etEditNomorHp.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if (etEditUsername.toString().isEmpty()) {
                    etEditUsername.error = "Tidak Boleh Kosong"
                    cek = true
                }
                if (etEditPassword.toString().isEmpty()) {
                    etEditPassword.error = "Tidak Boleh Kosong"
                    cek = true
                }

                if (!cek) {
                    loading.alertDialogLoading()

                    postTambahData(
                        etEditNama.text.toString().trim(),
                        valueIdBlokPerumahan,
                        etEditNoAlamat.text.toString().trim(),
                        etEditNomorHp.text.toString(),
                        etEditUsername.text.toString().trim(),
                        etEditPassword.text.toString().trim()
                    )
                }
            }
        }
    }


    private fun getResponseRegisterUser(){
        viewModel.getRegisterUser().observe(this@RegisterActivity){ response ->
            when(response){
                is UIState.Success ->{
                    if (!response.data.isNullOrEmpty()){
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

    private fun fetchDataPerumahan() {
        viewModel.fetchDataPerumahan()
    }

    private fun getDataPerumahan() {
        viewModel.getDataPerumahan().observe(this@RegisterActivity){result->
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

        setSpinnerPerumahan()

    }

    private fun setDataFailurePerumahan(message: String) {
        listNamaPerumahan = arrayListOf()
    }

    private fun fetchDataBlokPerumahan() {
        viewModel.fetchDataBlokPerumahan()
    }

    private fun getDataBlokPerumahan() {
        viewModel.getDataBlokPerumahan().observe(this@RegisterActivity){result->
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
            setBlokPerumahan(data)
        }

        setDataSpinner()
    }

    private fun setBlokPerumahan(data: ArrayList<PerumahanModel>) {
        listNamaBlokPerumahan = arrayListOf()
        listIdBlokPerumahan = arrayListOf()

        for(value in data){
            listNamaBlokPerumahan.add(value.blok_perumahan!!)
            listIdBlokPerumahan.add(value.id_blok!!)
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

    }

    private fun postTambahData(nama: String, idBlokPerumahan:String, noAlamat: String, nomorHp: String, username: String, password: String){
        viewModel.postRegisterUser(nama, idBlokPerumahan, noAlamat, nomorHp, username, password, "user")
    }

    private fun getTambahData(){
        viewModel.getRegisterUser().observe(this@RegisterActivity){result->
            when(result){
                is UIState.Success-> setTambahDataSuccess(result.data)
                is UIState.Failure-> setTambahDataFailure(result.message)
            }
        }
    }

    private fun setTambahDataSuccess(responseModel: ArrayList<ResponseModel>) {

        if(responseModel.isNotEmpty()){
            if(responseModel[0].status=="0"){
                dialogKonfirmasiBack()
            } else{
                Toast.makeText(this@RegisterActivity, responseModel[0].message_response, Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@RegisterActivity, "Gagal Tambah Data", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setTambahDataFailure(message: String) {
        Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun dialogKonfirmasiBack(){
        val viewAlertDialog = View.inflate(this@RegisterActivity, R.layout.alert_dialog_konfirmasi, null)

        val tvTitleKonfirmasi = viewAlertDialog.findViewById<TextView>(R.id.tvTitleKonfirmasi)
        val tvBodyKonfirmasi = viewAlertDialog.findViewById<TextView>(R.id.tvBodyKonfirmasi)
        val btnKonfirmasi = viewAlertDialog.findViewById<Button>(R.id.btnKonfirmasi)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)


        tvTitleKonfirmasi.text = "Tambah Akun Berhasil"
        tvBodyKonfirmasi.text = "Berhasil menambahkan akun, tekan konfirmasi untuk berpindah ke halaman login"

        val alertDialog = AlertDialog.Builder(this@RegisterActivity)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        btnKonfirmasi.setOnClickListener {
            val i = Intent(this@RegisterActivity, LoginActivity::class.java)
            i.putExtra("username", binding.etEditUsername.text.toString())
            i.putExtra("password", binding.etEditPassword.text.toString())
            startActivity(i)
        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        finish()
    }

}