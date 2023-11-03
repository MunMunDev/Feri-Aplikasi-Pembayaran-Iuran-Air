package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.muharya_pengingatjadwalkeretaapi.utils.SharedPreferencesLogin
import com.munmundev.feri_aplikasipembayaraniuranair.databinding.ActivityMainBinding
import com.munmundev.feri_aplikasipembayaraniuranair.utils.KontrolNavigationDrawer
import com.munmundev.feri_aplikasipembayaraniuranair.utils.LoadingAlertDialog

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private lateinit var sharedPref : SharedPreferencesLogin
    private lateinit var loading: LoadingAlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        kontrolNavigationDrawer = KontrolNavigationDrawer(this@MainActivity)
        sharedPref = SharedPreferencesLogin(this@MainActivity)
        loading = LoadingAlertDialog(this@MainActivity)

        mainBinding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@MainActivity)


        }

    }
}