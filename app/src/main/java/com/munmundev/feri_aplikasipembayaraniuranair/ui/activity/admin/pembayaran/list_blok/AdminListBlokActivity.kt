package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.admin.pembayaran.list_blok

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.munmundev.feri_aplikasipembayaraniuranair.R
import com.munmundev.feri_aplikasipembayaraniuranair.adapter.admin.AdminListPerumahanAdapter
import com.munmundev.feri_aplikasipembayaraniuranair.adapter.admin.AdminUsersAdapter
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.PembayaranModel
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.PerumahanModel
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.UsersModel
import com.munmundev.feri_aplikasipembayaraniuranair.databinding.ActivityAdminListBlokBinding
import com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.admin.pembayaran.riwayat.AdminRiwayatPembayaranActivity
import com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.admin.pembayaran.detail.AdminDetailPembayaranActivity
import com.munmundev.feri_aplikasipembayaraniuranair.utils.LoadingAlertDialog
import com.munmundev.feri_aplikasipembayaraniuranair.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminListBlokActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminListBlokBinding
    private lateinit var loading : LoadingAlertDialog
    private lateinit var listPerumahan : ArrayList<PerumahanModel>
    private lateinit var listUser : ArrayList<UsersModel>
    private lateinit var listPembayaran : ArrayList<PembayaranModel>
    private lateinit var adapter: AdminListPerumahanAdapter
    private lateinit var adapterUser: AdminUsersAdapter
    private var namaPerumahan = ""
    private var idPerumahan = ""
    private var idBlok = ""
    private var namaUser = ""
    private var idUser = ""
    private val viewModel: ListBlokViewModel by viewModels()

    private var TAG = "AdminPembayaranListBlokActivityTAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminListBlokBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setLoadingSetting()
        setDataIntentMoveActivity()
        button()
        getDataBlok()
        getDataUser()

    }

    private fun setLoadingSetting() {
        loading = LoadingAlertDialog(this@AdminListBlokActivity)
        loading.alertDialogLoading()
    }

    private fun setDataIntentMoveActivity() {
        val extras = intent.extras
        if(extras != null) {
            if (extras.getString("id_perumahan") != null) {
                namaPerumahan = extras.getString("nama_perumahan").toString()
                idPerumahan = extras.getString("id_perumahan").toString()
                fetchDataBlok(idPerumahan)
//                getData()

            } else if (extras.getString("id_blok") != null) {
                idBlok = extras.getString("id_blok").toString()
                val blokPerumahan = extras.getString("blok_perumahan").toString()
                binding.titleHeader.text = "Blok $blokPerumahan"
                fetchDataUser(idBlok)
//                getDataBlok(idBlok)
            }

        }
    }

    private fun button() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun fetchDataUser(idBlok: String) {
        viewModel.fetchDataUser(idBlok)
    }

    private fun fetchDataBlok(idPerumahan: String) {
        viewModel.fetchDataBlok(idPerumahan)
    }

    private fun getDataBlok() {
        viewModel.getDataBlok().observe(this@AdminListBlokActivity){result->
            when(result){
                is UIState.Success->{
                    setDataSuccess(result.data)
                }
                is UIState.Failure->{
                    setDataFailure(result.message)
                }
            }
        }
    }

    private fun setDataSuccess(data: ArrayList<PerumahanModel>) {
        binding.titleHeader.text = namaPerumahan
        binding.rvBlok.visibility = View.VISIBLE

        adapter = AdminListPerumahanAdapter(data, object : AdminListPerumahanAdapter.onClick{
            override fun ClickItem(perumahanModel: PerumahanModel, it:View) {
                val intent = Intent(this@AdminListBlokActivity, AdminListBlokActivity::class.java)
                intent.putExtra("id_blok", perumahanModel.id_blok)
                intent.putExtra("blok_perumahan", perumahanModel.blok_perumahan)
                startActivity(intent)
            }
        })

        binding.rvBlok.layoutManager = GridLayoutManager(this@AdminListBlokActivity, 2)
        binding.rvBlok.adapter = adapter

        loading.alertDialogCancel()
    }

    private fun setDataFailure(message: String) {
        Toast.makeText(this@AdminListBlokActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun getDataUser() {
        viewModel.getDataUser().observe(this@AdminListBlokActivity){result->
            when(result){
                is UIState.Success->{
                    setDataUserSuccess(result.data)
                }
                is UIState.Failure->{
                    setDataUserFailure(result.message)
                }
            }
        }
    }

    private fun setDataUserSuccess(data: ArrayList<UsersModel>) {
        if(data.isNotEmpty()){
            adapterUser = AdminUsersAdapter(data, object : AdminUsersAdapter.ClickItemListener{
                override fun onClickItem(user: UsersModel, it: View) {
                    val popupMenu = PopupMenu(this@AdminListBlokActivity, it)
                    popupMenu.inflate(R.menu.popup_menu_admin_pembayaran)
                    popupMenu.setOnMenuItemClickListener(object :
                        PopupMenu.OnMenuItemClickListener {
                        override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                            when (menuItem!!.itemId) {
                                R.id.pembayaranSekarang -> {
                                    var i = Intent(this@AdminListBlokActivity, AdminDetailPembayaranActivity::class.java)
                                    i.putExtra("idUser", user.idUser)
                                    startActivity(i)
                                    return true
                                }

                                R.id.riwayatPembayaran -> {
                                    var i = Intent(this@AdminListBlokActivity, AdminRiwayatPembayaranActivity::class.java)
                                    i.putExtra("idUser", user.idUser)
                                    startActivity(i)
                                    return true
                                }
                            }
                            return true
                        }

                    })
                    popupMenu.show()
                }
            })

            binding.rvBlok.layoutManager = GridLayoutManager(this@AdminListBlokActivity, 2)
            binding.rvBlok.adapter = adapterUser

        }else{
            Toast.makeText(this@AdminListBlokActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
        }

        loading.alertDialogCancel()
    }

    private fun setDataUserFailure(message: String) {
        loading.alertDialogCancel()
        Toast.makeText(this@AdminListBlokActivity, message, Toast.LENGTH_SHORT).show()
    }

    fun getData(){
//        ApiConfig2.getRetrofit().getBlokPerumahan("", idPerumahan)
//            .enqueue(object : Callback<ArrayList<PerumahanModel>> {
//                override fun onResponse(
//                    call: Call<ArrayList<PerumahanModel>>,
//                    response: Response<ArrayList<PerumahanModel>>
//                ) {
//                    listPerumahan = arrayListOf()
//                    if(!response.body().isNullOrEmpty()){
//                        listPerumahan = response.body()!!
//
//                        binding.titleHeader.text = namaPerumahan
//                        binding.rvBlok.visibility = View.VISIBLE
//
//                        adapter = AdminListPerumahanAdapter(listPerumahan, object : AdminListPerumahanAdapter.onClick{
//                            override fun ClickItem(perumahanModel: PerumahanModel) {
//                                Toast.makeText(this@AdminListBlokActivity, "Click perumahan", Toast.LENGTH_SHORT).show()
//                                val intent = Intent(this@AdminListBlokActivity, AdminListBlokActivity::class.java)
//                                intent.putExtra("id_blok", perumahanModel.id_blok)
//                                intent.putExtra("blok_perumahan", perumahanModel.blok_perumahan)
//                                startActivity(intent)
//                            }
//                        })
//
//                        binding.rvBlok.layoutManager = GridLayoutManager(this@AdminListBlokActivity, 2)
//                        binding.rvBlok.adapter = adapter
//                    }else{
//                        Toast.makeText(this@AdminListBlokActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
//                    }
//
//                    loading.alertDialogCancel()
//                }
//
//                override fun onFailure(call: Call<ArrayList<PerumahanModel>>, t: Throwable) {
//                    Toast.makeText(this@AdminListBlokActivity, "failure ${t.message}", Toast.LENGTH_SHORT).show()
//
//                    Toast.makeText(this@AdminListBlokActivity, "Gagal", Toast.LENGTH_SHORT).show()
//                    loading.alertDialogCancel()
//                }
//
//            })
    }

    private fun getDataBlok(idBlok: String) {
//        ApiConfig2.getRetrofit().getAdminListUserPembayaran("", idBlok)
//            .enqueue(object : Callback<ArrayList<UsersModel>> {
//                override fun onResponse(
//                    call: Call<ArrayList<UsersModel>>,
//                    response: Response<ArrayList<UsersModel>>
//                ) {
//                    listUser = arrayListOf()
//                    if(!response.body().isNullOrEmpty()){
//                        listUser = response.body()!!
//
//                        adapterUser = AdminUsersAdapter(listUser, object : AdminUsersAdapter.ClickItemListener{
//                            override fun onClickItem(user: UsersModel, it: View) {
//                                val popupMenu = PopupMenu(this@AdminListBlokActivity, it)
//                                popupMenu.inflate(R.menu.popup_menu_admin_pembayaran)
//                                popupMenu.setOnMenuItemClickListener(object :
//                                    PopupMenu.OnMenuItemClickListener {
//                                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
//                                        when (menuItem!!.itemId) {
//                                            R.id.pembayaranSekarang -> {
//                                                var i = Intent(this@AdminListBlokActivity, AdminDetailPembayaranActivity::class.java)
//                                                i.putExtra("idUser", user.idUser)
//                                                startActivity(i)
//                                                return true
//                                            }
//
//                                            R.id.riwayatPembayaran -> {
//                                                var i = Intent(this@AdminListBlokActivity, AdminRiwayatPembayaranActivity::class.java)
//                                                i.putExtra("idUser", user.idUser)
//                                                startActivity(i)
//                                                return true
//                                            }
//                                        }
//                                        return true
//                                    }
//
//                                })
//                                popupMenu.show()
//                            }
//                        })
//
//                        binding.rvBlok.layoutManager = GridLayoutManager(this@AdminListBlokActivity, 2)
//                        binding.rvBlok.adapter = adapterUser
//
//                    }else{
//                        Toast.makeText(this@AdminListBlokActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
//                    }
//
//                    loading.alertDialogCancel()
//                }
//
//                override fun onFailure(call: Call<ArrayList<UsersModel>>, t: Throwable) {
//                    Toast.makeText(this@AdminListBlokActivity, "failure ${t.message}", Toast.LENGTH_SHORT).show()
//                    Toast.makeText(this@AdminListBlokActivity, "Gagal", Toast.LENGTH_SHORT).show()
//                    loading.alertDialogCancel()
//                }
//
//            })
    }

    private fun dialogKonfirmasiPembayaran() {

    }

    private fun getDataRiwayatPembayaran(){

    }
}