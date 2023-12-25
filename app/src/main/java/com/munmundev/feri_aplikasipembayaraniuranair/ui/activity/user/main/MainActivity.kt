package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.user.main

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.example.muharya_pengingatjadwalkeretaapi.utils.SharedPreferencesLogin
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.PembayaranModel
import com.munmundev.feri_aplikasipembayaraniuranair.databinding.ActivityMainBinding
import com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.user.akun.AkunActivity
import com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.user.payment.PaymentActivity
import com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.user.riwayat_pembayaran.RiwayatPembayaranActivity
import com.munmundev.feri_aplikasipembayaraniuranair.utils.KontrolNavigationDrawer
import com.munmundev.feri_aplikasipembayaraniuranair.utils.KonversiRupiah
import com.munmundev.feri_aplikasipembayaraniuranair.utils.LoadingAlertDialog
import com.munmundev.feri_aplikasipembayaraniuranair.utils.TanggalDanWaktu
import com.munmundev.feri_aplikasipembayaraniuranair.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private lateinit var sharedPref : SharedPreferencesLogin
    private lateinit var loading: LoadingAlertDialog

    val viewModel : MainViewModel by viewModels()
    @Inject lateinit var rupiah : KonversiRupiah
    @Inject lateinit var tanggalDanWaktu: TanggalDanWaktu
    private var totalBiaya = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        setLoading()
        setSharedPreferences()
        setNavigationDrawer()

        fetchData(sharedPref.getIdUser().toString())
        getData()
        setButton()
    }

    private fun setLoading() {
        loading = LoadingAlertDialog(this@MainActivity)
        loading.alertDialogLoading()
    }

    private fun setSharedPreferences() {
        sharedPref = SharedPreferencesLogin(this@MainActivity)
        mainBinding.tvNamaUser.text = sharedPref.getNama()
    }

    private fun setButton() {
        mainBinding.apply {
            btnRiwayatPembayaran.setOnClickListener{
                startActivity(Intent(this@MainActivity, RiwayatPembayaranActivity::class.java))
                finish()
            }
            btnAkun.setOnClickListener{
                startActivity(Intent(this@MainActivity, AkunActivity::class.java))
                finish()
            }
            tvKesalahan.setOnClickListener {
                startActivity(Intent(this@MainActivity, MainActivity::class.java))
                finish()
            }
            btnBayar.setOnClickListener {
                val i = Intent(this@MainActivity, PaymentActivity::class.java)
                i.putExtra("totalBiaya", totalBiaya)
                startActivity(i)
                finish()
            }
        }
    }

    private fun setNavigationDrawer(){
        mainBinding.apply {
            kontrolNavigationDrawer = KontrolNavigationDrawer(this@MainActivity)
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@MainActivity)
        }
    }

    fun fetchData(idUser: String){
        viewModel.fetchData(idUser)
    }

    private fun getData() {
        viewModel.getData().observe(this@MainActivity){ result ->
            when(result){
                is UIState.Success ->{
                    Log.d("MainActivityTag", "setData: ${result.data}")
                    if(result.data.isNotEmpty()){
                        setData(result.data)
                    } else{
                        setNoHaveData()
                    }
                }
                is UIState.Failure ->{
                    Toast.makeText(this@MainActivity, result.message, Toast.LENGTH_SHORT).show()
                    setKesalahan()
                    loading.alertDialogCancel()
                }
            }
        }
    }

    private fun setKesalahan() {
        mainBinding.apply {
            tvKesalahan.visibility = View.VISIBLE
            tvSudahBayar.visibility = View.GONE
            isiPembayaranSekarang.visibility = View.GONE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setData(data: ArrayList<PembayaranModel>) {
        var namaJumlahBulan = ""
        var jumlahBulan = 0
        var harga = 0
        var denda = 0
        var biayaAdmin = 0

        Log.d("MainActivityTag", "setData: $data")

        for (value in data){
            jumlahBulan++
            harga += value.harga!!.trim().toInt()
            if(!value.denda!!.isEmpty()){
                denda += value.denda!!.trim().toInt()
            }
            biayaAdmin += value.biayaAdmin!!.trim().toInt()

            val bulan = tanggalDanWaktu.konversiBulan(value.tenggatWaktu!!)
            val arrayWaktu = bulan.split(" ")
            namaJumlahBulan+= "${arrayWaktu[1]} ${arrayWaktu[2]}, "

        }

        val total = harga+denda+biayaAdmin
        totalBiaya = total.toDouble()

        mainBinding.apply {
            tvBulan.text = "$jumlahBulan Bulan"
            tvHarga.text = rupiah.rupiah(harga.toLong())
            tvDenda.text = rupiah.rupiah(denda.toLong())
            tvBiayaAdmin.text = rupiah.rupiah(biayaAdmin.toLong())
            tvTotalTagihan.text = rupiah.rupiah(total.toLong())
            tvBulanTagihan.text = namaJumlahBulan

            tvKesalahan.visibility = View.GONE
            tvSudahBayar.visibility = View.GONE
            isiPembayaranSekarang.visibility = View.VISIBLE
        }

        loading.alertDialogCancel()
    }

    private fun setNoHaveData() {
        mainBinding.apply {
            tvSudahBayar.visibility = View.VISIBLE
            tvKesalahan.visibility = View.GONE
            isiPembayaranSekarang.visibility = View.GONE
        }
        loading.alertDialogCancel()
    }

    private var tapDuaKali = false
    override fun onBackPressed() {
        if (tapDuaKali){
            super.onBackPressed()
        }
        tapDuaKali = true
        Toast.makeText(this@MainActivity, "Tekan Sekali Lagi untuk keluar", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({
            tapDuaKali = false
        }, 2000)
    }

}