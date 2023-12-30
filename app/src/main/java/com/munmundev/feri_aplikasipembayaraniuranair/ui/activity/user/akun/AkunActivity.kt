package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.user.akun

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
import com.example.muharya_pengingatjadwalkeretaapi.utils.SharedPreferencesLogin
import com.munmundev.feri_aplikasipembayaraniuranair.R
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.PerumahanModel
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.ResponseModel
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
    private lateinit var userTemp: UsersModel
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
    private var cekUsername = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAkunBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSharedPreferences()
        setNavigationDrawer()
        button()
        setLoading()
        setData()
        getData()
        fetchDataPerumahan()
        getDataPerumahan()
        fetchDataBlokPerumahan()
        getDataBlokPerumahan()
        getPostUpdateData()
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
            etNoAlamat.setText(sharedPreferences.getAlamat())
            etNomorHp.setText(sharedPreferences.getNomorHp())
            etUsername.setText(sharedPreferences.getUsername())
            etPassword.setText(sharedPreferences.getPassword())
        }
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

    private fun fetchDataPerumahan() {
        viewModel.fetchDataPerumahan()
    }

    private fun getDataPerumahan() {
        viewModel.getDataPerumahan().observe(this@AkunActivity){result->
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
        viewModel.getDataBlokPerumahan().observe(this@AkunActivity){result->
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
            if(value.id_blok==sharedPreferences.getIdBlok()){
                idPerumahan = value.id_perumahan!!
                idBlokPerumahan = value.id_blok!!
                valueIdPerumahan = value.id_perumahan!!
                valueIdBlokPerumahan = value.id_blok!!

                valueNamaPerumahan = value.nama_perumahan!!
                valueNamaBlokPerumahan = value.blok_perumahan!!
                binding.apply {
                    etPerumahan.text = valueNamaPerumahan
                    etBlokPerumahan.text = valueNamaBlokPerumahan
                }
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

    private fun button() {
        binding.btnUbahData.setOnClickListener {
            val viewAlertDialog = View.inflate(this@AkunActivity, R.layout.alert_dialog_update_akun, null)

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

            etEditNama.setText(sharedPreferences.getNama())
            etEditNomorHp.setText(sharedPreferences.getNomorHp())
            etEditNoAlamat.setText(sharedPreferences.getAlamat())
            etEditUsername.setText(sharedPreferences.getUsername())
            etEditPassword.setText(sharedPreferences.getPassword())

            val arrayAdapter = ArrayAdapter(this@AkunActivity, android.R.layout.simple_spinner_item, listNamaPerumahan)
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


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

            val alertDialog = AlertDialog.Builder(this@AkunActivity)
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
                    Toast.makeText(this@AkunActivity, "Update", Toast.LENGTH_SHORT).show()
                    if(sharedPreferences.getUsername() == etEditUsername.text.toString().trim()){
                        cekUsername = true
                    }
                    userTemp = UsersModel(
                        idUser = sharedPreferences.getIdUser().toString(),
                        idBlok = valueIdBlokPerumahan,
                        nama = etEditNama.text.toString(),
                        alamat = etEditNoAlamat.text.toString(),
                        nomorHp = etEditNomorHp.text.toString(),
                        username = etEditUsername.text.toString(),
                        password = etEditPassword.text.toString()
                    )
                    postUpdateData(
                        sharedPreferences.getIdUser().toString(),
                        etEditNama.text.toString(),
                        valueIdBlokPerumahan,
                        etEditNoAlamat.text.toString(),
                        etEditNomorHp.text.toString(),
                        etEditUsername.text.toString(),
                        etEditPassword.text.toString(),
                        sharedPreferences.getUsername()
                    )
                    loading.alertDialogLoading()

                    dialogInputan.dismiss()
                }

            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun setSpinnerBlokPerumahan(
        spBlokPerumahan: Spinner,
        listNamaBlokPerumahan: ArrayList<String>,
        listIdBlokPerumahan: ArrayList<String>
    ) {
        val arrayAdapterBlok = ArrayAdapter(
            this@AkunActivity,
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
        viewModel.getUpdateData().observe(this@AkunActivity){result->
            when(result){
                is UIState.Success-> setSuccessUpdateData(result.data[0])
                is UIState.Failure-> setFailureUpdateData()
            }
        }
    }

    private fun setFailureUpdateData() {
        Toast.makeText(this@AkunActivity, "Gagal", Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessUpdateData(responseModel: ResponseModel) {
        if(responseModel.status == "0"){
            sharedPreferences.setLogin(
                userTemp.idUser!!.trim().toInt(), userTemp.idBlok!!, userTemp.nama!!,
                userTemp.alamat!!, userTemp.nomorHp!!, userTemp.username!!,
                userTemp.password!!, "user")
            setData()
            Toast.makeText(this@AkunActivity, "Berhasil", Toast.LENGTH_SHORT).show()
        } else{
            Toast.makeText(this@AkunActivity, responseModel.message_response, Toast.LENGTH_SHORT).show()
        }
        loading.alertDialogCancel()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@AkunActivity, MainActivity::class.java))
        finish()
    }
}