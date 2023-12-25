package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.admin.perumahan.list_perumahan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.munmundev.feri_aplikasipembayaraniuranair.R
import com.munmundev.feri_aplikasipembayaraniuranair.adapter.admin.AdminPerumahanAdapter
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.PerumahanModel
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.ResponseModel
import com.munmundev.feri_aplikasipembayaraniuranair.databinding.ActivityAdminPerumahanBinding
import com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.admin.perumahan.list_blok.AdminBlokPerumahanActivity
import com.munmundev.feri_aplikasipembayaraniuranair.utils.KontrolNavigationDrawer
import com.munmundev.feri_aplikasipembayaraniuranair.utils.LoadingAlertDialog
import com.munmundev.feri_aplikasipembayaraniuranair.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminPerumahanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminPerumahanBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private lateinit var loading: LoadingAlertDialog
    private lateinit var adapter: AdminPerumahanAdapter
    private lateinit var listPerumahan: PerumahanModel
    private val viewModel: PerumahanViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminPerumahanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        setLoading()
        setButton()
        fetchDataPerumahan()
        getDataPerumahan()
        getTambahPerumahan()
        getUpdatePerumahan()
    }

    private fun setButton() {
        binding.btnTambahPerumahan.setOnClickListener {
            dialogTambahData()
        }
    }

    private fun dialogTambahData() {
        val viewAlertDialog = View.inflate(this@AdminPerumahanActivity, R.layout.alert_dialog_update_perumahan, null)

        val tVTitle = viewAlertDialog.findViewById<TextView>(R.id.tVTitle)
        val etEditNama = viewAlertDialog.findViewById<EditText>(R.id.etEditNama)

        val btnSimpan = viewAlertDialog.findViewById<Button>(R.id.btnSimpan)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

        tVTitle.text = "Tambah Perumahan"

        val alertDialog = AlertDialog.Builder(this@AdminPerumahanActivity)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        btnSimpan.setOnClickListener {
            if(etEditNama.text.isEmpty()){
                etEditNama.error = "Tidak Boleh Kosong"
            } else{
                loading.alertDialogLoading()
                postTambahPerumahan(etEditNama.text.toString())
            }
            dialogInputan.dismiss()
        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }

    private fun postTambahPerumahan(namaPerumahan: String) {
        viewModel.postTambahPerumahan(namaPerumahan)
    }

    private fun getTambahPerumahan(){
        viewModel.getPostPerumahan().observe(this@AdminPerumahanActivity){result->
            when(result){
                is UIState.Success-> setDataSuccessTambahPerumahan(result.data)
                is UIState.Failure-> setDataFailureTambahPerumahan(result.message)
            }
        }
    }

    private fun setDataSuccessTambahPerumahan(data: ArrayList<ResponseModel>) {
        if(data[0].status=="0"){
            Toast.makeText(this@AdminPerumahanActivity, "Berhail Tambah", Toast.LENGTH_SHORT).show()
        } else{
            Toast.makeText(this@AdminPerumahanActivity, "Gagal Tambah", Toast.LENGTH_SHORT).show()
        }
        fetchDataPerumahan()
    }

    private fun setDataFailureTambahPerumahan(message: String) {
        Toast.makeText(this@AdminPerumahanActivity, message, Toast.LENGTH_SHORT).show()
        fetchDataPerumahan()
    }

    private fun setKontrolNavigationDrawer() {
        kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminPerumahanActivity)
        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminPerumahanActivity)
        }
    }

    private fun setLoading() {
        loading = LoadingAlertDialog(this@AdminPerumahanActivity)
        loading.alertDialogLoading()
    }

    private fun fetchDataPerumahan() {
        viewModel.fetchDataPerumahan()
    }

    private fun getDataPerumahan() {
        viewModel.getDataPerumahan().observe(this@AdminPerumahanActivity){result->
            when(result){
                is UIState.Failure-> setFailurePerumahan(result.message)
                is UIState.Success-> setSuccessPerumahan(result.data)
            }
        }
    }

    private fun setFailurePerumahan(message: String) {
        Toast.makeText(this@AdminPerumahanActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessPerumahan(data: ArrayList<PerumahanModel>) {
        adapter = AdminPerumahanAdapter(data, object:  AdminPerumahanAdapter.Klik{
            override fun klikItem(listPerumahan: PerumahanModel) {
                val i = Intent(this@AdminPerumahanActivity, AdminBlokPerumahanActivity::class.java)
                i.putExtra("idPerumahan", listPerumahan.id_perumahan)
                i.putExtra("namaPerumahan", listPerumahan.nama_perumahan)
                i.putExtra("listPerumahan", data)
                startActivity(i)
            }

            override fun klikSetting(listPerumahan: PerumahanModel, it: View) {
                val popupMenu = PopupMenu(this@AdminPerumahanActivity, it)
                popupMenu.inflate(R.menu.popup_menu_rincian_edit_delete)
                popupMenu.setOnMenuItemClickListener(object :
                    PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                        when (menuItem!!.itemId) {
                            R.id.rincian -> {
                                val i = Intent(this@AdminPerumahanActivity, AdminBlokPerumahanActivity::class.java)
                                i.putExtra("idPerumahan", listPerumahan.id_perumahan)
                                i.putExtra("namaPerumahan", listPerumahan.nama_perumahan)
                                i.putExtra("listPerumahan", data)
                                startActivity(i)
                                return true
                            }
                            R.id.edit -> {
                                dialogEditPerumahan(listPerumahan)
                                return true
                            }
                            R.id.hapus -> {
                                dialogHapusPerumahan(listPerumahan.nama_perumahan, listPerumahan.id_perumahan)
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
            rvPerumahan.layoutManager = LinearLayoutManager(this@AdminPerumahanActivity, LinearLayoutManager.VERTICAL, false)
            rvPerumahan.adapter = adapter
        }
        loading.alertDialogCancel()
    }

    private fun dialogEditPerumahan(listPerumahan: PerumahanModel) {
        val viewAlertDialog = View.inflate(this@AdminPerumahanActivity, R.layout.alert_dialog_update_perumahan, null)

        val tVTitle = viewAlertDialog.findViewById<TextView>(R.id.tVTitle)
        val etEditNama = viewAlertDialog.findViewById<EditText>(R.id.etEditNama)

        val btnSimpan = viewAlertDialog.findViewById<Button>(R.id.btnSimpan)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

        tVTitle.text = "Update Perumahan"
        etEditNama.setText(listPerumahan.nama_perumahan)

        val alertDialog = AlertDialog.Builder(this@AdminPerumahanActivity)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        btnSimpan.setOnClickListener {
            if(etEditNama.text.isEmpty()){
                etEditNama.error = "Tidak Boleh Kosong"
            } else{
                loading.alertDialogLoading()
                postUpdatePerumahan(etEditNama.text.toString(), listPerumahan.id_perumahan!!)
            }
            dialogInputan.dismiss()
        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }

    private fun postUpdatePerumahan(namaPerumahan: String, idPerumahan: String) {
        viewModel.postUpdatePerumahan(namaPerumahan, idPerumahan)
    }

    private fun getUpdatePerumahan(){
        viewModel.getPostUpdatePerumahan().observe(this@AdminPerumahanActivity){result->
            when(result){
                is UIState.Success-> setDataSuccessUpdatePerumahan(result.data)
                is UIState.Failure-> setDataFailureUpdatePerumahan(result.message)
            }
        }
    }

    private fun setDataSuccessUpdatePerumahan(data: ArrayList<ResponseModel>) {
        if(data[0].status == "0"){
            Toast.makeText(this@AdminPerumahanActivity, "Berhasil", Toast.LENGTH_SHORT).show()
        } else{
            Toast.makeText(this@AdminPerumahanActivity, "Gagal", Toast.LENGTH_SHORT).show()
        }
        fetchDataPerumahan()
    }

    private fun setDataFailureUpdatePerumahan(message: String) {
        Toast.makeText(this@AdminPerumahanActivity, message, Toast.LENGTH_SHORT).show()
        fetchDataPerumahan()
    }

    private fun dialogHapusPerumahan(namaPerumahan: String?, idPerumahan: String?) {
        val viewAlertDialog = View.inflate(this@AdminPerumahanActivity, R.layout.alert_dialog_konfirmasi, null)

        val tvTitleKonfirmasi = viewAlertDialog.findViewById<TextView>(R.id.tvTitleKonfirmasi)
        val tvBodyKonfirmasi = viewAlertDialog.findViewById<TextView>(R.id.tvBodyKonfirmasi)

        val btnKonfirmasi = viewAlertDialog.findViewById<Button>(R.id.btnKonfirmasi)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

        tvTitleKonfirmasi.text = "Hapus $namaPerumahan?"
        tvBodyKonfirmasi.text = "Menghapus Data Perumahan Akan menghapus semua data blok dan alamat user yang berkaitan"

        val alertDialog = AlertDialog.Builder(this@AdminPerumahanActivity)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        btnKonfirmasi.setOnClickListener {
            dialogInputan.dismiss()
        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }

}