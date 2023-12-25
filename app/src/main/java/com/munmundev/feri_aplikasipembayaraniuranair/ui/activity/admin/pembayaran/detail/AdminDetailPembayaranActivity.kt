package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.admin.pembayaran.detail

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.munmundev.feri_aplikasipembayaraniuranair.R
import com.munmundev.feri_aplikasipembayaraniuranair.adapter.admin.AdminDetailPembayaranAdapter
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.BiayaModel
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.PembayaranModel
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.ResponseModel
import com.munmundev.feri_aplikasipembayaraniuranair.databinding.ActivityAdminDetailPembayaranBinding
import com.munmundev.feri_aplikasipembayaraniuranair.utils.KonversiRupiah
import com.munmundev.feri_aplikasipembayaraniuranair.utils.LoadingAlertDialog
import com.munmundev.feri_aplikasipembayaraniuranair.utils.TanggalDanWaktu
import com.munmundev.feri_aplikasipembayaraniuranair.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class AdminDetailPembayaranActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminDetailPembayaranBinding
    private lateinit var loading: LoadingAlertDialog
    private val viewModel: AdminDetailPembayaranViewModel by viewModels()
    private lateinit var idUser: String
    @Inject lateinit var tanggalDanWaktu: TanggalDanWaktu
    @Inject lateinit var konversiRupiah: KonversiRupiah
    lateinit var adapter: AdminDetailPembayaranAdapter

    private var biaya:String = "7000"
    private var denda:String = "0"
    private var biayaAdmin:String = "1000"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDetailPembayaranBinding.inflate(layoutInflater)
        setContentView(binding.root)

        previousActivityData()
        setLoading()
        button()
        getData()
        getKonfirmasiPembayaran()
        getKonfirmasiSemuaPembayaran()
        getTambahPembayaran()
    }

    private fun setLoading() {
        loading = LoadingAlertDialog(this@AdminDetailPembayaranActivity)
        loading.alertDialogLoading()
    }

    private fun previousActivityData() {
        val extras = intent.extras
        if(extras != null) {
            idUser = extras.getString("idUser").toString()
            fetchData(idUser)
        }
    }

    private fun button() {
        binding.apply {
            btnBack.setOnClickListener {
                finish()
            }
            btnTambahPembayaran.setOnClickListener{
                tambahPembayaran()
            }
            btnKonfirmasiSemuaPembayaran.setOnClickListener {
                loading.alertDialogLoading()
                postKonfirmasiSemuaPembayaran(idUser)
            }
        }
    }

    private fun postKonfirmasiSemuaPembayaran(idUser: String) {
        viewModel.postKonfirmasiSemuaDataPembayaran(idUser)
    }

    private fun getKonfirmasiSemuaPembayaran(){
        viewModel.getKonfirmasiSemuaDataPembayaran().observe(this@AdminDetailPembayaranActivity){result->
            when(result){
                is UIState.Success-> setKonfirmasiSemuaDataSuccess(result.data)
                is UIState.Failure-> setKonfirmasiSemuaDataFailure(result.message)
            }
        }
    }

    private fun setKonfirmasiSemuaDataSuccess(responseModel: ArrayList<ResponseModel>) {
        if(responseModel.isNotEmpty()){
            if(responseModel[0].response=="0"){
                Toast.makeText(this@AdminDetailPembayaranActivity, "Berhasil Konfirmasi Semua Data", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this@AdminDetailPembayaranActivity, "Gagal Konfirmasi Semua Data", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(this@AdminDetailPembayaranActivity, "Data tidak ada", Toast.LENGTH_SHORT).show()
        }
        fetchData(idUser)
    }

    private fun setKonfirmasiSemuaDataFailure(message: String) {
        Toast.makeText(this@AdminDetailPembayaranActivity, message, Toast.LENGTH_SHORT).show()
        fetchData(idUser)
    }

    private fun tambahPembayaran() {
        val viewAlertDialog = View.inflate(this@AdminDetailPembayaranActivity, R.layout.alert_dialog_update_pembayaran, null)
        val btnSimpan = viewAlertDialog.findViewById<Button>(R.id.btnSimpan)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)
        val etEditTenggatWaktu = viewAlertDialog.findViewById<TextView>(R.id.etEditTenggatWaktu)
        val etEditTanggalPembayaran = viewAlertDialog.findViewById<TextView>(R.id.etEditTanggalPembayaran)
        val etEditWaktuPembayaran = viewAlertDialog.findViewById<TextView>(R.id.etEditWaktuPembayaran)
        val etEditBiaya = viewAlertDialog.findViewById<EditText>(R.id.etEditBiaya)
        val etEditDenda = viewAlertDialog.findViewById<EditText>(R.id.etEditDenda)
        val etEditBiayaAdmin = viewAlertDialog.findViewById<EditText>(R.id.etEditBiayaAdmin)

        val tvTitleTanggalPembayaran = viewAlertDialog.findViewById<TextView>(R.id.tvTitleTanggalPembayaran)
        val tvTitleWaktuPembayaran = viewAlertDialog.findViewById<TextView>(R.id.tvTitleWaktuPembayaran)
        val tVTitle = viewAlertDialog.findViewById<TextView>(R.id.tVTitle)

        val alertDialog = AlertDialog.Builder(this@AdminDetailPembayaranActivity)
        alertDialog.setView(viewAlertDialog)
        val dialog = alertDialog.create()
        dialog.show()

        etEditTenggatWaktu.setOnClickListener {
            selectedDate(tanggalDanWaktu.tanggalSekarangZonaMakassar(), etEditTenggatWaktu)
        }
        etEditTanggalPembayaran.visibility = View.GONE
        etEditWaktuPembayaran.visibility = View.GONE
        tvTitleTanggalPembayaran.visibility = View.GONE
        tvTitleWaktuPembayaran.visibility = View.GONE

        etEditBiaya.setText(biaya)
        etEditDenda.setText(denda)
        etEditBiayaAdmin.setText(biayaAdmin)

        etEditTenggatWaktu.text = tanggalDanWaktu.tanggalSekarangZonaMakassar()
        tVTitle.text = "Tambah Pembayaran"

        btnSimpan.setOnClickListener {
            val tanggalDanWaktuPembayaran = "${etEditTanggalPembayaran.text} ${etEditWaktuPembayaran.text}"
            val data = PembayaranModel(
                idUser, "", "", etEditBiaya.text.toString(),
                etEditDenda.text.toString(), etEditBiayaAdmin.text.toString(),
                etEditTenggatWaktu.text.toString(), tanggalDanWaktuPembayaran,
            )
            var isEnable = true
            if(etEditTenggatWaktu.text.isEmpty()){
                etEditTenggatWaktu.error = "Tidak Boleh Kosong"
                isEnable = false
            }
            if(etEditBiaya.text.isEmpty()){
                etEditBiaya.error = "Tidak Boleh Kosong"
//                etEditBiaya.setText("0")
                isEnable = false
            }
            if(etEditDenda.text.isEmpty()){
                etEditDenda.error = "Tidak Boleh Kosong"
//                etEditDenda.setText("0")
                isEnable = false
            }
            if(etEditBiayaAdmin.text.isEmpty()){
                etEditBiayaAdmin.error = "Tidak Boleh Kosong"
//                etEditBiayaAdmin.setText("0")
                isEnable = false
            }

            if(isEnable){
                val pembayaranModel = PembayaranModel(
                    "",
                    idUser,
                    "",
                    "",
                    etEditBiaya.text.toString(),
                    etEditDenda.text.toString(),
                    etEditBiayaAdmin.text.toString(),
                    etEditTenggatWaktu.text.toString(),
                    ""
                )
                loading.alertDialogLoading()
                postTambahPembayaran(pembayaranModel)
                dialog.dismiss()
            }

//            loading.alertDialogLoading()
//            dialog.dismiss()
        }
        btnBatal.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun postTambahPembayaran(pembayaranModel: PembayaranModel) {
        viewModel.postTambahPembayaran(pembayaranModel)
    }

    private fun getTambahPembayaran(){
        viewModel.getTambahPembayaran().observe(this@AdminDetailPembayaranActivity){result->
            when(result){
                is UIState.Success-> setTambahDataSuccess(result.data)
                is UIState.Failure-> setTambahDataFailure(result.message)
            }
        }
    }

    private fun setTambahDataSuccess(data: ArrayList<ResponseModel>) {
        if (data.isNotEmpty()){
            if(data[0].status == "0"){
                Toast.makeText(this@AdminDetailPembayaranActivity, "Berhasil", Toast.LENGTH_SHORT).show()
            }
            else if (data[0].status == "2"){
                Toast.makeText(this@AdminDetailPembayaranActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this@AdminDetailPembayaranActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(this@AdminDetailPembayaranActivity, "Ada yang salah", Toast.LENGTH_SHORT).show()
        }
        fetchData(idUser)
    }

    private fun setTambahDataFailure(message: String) {
        Toast.makeText(this@AdminDetailPembayaranActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun fetchData(idUser: String) {
        viewModel.fetchDataPembayaran(idUser)
        viewModel.fetchDataBiaya()
    }

    private fun getData() {
        viewModel.getData().observe(this@AdminDetailPembayaranActivity){result->
            when(result){
                is UIState.Success->{
                    if(result.data.isNotEmpty()){
                        setDataSuccess(result.data)
                    } else{
                        setNoDataSuccess()
                    }
                }
                is UIState.Failure->{
                    setDataFailure(result.message)
                }
            }
            loading.alertDialogCancel()
        }

        viewModel.getDataBiaya().observe(this@AdminDetailPembayaranActivity){result->
            when(result){
                is UIState.Success-> setDataSuccessBiaya(result.data)
                is UIState.Failure-> setDataFailureBiaya(result.message)
            }
        }
    }

    private fun setDataSuccessBiaya(data: ArrayList<BiayaModel>) {
        biaya = data[0].biaya!!
//        denda = data[0].denda!!
        biayaAdmin = data[0].biaya_admin!!
    }

    private fun setDataFailureBiaya(message: String) {

    }

    private fun setDataSuccess(pembayaran: ArrayList<PembayaranModel>) {
        val data = arrayListOf<PembayaranModel>()
        for (value in pembayaran){
            if(value.waktuPembayaran!!.isEmpty()){
                data.add(value)
            }
        }

        adapter = AdminDetailPembayaranAdapter(data, object : AdminDetailPembayaranAdapter.ButtonClickListener{
            override fun buttonClick(idPembayaran:String, tenggatWaktu:String, it: View) {
                val viewAlertDialog = View.inflate(this@AdminDetailPembayaranActivity, R.layout.alert_dialog_konfirmasi, null)
                val tvTitleKonfirmasi = viewAlertDialog.findViewById<TextView>(R.id.tvTitleKonfirmasi)
                val tvBodyKonfirmasi = viewAlertDialog.findViewById<TextView>(R.id.tvBodyKonfirmasi)
                val btnKonfirmasi = viewAlertDialog.findViewById<Button>(R.id.btnKonfirmasi)
                val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)
                tvTitleKonfirmasi.text = "Konfirmasi Pembayaran"
                tvBodyKonfirmasi.text = tenggatWaktu

                val alertDialog = AlertDialog.Builder(this@AdminDetailPembayaranActivity)
                alertDialog.setView(viewAlertDialog)
                val dialog = alertDialog.create()
                dialog.show()

                btnKonfirmasi.setOnClickListener {
                    dialog.dismiss()
                    loading.alertDialogLoading()
                    postKonfirmasiPembayaran(idPembayaran)
                }
                btnBatal.setOnClickListener {
                    dialog.dismiss()
                }
            }
        })

        binding.apply {
            tvInformasi.visibility = View.GONE

            rvDetailPembayaran.layoutManager = LinearLayoutManager(this@AdminDetailPembayaranActivity, LinearLayoutManager.VERTICAL, false)
            rvDetailPembayaran.adapter = adapter
        }
    }

    private fun setNoDataSuccess() {
        binding.apply {
            tvInformasi.visibility = View.VISIBLE

            btnKonfirmasiSemuaPembayaran.visibility = View.GONE
            rvDetailPembayaran.visibility = View.GONE
        }
    }

    private fun setDataFailure(message: String) {
        Toast.makeText(this@AdminDetailPembayaranActivity, message, Toast.LENGTH_SHORT).show()
        binding.apply {
            tvInformasi.text = "Ada Kesalahan \n$message"
            tvInformasi.visibility = View.VISIBLE

            btnKonfirmasiSemuaPembayaran.visibility = View.GONE
            rvDetailPembayaran.visibility = View.GONE
        }
    }

    private fun postKonfirmasiPembayaran(idPembayaran: String) {
        viewModel.postKonfirmasiDatapembayaran(idPembayaran)
    }

    private fun getKonfirmasiPembayaran(){
        viewModel.getKonfirmasiDataPembayaran().observe(this@AdminDetailPembayaranActivity){result->
            when(result){
                is UIState.Success->{
                    setKonfirmasiDataSuccess(result.data)
                }
                is UIState.Failure->{
                    setKonfirmasiDataFailure(result.message)
                }
            }
        }
    }

    private fun setKonfirmasiDataSuccess(data: ArrayList<ResponseModel>) {
        if(data[0].status=="0"){
            Toast.makeText(this@AdminDetailPembayaranActivity, "Berhasil Konfirmasi", Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(this@AdminDetailPembayaranActivity, "Gagal Konfirmasi", Toast.LENGTH_SHORT).show()
        }
        fetchData(idUser)
    }

    private fun setKonfirmasiDataFailure(message: String) {
        Toast.makeText(this@AdminDetailPembayaranActivity, message, Toast.LENGTH_SHORT).show()
        fetchData(idUser)
    }

    fun selectedDate(tanggal:String, tv: TextView){
        var arrayTanggalSekarang = tanggal.split("-")

        val c = Calendar.getInstance()
        val year = arrayTanggalSekarang[0].toInt()
        val month = arrayTanggalSekarang[1].toInt()-1   // Kurang 1, diambil dari array
        val day = arrayTanggalSekarang[2].toInt()


        val mDatePicker = DatePickerDialog(this@AdminDetailPembayaranActivity, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            var tahun = year.toString()
            var bulan = (monthOfYear+1).toString()
            var tanggal = dayOfMonth.toString()
            if(bulan.length==1){
                bulan = "0$bulan"
            }
            if(tanggal.length==1){
                tanggal = "0$tanggal"
            }

            val tanggalFull = "$tahun-$bulan-$tanggal"
            tv.text = tanggalFull

        }, year, month, day)
        mDatePicker.setTitle("Pilih Tanggal")
        mDatePicker.show()

    }

    fun selectedTime(waktu:String, tv: TextView){
        var valueWaktu = ""
        var arrayWaktu = waktu.split(":")
//        val hour = 12
//        val minute = 0
        val hour = arrayWaktu[0].toInt()
        val minute = arrayWaktu[1].toInt()
        val mTimePicker: TimePickerDialog = TimePickerDialog(this@AdminDetailPembayaranActivity,
            { timePicker, selectedHour, selectedMinute ->
                var menit = selectedMinute.toString()
                var jam = selectedHour.toString()
                if(jam.length==1){
                    jam = "0$selectedHour"
                }
                if(menit.length==1){
                    menit = "0$selectedMinute"
                }
                valueWaktu = "$jam:$menit:00"

                tv.text = valueWaktu

            },
            hour,
            minute,
            true
        )
        mTimePicker.setTitle("Pilih Waktu")
        mTimePicker.show()
    }
}