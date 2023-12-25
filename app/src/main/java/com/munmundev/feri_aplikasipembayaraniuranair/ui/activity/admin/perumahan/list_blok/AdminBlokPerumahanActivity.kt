package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.admin.perumahan.list_blok

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.munmundev.feri_aplikasipembayaraniuranair.R
import com.munmundev.feri_aplikasipembayaraniuranair.adapter.admin.AdminListPerumahanAdapter
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.PerumahanModel
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.ResponseModel
import com.munmundev.feri_aplikasipembayaraniuranair.databinding.ActivityAdminBlokPerumahanBinding
import com.munmundev.feri_aplikasipembayaraniuranair.utils.LoadingAlertDialog
import com.munmundev.feri_aplikasipembayaraniuranair.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminBlokPerumahanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBlokPerumahanBinding
    private lateinit var loading: LoadingAlertDialog
    private var idPerumahan: String? = null
    private val viewModel: BlokPerumahanViewModel by viewModels()
    private lateinit var adapter: AdminListPerumahanAdapter
    private lateinit var listPerumahan : ArrayList<PerumahanModel>
    private lateinit var listNamaPerumahan : ArrayList<String>
    private lateinit var listIdPerumahan : ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBlokPerumahanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setLoading()
        setButton()
        setDataSebelumnya()
        fetchDataPerumahan()
        getDataPerumahan()
        getTambahBlokPerumahan()
        getUpdateBlokPerumahan()
        getHapusBlokPerumahan()
    }

    private fun setButton() {
        binding.apply {
            ivBack.setOnClickListener{
                finish()
            }
            btnTambahBlok.setOnClickListener {
                dialogTambahBlokPerumahan()
            }
        }
    }

    private fun dialogTambahBlokPerumahan() {
        val viewAlertDialog = View.inflate(this@AdminBlokPerumahanActivity, R.layout.alert_dialog_update_blok_perumahan, null)

        val tVTitleBlok = viewAlertDialog.findViewById<TextView>(R.id.tVTitleBlok)
        val spPerumahan = viewAlertDialog.findViewById<Spinner>(R.id.spPerumahan)
        val etEditNama = viewAlertDialog.findViewById<EditText>(R.id.etEditNama)

        val btnSimpan = viewAlertDialog.findViewById<Button>(R.id.btnSimpan)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

        var valueIdPerumahan = ""

        tVTitleBlok.text = "Update Blok Perumahan"

        val arrayAdapter = ArrayAdapter(this@AdminBlokPerumahanActivity, android.R.layout.simple_spinner_item, listNamaPerumahan)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        var id = 0
        for((no, value) in listIdPerumahan.withIndex()){
            if(value == idPerumahan){
                id = no
            }
        }
        spPerumahan.adapter = arrayAdapter
        spPerumahan.setSelection(id)

        spPerumahan.isEnabled = false

        val alertDialog = AlertDialog.Builder(this@AdminBlokPerumahanActivity)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        btnSimpan.setOnClickListener {
            if(etEditNama.text.isEmpty()){
                etEditNama.error = "Tidak Boleh Kosong"
            } else{
                loading.alertDialogLoading()
                postTambahBlokPerumahan(idPerumahan!!, etEditNama.text.toString())
            }
            dialogInputan.dismiss()
        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }

    private fun postTambahBlokPerumahan(idPerumahan: String, namaBlokPerumahan: String) {
        viewModel.postTambahDataPerumahan(idPerumahan, namaBlokPerumahan)
    }

    private fun getTambahBlokPerumahan(){
        viewModel.getTambahPerumahan().observe(this@AdminBlokPerumahanActivity){result->
            when(result){
                is UIState.Success-> setDataSuccessTambahBlokPerumahan(result.data)
                is UIState.Failure-> setDataFailureTambahBlokPerumahan(result.message)
            }
        }
    }

    private fun setDataSuccessTambahBlokPerumahan(data: ArrayList<ResponseModel>) {
        if(data.isNotEmpty()){
            Toast.makeText(this@AdminBlokPerumahanActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
        } else{
            Toast.makeText(this@AdminBlokPerumahanActivity, "Gagal", Toast.LENGTH_SHORT).show()
        }
        fetchDataPerumahan()
    }

    private fun setDataFailureTambahBlokPerumahan(message: String) {
        Toast.makeText(this@AdminBlokPerumahanActivity, message, Toast.LENGTH_SHORT).show()
        fetchDataPerumahan()
    }

    private fun setDataSebelumnya() {
        idPerumahan = intent.getStringExtra("idPerumahan")
        val namaPerumahan = intent.getStringExtra("namaPerumahan")
        listPerumahan = intent.getParcelableArrayListExtra("listPerumahan")!!
        listNamaPerumahan = arrayListOf()
        listIdPerumahan = arrayListOf()
        for (value in listPerumahan){
            listNamaPerumahan.add(value.nama_perumahan!!)
            listIdPerumahan.add(value.id_blok!!)
        }

        binding.titleHeader.text = namaPerumahan
    }

    private fun setLoading() {
        loading = LoadingAlertDialog(this@AdminBlokPerumahanActivity)
        loading.alertDialogLoading()
    }

    private fun fetchDataPerumahan() {
        viewModel.fetchDataPerumahan(idPerumahan!!)
    }

    private fun getDataPerumahan() {
        viewModel.getBlokPerumahan().observe(this@AdminBlokPerumahanActivity){result->
            when(result){
                is UIState.Success-> setDataSuccessFetchBlokPerumahan(result.data)
                is UIState.Failure-> setDataFailureFetchBlokPerumahan(result.message)
            }
        }
    }

    private fun setDataSuccessFetchBlokPerumahan(data: ArrayList<PerumahanModel>) {
        Log.d("AdminBlokPerumahanActivityTAG", "data : $idPerumahan dan ${data.size}")
        adapter = AdminListPerumahanAdapter(data, object:AdminListPerumahanAdapter.onClick{
            override fun ClickItem(perumahanModel: PerumahanModel, it: View) {
                val popupMenu = PopupMenu(this@AdminBlokPerumahanActivity, it)
                popupMenu.inflate(R.menu.popup_menu)
                popupMenu.setOnMenuItemClickListener(object :
                    PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                        when (menuItem!!.itemId) {
                            R.id.edit -> {
                                dialogEditBlokPerumahan(idPerumahan!!, perumahanModel.id_blok!!, perumahanModel.blok_perumahan!!)
                                return true
                            }
                            R.id.hapus -> {
                                dialogHapusBlokPerumahan(perumahanModel.id_blok, perumahanModel.blok_perumahan)
                                return true
                            }
                        }
                        return true
                    }

                })
                popupMenu.show()
            }

        })
        binding.apply {
            rvBlok.layoutManager = GridLayoutManager(this@AdminBlokPerumahanActivity, 2)
            rvBlok.adapter = adapter
        }
        loading.alertDialogCancel()
    }

    private fun dialogEditBlokPerumahan(idPerumahan: String, idBlokPerumahan:String, blokPerumahan: String) {
        val viewAlertDialog = View.inflate(this@AdminBlokPerumahanActivity, R.layout.alert_dialog_update_blok_perumahan, null)

        val tVTitleBlok = viewAlertDialog.findViewById<TextView>(R.id.tVTitleBlok)
        val spPerumahan = viewAlertDialog.findViewById<Spinner>(R.id.spPerumahan)
        val etEditNama = viewAlertDialog.findViewById<EditText>(R.id.etEditNama)

        val btnSimpan = viewAlertDialog.findViewById<Button>(R.id.btnSimpan)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

        var valueIdPerumahan = ""

        tVTitleBlok.text = "Update Blok Perumahan"
        etEditNama.setText(blokPerumahan)

        val arrayAdapter = ArrayAdapter(this@AdminBlokPerumahanActivity, android.R.layout.simple_spinner_item, listNamaPerumahan)
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

                valueIdPerumahan = listIdPerumahan[numberPosition]
                Toast.makeText(this@AdminBlokPerumahanActivity, "$valueIdPerumahan", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        val alertDialog = AlertDialog.Builder(this@AdminBlokPerumahanActivity)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        btnSimpan.setOnClickListener {
            if(etEditNama.text.isEmpty()){
                etEditNama.error = "Tidak Boleh Kosong"
            } else{
                loading.alertDialogLoading()
                postUpdateBlokPerumahan(valueIdPerumahan, idBlokPerumahan, etEditNama.text.toString())
            }
            dialogInputan.dismiss()
        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }

    private fun postUpdateBlokPerumahan(
        idPerumahan: String,
        idBlokPerumahan: String,
        blokPerumahan: String
    ) {
        viewModel.postUpdateDataPerumahan(idPerumahan, idBlokPerumahan, blokPerumahan)
    }

    private fun getUpdateBlokPerumahan(){
        viewModel.getUpdatePerumahan().observe(this@AdminBlokPerumahanActivity){result->
            when(result){
                is UIState.Success-> setDataSuccessUpdatePerumahan(result.data)
                is UIState.Failure-> setDataFailureUpdatePerumahan(result.message)
            }
        }
    }

    private fun setDataSuccessUpdatePerumahan(data: ArrayList<ResponseModel>) {
        if(data[0].status == "0"){
            Toast.makeText(this@AdminBlokPerumahanActivity, "Berhasil", Toast.LENGTH_SHORT).show()
        } else{
            Toast.makeText(this@AdminBlokPerumahanActivity, "Gaagal", Toast.LENGTH_SHORT).show()
        }
        fetchDataPerumahan()
    }

    private fun setDataFailureUpdatePerumahan(message: String) {
        Toast.makeText(this@AdminBlokPerumahanActivity, message, Toast.LENGTH_SHORT).show()
        fetchDataPerumahan()
    }

    private fun dialogHapusBlokPerumahan(idBlok: String?, blokPerumahan: String?) {
        val viewAlertDialog = View.inflate(this@AdminBlokPerumahanActivity, R.layout.alert_dialog_konfirmasi, null)

        val tvTitleKonfirmasi = viewAlertDialog.findViewById<TextView>(R.id.tvTitleKonfirmasi)
        val tvBodyKonfirmasi = viewAlertDialog.findViewById<TextView>(R.id.tvBodyKonfirmasi)

        val btnKonfirmasi = viewAlertDialog.findViewById<Button>(R.id.btnKonfirmasi)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

        tvTitleKonfirmasi.text = "Hapus $blokPerumahan?"
        tvBodyKonfirmasi.text = "Menghapus Data Perumahan Akan menghapus semua data blok dan alamat user yang berkaitan"

        val alertDialog = AlertDialog.Builder(this@AdminBlokPerumahanActivity)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        btnKonfirmasi.setOnClickListener {
            dialogInputan.dismiss()
            postHapusBlokPerumahan(idBlok)
        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }

    private fun postHapusBlokPerumahan(idBlok: String?) {
        viewModel.postHapusDataPerumahan(idBlok!!)
    }

    private fun getHapusBlokPerumahan(){
        viewModel.getHapusPerumahan().observe(this@AdminBlokPerumahanActivity){result->
            when(result){
                is UIState.Success-> setDataSuccessHapusBlokPerumahan(result.data)
                is UIState.Failure-> setDataFailureHapusBlokPerumahan(result.message)
            }
        }
    }

    private fun setDataSuccessHapusBlokPerumahan(data: ArrayList<ResponseModel>) {
        if(data.isNotEmpty()){
            Toast.makeText(this@AdminBlokPerumahanActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
        } else{
            Toast.makeText(this@AdminBlokPerumahanActivity, "Gagal", Toast.LENGTH_SHORT).show()
        }
        fetchDataPerumahan()
    }

    private fun setDataFailureHapusBlokPerumahan(message: String) {
        Toast.makeText(this@AdminBlokPerumahanActivity, message, Toast.LENGTH_SHORT).show()
        fetchDataPerumahan()
    }

    private fun setDataFailureFetchBlokPerumahan(message: String) {
        Toast.makeText(this@AdminBlokPerumahanActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

}