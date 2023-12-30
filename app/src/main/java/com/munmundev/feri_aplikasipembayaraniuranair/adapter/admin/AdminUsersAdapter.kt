package com.munmundev.feri_aplikasipembayaraniuranair.adapter.admin

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.munmundev.feri_aplikasipembayaraniuranair.R
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.UsersModel
import com.munmundev.feri_aplikasipembayaraniuranair.databinding.ListAdminUsersBinding

class AdminUsersAdapter(var arrayUser: ArrayList<UsersModel>, val listener: ClickItemListener): RecyclerView.Adapter<AdminUsersAdapter.AdminUsersViewHolder>() {

    private var listUser = arrayUser
    private var tempListUser = arrayListOf<UsersModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun setFilterData(id:String){
        if(id != "0"){
            for((no, value) in listUser.withIndex()){
                if(value.perumahan!![0].id_perumahan == id){
                    tempListUser.add(value)
                }
            }
            arrayUser = tempListUser
            tempListUser = arrayListOf()
        } else{
            arrayUser = listUser
        }
        notifyDataSetChanged()
    }

    class AdminUsersViewHolder(val binding: ListAdminUsersBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminUsersViewHolder {
        val binding = ListAdminUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdminUsersViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if(arrayUser.size > 0){
            arrayUser.size
        } else{
            0
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AdminUsersViewHolder, position: Int) {
        val data = arrayUser[position]
        holder.binding.apply {
            tvNama.text = data.nama
            val perumahan = data.perumahan!![0].nama_perumahan
            val blokPerumahan = data.perumahan!![0].blok_perumahan
            val noAlamat = data.perumahan!![0].alamat
            tvAlamat.text = "$perumahan, $blokPerumahan, $noAlamat"

            data.perumahan!![0] = data.perumahan!![0]

            clBody.setOnClickListener {
                listener.onClickItem(data, it, position)
            }
        }
    }

    interface ClickItemListener{
        fun onClickItem(user: UsersModel, it:View, position: Int)
    }
}