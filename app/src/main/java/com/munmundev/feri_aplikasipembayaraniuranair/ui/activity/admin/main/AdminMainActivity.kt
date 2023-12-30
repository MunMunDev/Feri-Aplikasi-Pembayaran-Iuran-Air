package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.admin.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.munmundev.feri_aplikasipembayaraniuranair.databinding.ActivityAdminMainBinding
import com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.admin.account.AdminUsersActivity
import com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.admin.biaya.AdminBiayaActivity
import com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.admin.pembayaran.list_perumahan.AdminPembayaranActivity
import com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.admin.perumahan.list_perumahan.AdminPerumahanActivity
import com.munmundev.feri_aplikasipembayaraniuranair.utils.KontrolNavigationDrawer

class AdminMainActivity : Activity() {
    private lateinit var binding: ActivityAdminMainBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminMainActivity)

        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminMainActivity)
        }
        setButton()
    }

    private fun setButton() {
        binding.apply {
            btnHalamanPerumahan.setOnClickListener {
                startActivity(Intent(this@AdminMainActivity, AdminPerumahanActivity::class.java))
                finish()
            }
            btnHalamanBiaya.setOnClickListener {
                startActivity(Intent(this@AdminMainActivity, AdminBiayaActivity::class.java))
                finish()
            }
            btnHalamanPembayaran.setOnClickListener {
                startActivity(Intent(this@AdminMainActivity, AdminPembayaranActivity::class.java))
                finish()
            }
            btnHalamanAkun.setOnClickListener {
                startActivity(Intent(this@AdminMainActivity, AdminUsersActivity::class.java))
                finish()
            }
        }
    }

    private var tapDuaKali = false
    override fun onBackPressed() {
        if (tapDuaKali){
            super.onBackPressed()
        }
        tapDuaKali = true
        Toast.makeText(this@AdminMainActivity, "Tekan Sekali Lagi untuk keluar", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({
            tapDuaKali = false
        }, 2000)
    }
}