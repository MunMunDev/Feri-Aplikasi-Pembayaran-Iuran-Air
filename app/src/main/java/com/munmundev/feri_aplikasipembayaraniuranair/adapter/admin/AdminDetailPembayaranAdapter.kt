package com.munmundev.feri_aplikasipembayaraniuranair.adapter.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.munmundev.feri_aplikasipembayaraniuranair.R
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.PembayaranModel
import com.munmundev.feri_aplikasipembayaraniuranair.utils.KonversiRupiah
import com.munmundev.feri_aplikasipembayaraniuranair.utils.TanggalDanWaktu
import javax.inject.Inject

class AdminDetailPembayaranAdapter(
    private var list: ArrayList<PembayaranModel>,
    private var listener: ButtonClickListener
): RecyclerView.Adapter<AdminDetailPembayaranAdapter.viewHolder>() {

    @Inject
    lateinit var tanggalDanWaktu: TanggalDanWaktu
    @Inject
    lateinit var rupiah: KonversiRupiah
    class viewHolder(v: View): RecyclerView.ViewHolder(v) {
        val tvTenggatBulan: TextView
        val tvHarga: TextView
        val tvDenda: TextView
        val tvBiayaAdmin: TextView
        val tvTotal: TextView
        val btnKonfirmasiPembayaran: Button
        init {
            tvTenggatBulan = v.findViewById(R.id.tvTenggatBulan)
            tvHarga = v.findViewById(R.id.tvHarga)
            tvDenda = v.findViewById(R.id.tvDenda)
            tvBiayaAdmin = v.findViewById(R.id.tvBiayaAdmin)
            tvTotal = v.findViewById(R.id.tvTotal)
            btnKonfirmasiPembayaran = v.findViewById(R.id.btnKonfirmasiPembayaran)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_detail_pembayaran, parent, false)
        return viewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (list.isNotEmpty()) list.size
        else 0
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val data = list[position]

        tanggalDanWaktu = TanggalDanWaktu()
        rupiah = KonversiRupiah()

        val arrayTanggal = tanggalDanWaktu.konversiBulan(data.tenggatWaktu!!).split(" ")
        val bulanTahun = "${arrayTanggal[1]} ${arrayTanggal[2]}"

        val harga = data.harga!!.toLong()
        val denda = data.denda!!.toLong()
        val biayaAdmin = data.biayaAdmin!!.toLong()
        val total = harga+denda+biayaAdmin

        with(holder){
            tvTenggatBulan.text = bulanTahun
            tvHarga.text = rupiah.rupiah(harga)
            tvDenda.text = rupiah.rupiah(denda)
            tvBiayaAdmin.text = rupiah.rupiah(biayaAdmin)
            tvTotal.text = rupiah.rupiah(total)

            btnKonfirmasiPembayaran.setOnClickListener {
                listener.buttonClick(data.idPembayaran!!, bulanTahun, it)
            }
        }
    }

    interface ButtonClickListener{
        fun buttonClick(idPembayaran:String, tenggatWaktu:String, it:View)
    }
}