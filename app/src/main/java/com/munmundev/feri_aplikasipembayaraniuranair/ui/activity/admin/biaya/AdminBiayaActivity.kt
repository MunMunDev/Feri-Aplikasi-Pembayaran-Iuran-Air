package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.admin.biaya

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.munmundev.feri_aplikasipembayaraniuranair.R
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.BiayaModel
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.ResponseModel
import com.munmundev.feri_aplikasipembayaraniuranair.databinding.ActivityAdminBiayaBinding
import com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.admin.main.AdminMainActivity
import com.munmundev.feri_aplikasipembayaraniuranair.utils.KontrolNavigationDrawer
import com.munmundev.feri_aplikasipembayaraniuranair.utils.KonversiRupiah
import com.munmundev.feri_aplikasipembayaraniuranair.utils.LoadingAlertDialog
import com.munmundev.feri_aplikasipembayaraniuranair.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AdminBiayaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBiayaBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private val viewModel: BiayaViewModel by viewModels()
    private lateinit var loading: LoadingAlertDialog
    @Inject lateinit var rupiah: KonversiRupiah
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBiayaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        setLoading()
        fetchDataBiaya()
        getDataBiaya()
        getUpdateBiaya()
    }

    private fun setKontrolNavigationDrawer() {
        kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminBiayaActivity)
        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminBiayaActivity)
        }
    }

    private fun setLoading() {
        loading = LoadingAlertDialog(this@AdminBiayaActivity)
        loading.alertDialogLoading()
    }

    private fun fetchDataBiaya() {
        viewModel.fetchDataBiaya()
    }

    private fun getDataBiaya() {
        viewModel.getDataBiaya().observe(this@AdminBiayaActivity){result->
            when(result){
                is UIState.Success-> setDataSuccessFetchBiaya(result.data)
                is UIState.Failure-> setDataFailureFetchBiaya(result.message)
            }
        }
    }

    private fun setDataFailureFetchBiaya(message: String) {
        Toast.makeText(this@AdminBiayaActivity, message, Toast.LENGTH_SHORT).show()
        setErrorBiaya()
        loading.alertDialogCancel()
    }

    private fun setDataSuccessFetchBiaya(data: ArrayList<BiayaModel>) {
        val valueData = data[0]
        if(data.isNotEmpty()){
            setHaveDataBiaya()
            val biaya = rupiah.rupiah(valueData.biaya!!.toLong())
            val denda = rupiah.rupiah(valueData.denda!!.toLong())
            val biayaAdmin = rupiah.rupiah(valueData.biaya_admin!!.toLong())

            binding.apply {
                tvBiaya.text = biaya
                tvDenda.text = denda
                tvBiayaAdmin.text = biayaAdmin
            }
            binding.btnUbahData.setOnClickListener {
                setButtonUpdate(valueData.biaya!!, valueData.denda!!, valueData.biaya_admin!!)
            }
        } else{
            Toast.makeText(this@AdminBiayaActivity, "Tidak Ada Data", Toast.LENGTH_SHORT).show()
            setErrorBiaya()
        }
        loading.alertDialogCancel()
    }

    private fun setHaveDataBiaya(){
        binding.clBiaya.visibility = View.VISIBLE
        binding.clUbah.visibility = View.VISIBLE
    }
    private fun setErrorBiaya(){
        binding.clBiaya.visibility = View.GONE
        binding.clUbah.visibility = View.GONE
    }

    private fun setButtonUpdate(biaya: String, denda:String, biayaAdmin:String) {
        val viewAlertDialog = View.inflate(this@AdminBiayaActivity, R.layout.alert_dialog_update_biaya, null)

        val etEditBiaya = viewAlertDialog.findViewById<EditText>(R.id.etEditBiaya)
        val etEditDenda = viewAlertDialog.findViewById<EditText>(R.id.etEditDenda)
        val etEditBiayaAdmin = viewAlertDialog.findViewById<EditText>(R.id.etEditBiayaAdmin)

        val btnSimpan = viewAlertDialog.findViewById<Button>(R.id.btnSimpan)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

        etEditBiaya.setText(biaya)
        etEditDenda.setText(denda)
        etEditBiayaAdmin.setText(biayaAdmin)

        val alertDialog = AlertDialog.Builder(this@AdminBiayaActivity)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        btnSimpan.setOnClickListener {
            if(etEditBiaya.text.toString().isNotEmpty() &&
                etEditDenda.text.toString().isNotEmpty() &&
                etEditBiayaAdmin.text.toString().isNotEmpty())
            {
                postUpdateBiaya(
                    etEditBiaya.text.toString(),
                    etEditDenda.text.toString(),
                    etEditBiayaAdmin.text.toString()
                )
            }
            else{
                Toast.makeText(this@AdminBiayaActivity, "Tidak boleh ada yang Kosong.\n Min 0", Toast.LENGTH_SHORT).show()
            }
            dialogInputan.dismiss()
        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }

    private fun postUpdateBiaya(biaya:String, denda: String, biayaAdmin: String) {
        viewModel.postUpdateDataBiaya(biaya, denda, biayaAdmin)
    }

    private fun getUpdateBiaya(){
        viewModel.getUpdateBiaya().observe(this@AdminBiayaActivity){result->
            when(result){
                is UIState.Success-> setSuccessPostUpdatBiaya(result.data)
                is UIState.Failure-> setFailurePostUpdatBiaya(result.message)
            }
        }
    }

    private fun setSuccessPostUpdatBiaya(data: ArrayList<ResponseModel>) {
        if(data.isNotEmpty()){
            Toast.makeText(this@AdminBiayaActivity, data[0].message_response, Toast.LENGTH_SHORT).show()
        } else{
            Toast.makeText(this@AdminBiayaActivity, "Gagal", Toast.LENGTH_SHORT).show()
        }
        fetchDataBiaya()
    }

    private fun setFailurePostUpdatBiaya(message: String) {
        Toast.makeText(this@AdminBiayaActivity, message, Toast.LENGTH_SHORT).show()
        fetchDataBiaya()
    }


    override fun onBackPressed() {
        startActivity(Intent(this@AdminBiayaActivity, AdminMainActivity::class.java))
        finish()
        super.onBackPressed()
    }
}
