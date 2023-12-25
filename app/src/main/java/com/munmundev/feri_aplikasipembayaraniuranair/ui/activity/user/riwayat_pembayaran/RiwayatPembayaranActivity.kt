package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.user.riwayat_pembayaran

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.muharya_pengingatjadwalkeretaapi.utils.SharedPreferencesLogin
import com.munmundev.feri_aplikasipembayaraniuranair.adapter.user.RiwayatPembayaranAdapter
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.PembayaranModel
import com.munmundev.feri_aplikasipembayaraniuranair.databinding.ActivityRiwayatPembayaranBinding
import com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.user.main.MainActivity
import com.munmundev.feri_aplikasipembayaraniuranair.utils.KontrolNavigationDrawer
import com.munmundev.feri_aplikasipembayaraniuranair.utils.LoadingAlertDialog
import com.munmundev.feri_aplikasipembayaraniuranair.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RiwayatPembayaranActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRiwayatPembayaranBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private lateinit var sharedPreferences: SharedPreferencesLogin
    private lateinit var loading: LoadingAlertDialog
    private val viewModel : RiwayatPembayaranViewModel by viewModels()
    private lateinit var adapter: RiwayatPembayaranAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiwayatPembayaranBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setLoading()
        setSharedPreferences()
        setNavigationDrawer()
        fetchData(sharedPreferences.getIdUser().toString())
        getData()
    }

    private fun setLoading() {
        loading = LoadingAlertDialog(this@RiwayatPembayaranActivity)
        loading.alertDialogLoading()
    }

    private fun setSharedPreferences() {
        sharedPreferences = SharedPreferencesLogin(this@RiwayatPembayaranActivity)
    }

    private fun setNavigationDrawer(){
        binding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@RiwayatPembayaranActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@RiwayatPembayaranActivity)
        }
    }

    fun fetchData(idUser: String){
        viewModel.fetchRiwayatPembayaran(idUser)
    }

    private fun getData() {
        viewModel.getRiwayatPembayaran().observe(this@RiwayatPembayaranActivity){ result->
            when(result){
                is UIState.Success -> setSuccessData(result.data)
                is UIState.Failure -> setFailureData(result.message)
            }
        }
    }

    private fun setFailureData(message: String) {
        Toast.makeText(this@RiwayatPembayaranActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setSuccessData(data: ArrayList<PembayaranModel>) {
        adapter = RiwayatPembayaranAdapter(data, object : RiwayatPembayaranAdapter.OnClick{
            override fun clickItem(pembayaran: PembayaranModel, it: View) {
                val i = Intent(this@RiwayatPembayaranActivity, DetailRiwayatPembayaranActivity::class.java)
                i.putExtra("pembayaran", pembayaran)
                startActivity(i)
            }
        })
        binding.rvRiwayatPembayaran.layoutManager = LinearLayoutManager(this@RiwayatPembayaranActivity, LinearLayoutManager.VERTICAL, false)
        binding.rvRiwayatPembayaran.adapter = adapter
        loading.alertDialogCancel()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@RiwayatPembayaranActivity, MainActivity::class.java))
        finish()
    }
}