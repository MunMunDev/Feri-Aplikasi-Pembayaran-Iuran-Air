package com.munmundev.feri_aplikasipembayaraniuranair.adapter.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.munmundev.feri_aplikasipembayaraniuranair.R
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.PerumahanModel
import com.munmundev.feri_aplikasipembayaraniuranair.databinding.ListAdminPerumahanBinding

class AdminListPerumahanAdapter(private val listUser:ArrayList<PerumahanModel>, val click: onClick):
    RecyclerView.Adapter<AdminListPerumahanAdapter.AdminListUserPerumahanViewHolder>() {
    class AdminListUserPerumahanViewHolder(val binding: ListAdminPerumahanBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdminListUserPerumahanViewHolder {
        val binding = ListAdminPerumahanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdminListUserPerumahanViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    override fun onBindViewHolder(holder: AdminListUserPerumahanViewHolder, position: Int) {
        val list = listUser[position]

        holder.apply {
            binding.apply {
                if(!list.alamat.isNullOrEmpty()){
                    tvNamaPerumahan.text = "No. ${list.alamat}"
                }
                else if(!list.blok_perumahan.isNullOrEmpty()){
                    tvNamaPerumahan.text = list.blok_perumahan
                }else{
                    tvNamaPerumahan.text = list.nama_perumahan
                }
            }

            itemView.setOnClickListener {
                click.ClickItem(list, it)
            }
        }

    }

    interface onClick{
        fun ClickItem(perumahanModel: PerumahanModel, it:View)
    }
}