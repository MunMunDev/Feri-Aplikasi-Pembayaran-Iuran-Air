package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.admin.account

import android.annotation.SuppressLint
import android.content.Intent
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
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.munmundev.feri_aplikasipembayaraniuranair.R
import com.munmundev.feri_aplikasipembayaraniuranair.adapter.admin.AdminUsersAdapter
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.PerumahanModel
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.ResponseModel
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.UsersModel
import com.munmundev.feri_aplikasipembayaraniuranair.databinding.ActivityAdminUsersBinding
import com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.admin.main.AdminMainActivity
import com.munmundev.feri_aplikasipembayaraniuranair.utils.KontrolNavigationDrawer
import com.munmundev.feri_aplikasipembayaraniuranair.utils.LoadingAlertDialog
import com.munmundev.feri_aplikasipembayaraniuranair.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminUsersActivity : AppCompatActivity() {
    private val TAG = "AdminUsersActivityTAG"
    private lateinit var binding: ActivityAdminUsersBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private lateinit var loading: LoadingAlertDialog
    private lateinit var adapter: AdminUsersAdapter
    private val viewModel: AccountViewModel by viewModels()
    private lateinit var listPerumahan: ArrayList<PerumahanModel>
    private lateinit var listBlokPerumahan: ArrayList<PerumahanModel>
    private lateinit var listNamaPerumahan: ArrayList<String>
    private lateinit var listIdPerumahan: ArrayList<String>
    private lateinit var listNamaBlokPerumahan: ArrayList<String>
    private lateinit var listIdBlokPerumahan: ArrayList<String>
    private var idBlokPerumahan: String = "0"
    private var valueIdBlokPerumahan: String = "0"

    // Filter
    private var listFilterNamaPerumahan: ArrayList<String> = arrayListOf()
    private var listFilterIdPerumahan: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawerSetting()
        setLoadingSetting()
        button()
        fetchDataUsers()
        getDataUsers()
        fetchDataPerumahan()
        getDataPerumahan()
        fetchDataBlokPerumahan()
        getDataBlokPerumahan()
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

    private fun fetchDataUsers() {
        viewModel.fetchDataUsers()
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
        if(data.isNotEmpty()){
            val tempData = arrayListOf<UsersModel>()
            val sortedDataUser = data.sortedWith(
                compareBy {
                    it.perumahan!![0].id_perumahan
                }
            )

            for (value in sortedDataUser){
                tempData.add(value)
            }

            adapter = AdminUsersAdapter(data, object: AdminUsersAdapter.ClickItemListener{
                override fun onClickItem(user: UsersModel, it: View, position:Int) {
                    val i = Intent(this@AdminUsersActivity, AdminDetailUserAccountActivity::class.java)
                    i.putExtra("data", user)
                    startActivity(i)
                }
            })

            binding.rvAkun.layoutManager = LinearLayoutManager(this@AdminUsersActivity, LinearLayoutManager.VERTICAL, false)
            binding.rvAkun.adapter = adapter
        } else{
            Toast.makeText(this@AdminUsersActivity, "Ada kesalahan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setDataFailure(message: String) {
        Toast.makeText(this@AdminUsersActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun fetchDataPerumahan() {
        viewModel.fetchDataPerumahan()
    }

    private fun getDataPerumahan() {
        viewModel.getDataPerumahan().observe(this@AdminUsersActivity){result->
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

        listFilterNamaPerumahan = arrayListOf()
        listFilterIdPerumahan = arrayListOf()
        listFilterNamaPerumahan.add("Filter By Default")
        listFilterIdPerumahan.add("0")
        for(value in data){
            if(value.jumlah_blok != "0"){
                listNamaPerumahan.add(value.nama_perumahan!!)
                listIdPerumahan.add(value.id_perumahan!!)

                listFilterNamaPerumahan.add(value.nama_perumahan!!)
                listFilterIdPerumahan.add(value.id_perumahan!!)
            }
        }
        setSpinnerFilterData()
    }

    var cekSpinnerKlik = false
    private fun setSpinnerFilterData() {
        val arrayAdapter = ArrayAdapter(this@AdminUsersActivity, android.R.layout.simple_spinner_item, listFilterNamaPerumahan)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spFilter.adapter = arrayAdapter
        binding.spFilter.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val numberPosition = binding.spFilter.selectedItemPosition
                val valueIdPerumahan = listFilterIdPerumahan[numberPosition]
                if(!cekSpinnerKlik){
                    cekSpinnerKlik = true
                } else{
                    adapter.setFilterData(valueIdPerumahan)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

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
        viewModel.getDataBlokPerumahan().observe(this@AdminUsersActivity){result->
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

    private fun dialogTambahData(){
        val viewAlertDialog = View.inflate(this@AdminUsersActivity, R.layout.alert_dialog_update_akun, null)

        val tVTitle = viewAlertDialog.findViewById<TextView>(R.id.tVTitle)
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

        tVTitle.text = "Tambah User"

        val arrayAdapter = ArrayAdapter(this@AdminUsersActivity, android.R.layout.simple_spinner_item, listNamaPerumahan)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        var id = 0

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

                val valueIdPerumahan = listIdPerumahan[numberPosition]

                setValueIdPerumahan(valueIdPerumahan)
                setSpinnerBlokPerumahan(spBlokPerumahan, listNamaBlokPerumahan, listIdBlokPerumahan)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        setSpinnerBlokPerumahan(spBlokPerumahan, listNamaBlokPerumahan, listIdBlokPerumahan)

        val alertDialog = AlertDialog.Builder(this@AdminUsersActivity)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        btnSimpan.setOnClickListener {
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
            this@AdminUsersActivity,
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
                Log.d(TAG, "onItemSelected: ${valueIdBlokPerumahan}")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun postTambahData(nama: String, idBlokPerumahan:String, noAlamat: String, nomorHp: String, username: String, password: String){
        viewModel.postTambahUser(nama, idBlokPerumahan, noAlamat, nomorHp, username, password, "user")
    }

    private fun getTambahData(){
        viewModel.getTambahData().observe(this@AdminUsersActivity){result->
            when(result){
                is UIState.Success-> setTambahDataSuccess(result.data)
                is UIState.Failure-> setTambahDataFailure(result.message)
            }
        }
    }

    private fun setTambahDataSuccess(responseModel: ArrayList<ResponseModel>) {

        if(responseModel.isNotEmpty()){
            Toast.makeText(this@AdminUsersActivity, responseModel[0].message_response, Toast.LENGTH_SHORT).show()
        } else{
            Toast.makeText(this@AdminUsersActivity, "Gagal Tambah Data", Toast.LENGTH_SHORT).show()
        }
        fetchDataUsers()
    }


    private fun setTambahDataFailure(message: String) {
        Toast.makeText(this@AdminUsersActivity, message, Toast.LENGTH_SHORT).show()
        Log.d(TAG, "error: $message")
        fetchDataUsers()
    }

    override fun onBackPressed() {
        startActivity(Intent(this@AdminUsersActivity, AdminMainActivity::class.java))
        finish()
        super.onBackPressed()
    }
}