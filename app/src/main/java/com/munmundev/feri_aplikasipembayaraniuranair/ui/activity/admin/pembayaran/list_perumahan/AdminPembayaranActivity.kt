package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.admin.pembayaran.list_perumahan

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.munmundev.feri_aplikasipembayaraniuranair.R
import com.munmundev.feri_aplikasipembayaraniuranair.adapter.admin.AdminListPerumahanAdapter
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.PerumahanModel
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.ResponseModel
import com.munmundev.feri_aplikasipembayaraniuranair.databinding.ActivityAdminPembayaranBinding
import com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.admin.main.AdminMainActivity
import com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.admin.pembayaran.list_blok.AdminListBlokActivity
import com.munmundev.feri_aplikasipembayaraniuranair.utils.KontrolNavigationDrawer
import com.munmundev.feri_aplikasipembayaraniuranair.utils.LoadingAlertDialog
import com.munmundev.feri_aplikasipembayaraniuranair.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminPembayaranActivity : AppCompatActivity() {
    lateinit var binding : ActivityAdminPembayaranBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private lateinit var loading: LoadingAlertDialog
    private lateinit var adapter: AdminListPerumahanAdapter
    private val viewModel: PembayaranListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminPembayaranBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawerSetting()
        setLoadingSetting()
        button()
        fetchDataPerumahan()
        getDataPerumahan()
        getPostTambahSemuaPembayaran()
    }

    private fun fetchDataPerumahan() {
        viewModel.fetchDataPerumahan()
    }

    private fun button() {
        binding.btnTambahSemuaPembayaran.setOnClickListener {
            val viewAlertDialog = View.inflate(this@AdminPembayaranActivity, R.layout.alert_dialog_konfirmasi, null)
            val tvTitleKonfirmasi = viewAlertDialog.findViewById<TextView>(R.id.tvTitleKonfirmasi)
            val tvBodyKonfirmasi = viewAlertDialog.findViewById<TextView>(R.id.tvBodyKonfirmasi)
            val btnKonfirmasi = viewAlertDialog.findViewById<Button>(R.id.btnKonfirmasi)
            val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)
            tvTitleKonfirmasi.text = "Konfirmasi"
            tvBodyKonfirmasi.text = "Tambah Semua Untuk Pembayaran User Bulan Ini?"

            val alertDialog = AlertDialog.Builder(this@AdminPembayaranActivity)
            alertDialog.setView(viewAlertDialog)
            val dialog = alertDialog.create()
            dialog.show()

            btnKonfirmasi.setOnClickListener {
                dialog.dismiss()
                loading.alertDialogLoading()
                postTambahSemuaPembayaran()
            }
            btnBatal.setOnClickListener {
                dialog.dismiss()
            }
        }
    }

    private fun postTambahSemuaPembayaran() {
        viewModel.postSemuaPembayaranBulanIni()
    }

    private fun getPostTambahSemuaPembayaran(){
        viewModel.getPostSemuaPembayaranBulanIni().observe(this@AdminPembayaranActivity){result->
            when(result){
                is UIState.Success-> setTambahSemuaPembayaranSuccess(result.data)
                is UIState.Failure-> setTambahSemuaPembayaranFailure(result.message)
            }
        }
    }

    private fun setTambahSemuaPembayaranSuccess(data: ArrayList<ResponseModel>) {
        Toast.makeText(this@AdminPembayaranActivity, "Berhasil", Toast.LENGTH_SHORT).show()
        fetchDataPerumahan()
    }

    private fun setTambahSemuaPembayaranFailure(message: String) {
        Toast.makeText(this@AdminPembayaranActivity, message, Toast.LENGTH_SHORT).show()
        fetchDataPerumahan()
    }

    private fun setKontrolNavigationDrawerSetting() {
        kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminPembayaranActivity)
        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminPembayaranActivity)
        }
    }

    private fun setLoadingSetting() {
        loading = LoadingAlertDialog(this@AdminPembayaranActivity)
        loading.alertDialogLoading()
    }

    private fun getDataPerumahan() {
        viewModel.getDataPerumahan().observe(this@AdminPembayaranActivity){result->
            when(result){
                is UIState.Success->{
                    setDataSuccess(result.data)
                }
                is UIState.Failure->{
                    setDataFailure(result.message)
                }
            }
        }
    }

    private fun setDataSuccess(data: ArrayList<PerumahanModel>) {
        if(data.isNotEmpty()){
            adapter = AdminListPerumahanAdapter(data, object : AdminListPerumahanAdapter.onClick{
                override fun ClickItem(perumahanModel: PerumahanModel, it:View) {
                    val intent = Intent(this@AdminPembayaranActivity, AdminListBlokActivity::class.java)
                    intent.putExtra("nama_perumahan", perumahanModel.nama_perumahan)
                    intent.putExtra("id_perumahan", perumahanModel.id_perumahan)
                    startActivity(intent)
                }
            })

            binding.rvPerumahan.layoutManager = GridLayoutManager(this@AdminPembayaranActivity, 2)
            binding.rvPerumahan.adapter = adapter
        } else{
            Toast.makeText(this@AdminPembayaranActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }

        loading.alertDialogCancel()

    }

    private fun setDataFailure(message: String) {
        Toast.makeText(this@AdminPembayaranActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    override fun onBackPressed() {
        startActivity(Intent(this@AdminPembayaranActivity, AdminMainActivity::class.java))
        finish()
        super.onBackPressed()
    }
}