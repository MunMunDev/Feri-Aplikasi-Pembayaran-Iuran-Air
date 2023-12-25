package com.munmundev.feri_aplikasipembayaraniuranair.adapter.admin

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.munmundev.feri_aplikasipembayaraniuranair.R
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.UsersModel

class AdminUsersAdapter(var arrayUser: ArrayList<UsersModel>, val listener: ClickItemListener): RecyclerView.Adapter<AdminUsersAdapter.AdminUsersViewHolder>() {
    class AdminUsersViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val clBody: ConstraintLayout
        val tvNama : TextView
        val tvAlamat : TextView
        init {
            clBody = v.findViewById(R.id.clBody)
            tvNama = v.findViewById(R.id.tvNama)
            tvAlamat = v.findViewById(R.id.tvAlamat)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminUsersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_admin_users, parent, false)
        return AdminUsersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayUser.size
    }

    override fun onBindViewHolder(holder: AdminUsersViewHolder, position: Int) {
        val data = arrayUser[position]
        holder.apply {
            tvNama.text = data.nama
            tvAlamat.text = data.alamat

            clBody.setOnClickListener {
                listener.onClickItem(data, it)
            }
        }
    }

    interface ClickItemListener{
        fun onClickItem(user: UsersModel, it:View)
    }
}