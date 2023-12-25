package com.munmundev.feri_aplikasipembayaraniuranair.adapter.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.PerumahanModel
import com.munmundev.feri_aplikasipembayaraniuranair.databinding.ListPerumahanBinding

class AdminPerumahanAdapter(private var listPerumahan: ArrayList<PerumahanModel>, val klik: Klik): RecyclerView.Adapter<AdminPerumahanAdapter.ViewHolder>() {
    class ViewHolder(val binding: ListPerumahanBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListPerumahanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listPerumahan.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listPerumahan[position]
        holder.apply {
            binding.apply {
                tvNamaPerumahan.text = data.nama_perumahan
                tvJumlahBlok.text = "${data.jumlah_blok} Blok"
                ivSetting.setOnClickListener {
                    klik.klikSetting(data, it)
                }
            }
            itemView.setOnClickListener {
                klik.klikItem(data)
            }
        }
    }

    interface Klik{
        fun klikItem(listPerumahan: PerumahanModel)
        fun klikSetting(listPerumahan: PerumahanModel, it: View)
    }
}