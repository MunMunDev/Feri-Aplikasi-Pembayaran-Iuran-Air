package com.munmundev.feri_aplikasipembayaraniuranair.adapter.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.munmundev.feri_aplikasipembayaraniuranair.R
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.PerumahanModel

class AdminListPerumahanAdapter(private val listUser:ArrayList<PerumahanModel>, val click: onClick):
    RecyclerView.Adapter<AdminListPerumahanAdapter.AdminListUserPerumahanViewHolder>() {
    class AdminListUserPerumahanViewHolder(v: View): RecyclerView.ViewHolder(v) {
        val tvNamaPerumahan: TextView
        val clBody: ConstraintLayout
        init {
            tvNamaPerumahan = v.findViewById(R.id.tvNamaPerumahan)
            clBody = v.findViewById(R.id.clBody)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdminListUserPerumahanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_admin_perumahan, parent, false)
        return AdminListUserPerumahanViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    override fun onBindViewHolder(holder: AdminListUserPerumahanViewHolder, position: Int) {
        val list = listUser[position]

        holder.apply {
            if(!list.alamat.isNullOrEmpty()){
                tvNamaPerumahan.text = "No. ${list.alamat}"
            }
            else if(!list.blok_perumahan.isNullOrEmpty()){
                tvNamaPerumahan.text = list.blok_perumahan
            }else{
                tvNamaPerumahan.text = list.nama_perumahan
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