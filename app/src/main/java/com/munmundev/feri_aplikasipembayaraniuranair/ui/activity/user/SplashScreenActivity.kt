package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.muharya_pengingatjadwalkeretaapi.utils.SharedPreferencesLogin
import com.munmundev.feri_aplikasipembayaraniuranair.R
import com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.admin.AdminUsersActivity

class SplashScreenActivity : AppCompatActivity() {
    lateinit var sharedPreferencesLogin: SharedPreferencesLogin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.let {
            it.hide()
        }

        sharedPreferencesLogin = SharedPreferencesLogin(this@SplashScreenActivity)

        Handler(Looper.getMainLooper()).postDelayed({
            if(sharedPreferencesLogin.getId() == 0){
                startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
            }
            else{
                if(sharedPreferencesLogin.getSebagai() == "user"){
                    startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                    finish()
                }else if(sharedPreferencesLogin.getSebagai() == "admin"){
//                    startActivity(Intent(this@SplashScreenActivity, AdminMainActivity::class.java))
                    startActivity(Intent(this@SplashScreenActivity, AdminUsersActivity::class.java))
                    finish()
                }
            }
        }, 3000)
    }
}