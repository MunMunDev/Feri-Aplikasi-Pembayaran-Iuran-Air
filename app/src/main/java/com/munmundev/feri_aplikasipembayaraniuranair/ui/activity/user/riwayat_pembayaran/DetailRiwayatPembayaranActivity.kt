package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.user.riwayat_pembayaran

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.munmundev.feri_aplikasipembayaraniuranair.R
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.PembayaranModel
import com.munmundev.feri_aplikasipembayaraniuranair.databinding.ActivityDetailRiwayatPembayaranBinding
import com.munmundev.feri_aplikasipembayaraniuranair.utils.KonversiRupiah
import com.munmundev.feri_aplikasipembayaraniuranair.utils.TanggalDanWaktu
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailRiwayatPembayaranActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailRiwayatPembayaranBinding
    private lateinit var pembayaran : PembayaranModel
    @Inject lateinit var rupiah: KonversiRupiah
    @Inject lateinit var waktuDanBulan: TanggalDanWaktu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailRiwayatPembayaranBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getDataPembayaran()
        button()
    }

    private fun getDataPembayaran() {
        pembayaran = intent.getParcelableExtra<PembayaranModel>("pembayaran")!!
        val arrayWaktuPembayaran = pembayaran.waktuPembayaran!!.split(" ")
        val tanggalPembayaran = waktuDanBulan.konversiBulan(arrayWaktuPembayaran[0])
        val waktuPembayaran = "${waktuDanBulan.waktuNoSecond(arrayWaktuPembayaran[1])} WITA"
        val arrayBulanTenggatWaktu = (waktuDanBulan.konversiBulan(pembayaran.tenggatWaktu!!)).split(" ")
        val bulanTenggatWaktu = "${arrayBulanTenggatWaktu[1]} ${arrayBulanTenggatWaktu[2]}"
        val harga = pembayaran.harga!!.toInt()
        var denda = 0
        val biayaAdmin = pembayaran.biayaAdmin!!.toInt()
        if(pembayaran.denda != "" ){
            denda = pembayaran.denda!!.toInt()
        }
        val total = harga+denda+biayaAdmin
        binding.apply{
            tvTanggalPembayaran.text = "$tanggalPembayaran, $waktuPembayaran"
            tvTenggatBulan.text = bulanTenggatWaktu
            tvHarga.text = rupiah.rupiah(harga.toLong())
            tvDenda.text = rupiah.rupiah(denda.toLong())
            tvBiayaAdmin.text = rupiah.rupiah(biayaAdmin.toLong())
            tvTotalTagihan.text = rupiah.rupiah(total.toLong())
        }
    }

    private fun button() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

}