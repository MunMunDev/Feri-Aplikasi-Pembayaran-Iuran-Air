package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.admin.pembayaran.riwayat

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.munmundev.feri_aplikasipembayaraniuranair.R
import com.munmundev.feri_aplikasipembayaraniuranair.adapter.user.RiwayatPembayaranAdapter
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.PembayaranModel
import com.munmundev.feri_aplikasipembayaraniuranair.databinding.ActivityAdminRiwayatPembayaranBinding
import com.munmundev.feri_aplikasipembayaraniuranair.utils.KonversiRupiah
import com.munmundev.feri_aplikasipembayaraniuranair.utils.LoadingAlertDialog
import com.munmundev.feri_aplikasipembayaraniuranair.utils.TanggalDanWaktu
import com.munmundev.feri_aplikasipembayaraniuranair.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AdminRiwayatPembayaranActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminRiwayatPembayaranBinding
    private val viewModel: AdminRiwayatPembayaranViewModel by viewModels()
    @Inject lateinit var tanggalDanWaktu: TanggalDanWaktu
    @Inject lateinit var rupiah: KonversiRupiah
    private lateinit var loading: LoadingAlertDialog
    private lateinit var adapter: RiwayatPembayaranAdapter
    private lateinit var idUser: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminRiwayatPembayaranBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setLoading()
        button()
        getDataPreviousActivity()
        fetchDdata(idUser)
        getData()
        getDeletePembayaran(idUser)
        getEditPembayaran(idUser)
    }

    private fun setLoading() {
        loading = LoadingAlertDialog(this@AdminRiwayatPembayaranActivity)
        loading.alertDialogLoading()
    }

    private fun getDataPreviousActivity() {
        idUser = intent.getStringExtra("idUser").toString()
    }

    private fun button() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun fetchDdata(idUser:String) {
        viewModel.fetchData(idUser)
    }

    private fun getData() {
        viewModel.getData().observe(this@AdminRiwayatPembayaranActivity){result->
            when(result){
                is UIState.Success->{
                    setData(result.data)
                }
                is UIState.Failure-> {
                    Toast.makeText(
                        this@AdminRiwayatPembayaranActivity,
                        result.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            loading.alertDialogCancel()
        }
    }

    private fun setData(data: ArrayList<PembayaranModel>) {
        var tempData = arrayListOf<PembayaranModel>()
        for (value in data){
            if (value.waktuPembayaran!!.isNotEmpty()){
                tempData.add(value)
            }
        }
        adapter = RiwayatPembayaranAdapter(tempData, object : RiwayatPembayaranAdapter.OnClick{
            override fun clickItem(pembayaran: PembayaranModel, it: View) {
                val popupMenu = PopupMenu(this@AdminRiwayatPembayaranActivity, it)
                popupMenu.inflate(R.menu.popup_menu_rincian_edit_delete)
                popupMenu.setOnMenuItemClickListener(object :
                    PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                        when (menuItem!!.itemId) {
                            R.id.edit -> {
                                clickEdit(pembayaran)
                                return true
                            }
                            R.id.hapus -> {
                                val arrayTanggal = tanggalDanWaktu.konversiBulan(pembayaran.tenggatWaktu!!).split(" ")
                                val tanggal = "${arrayTanggal[1]} ${arrayTanggal[2]}"
                                clickHapus(pembayaran.idPembayaran!!, tanggal)
                                return true
                            }
                            R.id.rincian->{
                                val i = Intent(this@AdminRiwayatPembayaranActivity, AdminRincianRiwayatPembayaranActivity::class.java)
                                i.putExtra("pembayaran", pembayaran)
                                startActivity(i)
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
            rvRiwayatPembayaran.layoutManager = LinearLayoutManager(this@AdminRiwayatPembayaranActivity, LinearLayoutManager.VERTICAL, false)
            rvRiwayatPembayaran.adapter = adapter
        }

    }

    private fun clickEdit(pembayaran: PembayaranModel) {
        val viewAlertDialog = View.inflate(this@AdminRiwayatPembayaranActivity, R.layout.alert_dialog_update_pembayaran, null)
        val btnSimpan = viewAlertDialog.findViewById<Button>(R.id.btnSimpan)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)
        val etEditTenggatWaktu = viewAlertDialog.findViewById<TextView>(R.id.etEditTenggatWaktu)
        val etEditTanggalPembayaran = viewAlertDialog.findViewById<TextView>(R.id.etEditTanggalPembayaran)
        val etEditWaktuPembayaran = viewAlertDialog.findViewById<TextView>(R.id.etEditWaktuPembayaran)
        val etEditBiaya = viewAlertDialog.findViewById<EditText>(R.id.etEditBiaya)
        val etEditDenda = viewAlertDialog.findViewById<EditText>(R.id.etEditDenda)
        val etEditBiayaAdmin = viewAlertDialog.findViewById<EditText>(R.id.etEditBiayaAdmin)

        Log.d(
            "TAG",
            "setDataEdit: ${pembayaran.tenggatWaktu} dan ${pembayaran.waktuPembayaran}"
        )
        val arrayTanggalDanWaktuPembayaran = pembayaran.waktuPembayaran!!.split(" ")
        val tanggalPembayaran = arrayTanggalDanWaktuPembayaran[0]
        val waktuPembayaran = arrayTanggalDanWaktuPembayaran[1]

        etEditTenggatWaktu.text = pembayaran.tenggatWaktu
        etEditTanggalPembayaran.text = tanggalPembayaran
        etEditWaktuPembayaran.text = waktuPembayaran
        etEditBiaya.setText(pembayaran.harga)
        etEditDenda.setText(pembayaran.denda)
        etEditBiayaAdmin.setText(pembayaran.biayaAdmin)

        val alertDialog = AlertDialog.Builder(this@AdminRiwayatPembayaranActivity)
        alertDialog.setView(viewAlertDialog)
        val dialog = alertDialog.create()
        dialog.show()

        etEditTenggatWaktu.setOnClickListener {
            tanggalDanWaktu.selectedDate(pembayaran.tenggatWaktu!!, etEditTenggatWaktu, this@AdminRiwayatPembayaranActivity)
        }
        etEditTanggalPembayaran.setOnClickListener {
            tanggalDanWaktu.selectedDate(tanggalPembayaran, etEditTanggalPembayaran, this@AdminRiwayatPembayaranActivity)
        }
        etEditWaktuPembayaran.setOnClickListener {
            tanggalDanWaktu.selectedTime(waktuPembayaran, etEditWaktuPembayaran, this@AdminRiwayatPembayaranActivity)
        }

        btnSimpan.setOnClickListener {
            val tanggalDanWaktuPembayaran = "${etEditTanggalPembayaran.text} ${etEditWaktuPembayaran.text}"
            val data = PembayaranModel(
                pembayaran.idPembayaran, idUser, "", "", etEditBiaya.text.toString(),
                etEditDenda.text.toString(), etEditBiayaAdmin.text.toString(),
                etEditTenggatWaktu.text.toString(), tanggalDanWaktuPembayaran,
            )

            Log.d("AdminRiwayatPembayaranTAG", "clickEdit: ${data.idPembayaran}, " +
                    "${data.idUser}, ${data.harga}, ${data.denda}, ${data.biayaAdmin}, " +
                    "${data.tenggatWaktu}, ${data.waktuPembayaran}")
            postEditPembayaran(data)
            loading.alertDialogLoading()
            dialog.dismiss()
        }
        btnBatal.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun postEditPembayaran(pembayaran: PembayaranModel) {
        viewModel.updatePembayaran(pembayaran)
    }
    private fun getEditPembayaran(idUser: String){
        viewModel.getUpdatePembayaran().observe(this@AdminRiwayatPembayaranActivity){result->
            when(result){
                is UIState.Success->{
                    if(result.data[0].status!! == "0"){
                        fetchDdata(idUser)
                        Toast.makeText(this@AdminRiwayatPembayaranActivity, "Berhasil Update Data", Toast.LENGTH_SHORT).show()
                        loading.alertDialogCancel()
                    } else{
                        Toast.makeText(this@AdminRiwayatPembayaranActivity, "Gagal update data", Toast.LENGTH_SHORT).show()
                        loading.alertDialogCancel()
                    }
                }
                is UIState.Failure->{
                    Toast.makeText(this@AdminRiwayatPembayaranActivity, result.message, Toast.LENGTH_SHORT).show()
                    loading.alertDialogCancel()
                }
            }
        }
    }

    private fun clickHapus(idPembayaran: String, tanggal:String) {
        val viewAlertDialog = View.inflate(this@AdminRiwayatPembayaranActivity, R.layout.alert_dialog_admin_hapus, null)
        val tvHapus = viewAlertDialog.findViewById<TextView>(R.id.tvHapus)
        val btnHapus = viewAlertDialog.findViewById<Button>(R.id.btnHapus)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)
        tvHapus.text = "Hapus Data Tanggal \n$tanggal"

        val alertDialog = AlertDialog.Builder(this@AdminRiwayatPembayaranActivity)
        alertDialog.setView(viewAlertDialog)
        val dialog = alertDialog.create()
        dialog.show()

        btnHapus.setOnClickListener {
            loading.alertDialogLoading()
            viewModel.deletePembayaran(idPembayaran)
            dialog.dismiss()
        }
        btnBatal.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun getDeletePembayaran(idUser: String){
        viewModel.getDeletePembayaran().observe(this@AdminRiwayatPembayaranActivity){result->
            when(result){
                is UIState.Success->{
                    if(result.data[0].status=="0"){
                        fetchDdata(idUser)
                        Toast.makeText(this@AdminRiwayatPembayaranActivity, "Berhasil Hapus", Toast.LENGTH_SHORT).show()
                    } else{
                        Toast.makeText(this@AdminRiwayatPembayaranActivity, "Gagal Hapus", Toast.LENGTH_SHORT).show()
                    }
                    loading.alertDialogCancel()
                }
                is UIState.Failure->{
                    Toast.makeText(this@AdminRiwayatPembayaranActivity, result.message, Toast.LENGTH_SHORT).show()
                    loading.alertDialogCancel()
                }
            }
        }
    }

}