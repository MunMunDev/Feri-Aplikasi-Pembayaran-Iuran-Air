package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.admin.main

import android.app.Activity
import android.os.Bundle
import com.munmundev.feri_aplikasipembayaraniuranair.databinding.ActivityAdminMainBinding
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
    }
}