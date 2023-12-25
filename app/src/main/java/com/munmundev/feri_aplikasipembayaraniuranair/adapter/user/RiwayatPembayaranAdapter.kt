package com.munmundev.feri_aplikasipembayaraniuranair.adapter.user

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.munmundev.feri_aplikasipembayaraniuranair.R
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.PembayaranModel
import com.munmundev.feri_aplikasipembayaraniuranair.utils.KonversiRupiah
import com.munmundev.feri_aplikasipembayaraniuranair.utils.TanggalDanWaktu

class RiwayatPembayaranAdapter(
    var list : ArrayList<PembayaranModel>,
    var listener: RiwayatPembayaranAdapter.OnClick
): RecyclerView.Adapter<RiwayatPembayaranAdapter.RiwayatPembayaranViewHolder>() {

    private val rupiah = KonversiRupiah()
    private val waktuDanBulan = TanggalDanWaktu()

    class RiwayatPembayaranViewHolder(v: View): RecyclerView.ViewHolder(v) {
        val clRiwayatPembayaran : ConstraintLayout
        val lsTvBulanTenggatWaktu : TextView
        val lsTvHargaRiwayatPesanan : TextView
        val lsTvTanggal : TextView
        val lsTvWaktu : TextView
        init {
            clRiwayatPembayaran = v.findViewById(R.id.clRiwayatPembayaran)
            lsTvBulanTenggatWaktu = v.findViewById(R.id.lsTvBulanTenggatWaktu)
            lsTvHargaRiwayatPesanan = v.findViewById(R.id.lsTvHargaRiwayatPesanan)
            lsTvTanggal = v.findViewById(R.id.lsTvTanggal)
            lsTvWaktu = v.findViewById(R.id.lsTvWaktu)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiwayatPembayaranViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_riwayat_pembayaran, parent, false)
        return RiwayatPembayaranViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RiwayatPembayaranViewHolder, position: Int) {
        val data = list[position]
        val harga = data.harga!!.toInt()
        val biayaAdmin = data.biayaAdmin!!.toInt()
        var denda = 0
        if(data.denda!!.isNotEmpty()){
            denda = data.denda!!.toInt()
        }
        val total = (harga+biayaAdmin+denda).toLong()
        val arrayTenggatWaktu = (waktuDanBulan.konversiBulan(data.tenggatWaktu!!)).split(" ")
        val valueTenggatWaktu = "${arrayTenggatWaktu[1]} ${arrayTenggatWaktu[2]}"
        val arrayWaktuPembayaran = data.waktuPembayaran!!.split(" ")
        val bulanPembayaran = waktuDanBulan.konversiBulan(arrayWaktuPembayaran[0])
        val waktuPembayaran = waktuDanBulan.waktuNoSecond(arrayWaktuPembayaran[1])

        holder.apply {
            lsTvBulanTenggatWaktu.text = valueTenggatWaktu
            lsTvHargaRiwayatPesanan.text = "Harga Total : ${rupiah.rupiah(total)}"
            lsTvTanggal.text = bulanPembayaran
            lsTvWaktu.text = waktuPembayaran

            clRiwayatPembayaran.setOnClickListener {
                listener.clickItem(data, it)
            }
        }
    }

    interface OnClick{
        fun clickItem(pembayaran: PembayaranModel, it: View)
    }
}