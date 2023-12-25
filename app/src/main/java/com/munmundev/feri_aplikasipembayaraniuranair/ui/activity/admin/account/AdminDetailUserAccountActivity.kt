package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.admin.account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.munmundev.feri_aplikasipembayaraniuranair.R
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.PerumahanModel
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDetailUserAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDataSebelumnya()
        setData(user)
        fetchDataPerumahan()
        getDataPerumahan()
        fetchDataBlokPerumahan()
        getDataBlokPerumahan()
        setButton()
        getPostUpdateData()
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
            etAlamat.text = user.alamat
            etNomorHp.text = user.nomorHp
            etUsername.text = user.username
            etPassword.text = user.password
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
            listNamaPerumahan.add(value.nama_perumahan!!)
            listIdPerumahan.add(value.id_perumahan!!)
        }
    }

    private fun setDataFailurePerumahan(message: String) {
        listNamaPerumahan = arrayListOf()
    }

    private fun setPerumahan() {
        listNamaBlokPerumahan = arrayListOf()
        listIdBlokPerumahan = arrayListOf()

        for(value in listBlokPerumahan){
            if(value.id_blok==user.idBlok){
                idPerumahan = value.id_perumahan!!
                idBlokPerumahan = value.id_blok!!
            }
        }

        for(value in listBlokPerumahan){
            if(value.id_perumahan == idPerumahan){
                listNamaBlokPerumahan.add(value.blok_perumahan!!)
                listIdBlokPerumahan.add(value.id_blok!!)
            }
        }

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
            setPerumahan()
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
        val etEditNomorHp = viewAlertDialog.findViewById<EditText>(R.id.etEditNomorHp)
        val etEditUsername = viewAlertDialog.findViewById<EditText>(R.id.etEditUsername)
        val etEditPassword = viewAlertDialog.findViewById<EditText>(R.id.etEditPassword)

        val btnSimpan = viewAlertDialog.findViewById<Button>(R.id.btnSimpan)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

        etEditNama.setText(user.nama)
        etEditNomorHp.setText(user.nomorHp)
        etEditUsername.setText(user.username)
        etEditPassword.setText(user.password)

        val arrayAdapter = ArrayAdapter(this@AdminDetailUserAccountActivity, android.R.layout.simple_spinner_item, listNamaPerumahan)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        var id = 0
        for((no, value) in listIdPerumahan.withIndex()){
            if(value == idPerumahan){
                id = no
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

//                valueIdPerumahan = listIdPerumahan[numberPosition]
//                Toast.makeText(this@AdminDetailUserAccountActivity, "$valueIdPerumahan", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        val arrayAdapterBlok = ArrayAdapter(this@AdminDetailUserAccountActivity, android.R.layout.simple_spinner_item, listNamaBlokPerumahan)
        arrayAdapterBlok.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        id = 0
        for((no, value) in listIdBlokPerumahan.withIndex()){
            if(value == idPerumahan){
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

//                valueIdPerumahan = listIdPerumahan[numberPosition]
//                Toast.makeText(this@AdminDetailUserAccountActivity, "$valueIdPerumahan", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        val alertDialog = AlertDialog.Builder(this@AdminDetailUserAccountActivity)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        btnSimpan.setOnClickListener {
            loading.alertDialogLoading()
//            val userModel = UsersModel(user.idUser, etEditNama.text.toString(), etEditAlamat.text.toString(), etEditNomorHp.text.toString(), etEditUsername.text.toString(), etEditPassword.text.toString(), "user")
//            postUpdateData(dialogInputan, userModel)
        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }

    private fun postUpdateData(dialog: AlertDialog, usersModel: UsersModel){
        viewModel.postUpdateUser(usersModel.idUser!!, usersModel.nama!!, usersModel.alamat!!, usersModel.nomorHp!!, usersModel.username!!, usersModel.password!!)
        userTemp = usersModel
        dialog.dismiss()
    }

    private fun setButton() {
        binding.apply {
            btnBack.setOnClickListener {
                finish()
            }
            btnUbahData.setOnClickListener {
                dialogUbahData()
            }
        }
    }

    private fun getPostUpdateData(){
        viewModel.getUpdateData().observe(this@AdminDetailUserAccountActivity){result->
            when(result){
                is UIState.Success->{
                    setSuccessUpdateData()
                    setData(userTemp)
                }
                is UIState.Failure->{
                    setFailureUpdateData()
                }
            }
        }
    }

    private fun setFailureUpdateData() {
        Toast.makeText(this@AdminDetailUserAccountActivity, "Gagal", Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessUpdateData() {
        Toast.makeText(this@AdminDetailUserAccountActivity, "Berhasil", Toast.LENGTH_SHORT).show()
    }
}