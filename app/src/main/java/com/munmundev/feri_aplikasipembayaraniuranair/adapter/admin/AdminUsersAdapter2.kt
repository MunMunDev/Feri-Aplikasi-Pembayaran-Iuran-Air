package com.munmundev.feri_aplikasipembayaraniuranair.adapter.admin

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.munmundev.feri_aplikasipembayaraniuranair.R
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.UsersModel

class AdminUsersAdapter2(var arrayUser: ArrayList<UsersModel>, val listener: AdminUsersAdapter2.ClickItemListener): RecyclerView.Adapter<AdminUsersAdapter2.AdminUsersViewHolder>() {
    class AdminUsersViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvNo : TextView
        val tvNama : TextView
        val tvAlamat : TextView
        val tvNomorHp : TextView
        val tvUsername : TextView
        val tvPassword : TextView
        val tvSetting : TextView
        init {
            tvNo = v.findViewById(R.id.tvNo)
            tvNama = v.findViewById(R.id.tvNama)
            tvAlamat = v.findViewById(R.id.tvAlamat)
            tvNomorHp = v.findViewById(R.id.tvNomorHp)
            tvUsername = v.findViewById(R.id.tvUsername)
            tvPassword = v.findViewById(R.id.tvPassword)
            tvSetting = v.findViewById(R.id.tvSetting)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminUsersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_admin_user_table, parent, false)
        return AdminUsersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayUser.size+1
    }

    override fun onBindViewHolder(holder: AdminUsersViewHolder, position: Int) {
        holder.apply {
            if(position==0){
                tvNo.text = "NO"
                tvNama.text = "Nama User"
                tvAlamat.text = "Alamat"
                tvNomorHp.text = "Nomor HP"
                tvUsername.text = "Username"
                tvPassword.text = "Password"
                tvSetting.text = ""

                tvNo.setBackgroundResource(R.drawable.bg_table_title)
                tvNama.setBackgroundResource(R.drawable.bg_table_title)
                tvAlamat.setBackgroundResource(R.drawable.bg_table_title)
                tvNomorHp.setBackgroundResource(R.drawable.bg_table_title)
                tvUsername.setBackgroundResource(R.drawable.bg_table_title)
                tvPassword.setBackgroundResource(R.drawable.bg_table_title)
                tvSetting.setBackgroundResource(R.drawable.bg_table_title)

                tvNo.setTextColor(Color.parseColor("#ffffff"))
                tvNama.setTextColor(Color.parseColor("#ffffff"))
                tvAlamat.setTextColor(Color.parseColor("#ffffff"))
                tvNomorHp.setTextColor(Color.parseColor("#ffffff"))
                tvUsername.setTextColor(Color.parseColor("#ffffff"))
                tvPassword.setTextColor(Color.parseColor("#ffffff"))
                tvSetting.setTextColor(Color.parseColor("#ffffff"))

                tvNo.setTypeface(null, Typeface.BOLD)
                tvNama.setTypeface(null, Typeface.BOLD)
                tvAlamat.setTypeface(null, Typeface.BOLD)
                tvNomorHp.setTypeface(null, Typeface.BOLD)
                tvUsername.setTypeface(null, Typeface.BOLD)
                tvPassword.setTypeface(null, Typeface.BOLD)
                tvSetting.setTypeface(null, Typeface.BOLD)
            }
            else{
                val user = arrayUser[(position-1)]

                tvNo.text = "$position"
                tvNama.text = user.nama
                tvAlamat.text = user.alamat
                tvNomorHp.text = user.nomorHp
                tvUsername.text = user.username
                var password = ""
                user.password?.length?.let {
                    for(value in 0..it){
                        password+="*"
                    }
                }

//                tvPassword.text = user.password
                tvPassword.text = password
                tvSetting.text = ":::"

                tvNo.setBackgroundResource(R.drawable.bg_table)
                tvNama.setBackgroundResource(R.drawable.bg_table)
                tvAlamat.setBackgroundResource(R.drawable.bg_table)
                tvNomorHp.setBackgroundResource(R.drawable.bg_table)
                tvUsername.setBackgroundResource(R.drawable.bg_table)
                tvPassword.setBackgroundResource(R.drawable.bg_table)
                tvSetting.setBackgroundResource(R.drawable.bg_table)

                tvNo.setTextColor(Color.parseColor("#000000"))
                tvNama.setTextColor(Color.parseColor("#000000"))
                tvAlamat.setTextColor(Color.parseColor("#000000"))
                tvNomorHp.setTextColor(Color.parseColor("#000000"))
                tvUsername.setTextColor(Color.parseColor("#000000"))
                tvPassword.setTextColor(Color.parseColor("#000000"))
                tvSetting.setTextColor(Color.parseColor("#000000"))

                tvNo.setTypeface(null, Typeface.NORMAL)
                tvNama.setTypeface(null, Typeface.NORMAL)
                tvAlamat.setTypeface(null, Typeface.NORMAL)
                tvNomorHp.setTypeface(null, Typeface.NORMAL)
                tvUsername.setTypeface(null, Typeface.NORMAL)
                tvPassword.setTypeface(null, Typeface.NORMAL)
                tvSetting.setTypeface(null, Typeface.NORMAL)

                tvSetting.setOnClickListener {
                    listener.onClickSetting(user, position, it)
                }
            }
        }
    }

    interface ClickItemListener{
        fun onClickSetting(user: UsersModel, position: Int, it:View)
    }
}