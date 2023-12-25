package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.user.payment

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.muharya_pengingatjadwalkeretaapi.utils.SharedPreferencesLogin
import com.midtrans.sdk.uikit.api.model.CustomerDetails
import com.midtrans.sdk.uikit.api.model.ItemDetails
import com.midtrans.sdk.uikit.api.model.SnapTransactionDetail
import com.midtrans.sdk.uikit.external.UiKitApi
import com.midtrans.sdk.uikit.internal.util.UiKitConstants
import com.munmundev.feri_aplikasipembayaraniuranair.R
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.PembayaranModel
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.ResponseModel
import com.munmundev.feri_aplikasipembayaraniuranair.databinding.ActivityPaymentBinding
import com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.user.main.MainActivity
import com.munmundev.feri_aplikasipembayaraniuranair.utils.Constant
import com.munmundev.feri_aplikasipembayaraniuranair.utils.HurufAcak
import com.munmundev.feri_aplikasipembayaraniuranair.utils.KonversiRupiah
import com.munmundev.feri_aplikasipembayaraniuranair.utils.LoadingAlertDialog
import com.munmundev.feri_aplikasipembayaraniuranair.utils.TanggalDanWaktu
import com.munmundev.feri_aplikasipembayaraniuranair.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class PaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentBinding
    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin
    private lateinit var loading: LoadingAlertDialog
    private val viewModel: PaymentViewModel by viewModels()
    @Inject lateinit var tanggalDanWaktu: TanggalDanWaktu
    @Inject lateinit var hurufAcak: HurufAcak
    @Inject lateinit var rupiah: KonversiRupiah
    private var totalBiaya = 0.0
    private var uuid = UUID.randomUUID().toString()

    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var customerDetails: CustomerDetails
    private var itemDetails: ArrayList<ItemDetails> = arrayListOf()
    private lateinit var initTransactionDetails: SnapTransactionDetail

    private var idUser: String = ""


//    private val launcher2 =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result?.resultCode == RESULT_OK) {
//                result.data?.let {
//                    val transactionResult = it.getParcelableExtra<com.midtrans.sdk.uikit.api.model.TransactionResult>(UiKitConstants.KEY_TRANSACTION_RESULT)
//                    Toast.makeText(this, "${transactionResult?.transactionId}", Toast.LENGTH_LONG).show()
//                }
//            }
//        }
//
//
//
//    private var customerDetails2 = CustomerDetails(
//        firstName = sharedPreferencesLogin.getNama(),
//        customerIdentifier = "mail@mail.com",
//        email = "mail@mail.com",
//        phone = "089121"
//    )
//    private var itemDetails2 = listOf(
//        ItemDetails(
//            "${sharedPreferencesLogin.getIdUser()}-${hurufAcak.getHurufAngkaAcak()}",
//            36500.0,
//            1,
//            "3 Bulan"
//        )
//    )

//    private fun initTransactionDetails() : SnapTransactionDetail {
//        return SnapTransactionDetail(
//            orderId = UUID.randomUUID().toString(),
//            grossAmount = 36500.0
//        )
//    }

//    private fun initTransactionDetails2() : SnapTransactionDetail {
//        return SnapTransactionDetail(
//            orderId = "${sharedPreferencesLogin.getIdUser()}-${hurufAcak.getHurufAngkaAcak()}",
//            grossAmount = 36500.0
//        )
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSharedPreferencesLogin()
        setLoading()
        setButton()
        fetchDataPembayaran(sharedPreferencesLogin.getIdUser().toString())
        getDataPembayaran()
        getDataRegistrasiPembayaran()
        konfigurationMidtrans()
        getPostDefaultPembayaran()
        getPostEnamBulanPembayaran()
        getPostDuabelasBulanPembayaran()
    }

    private fun konfigurationMidtrans() {
        setLauncher()
        setCustomerDetails()
        setInitTransactionDetails()
        buildUiKit()
    }

    private fun getDataRegistrasiPembayaran() {
        viewModel.getRegistrasiPembayaran().observe(this@PaymentActivity){result->
            when(result){
                is UIState.Success-> setDataSuccessRegistrasiPembayaran(result.data)
                is UIState.Failure-> setDataFailureRegistrasiPembayaran(result.message)
            }
        }
    }

    private fun setDataSuccessRegistrasiPembayaran(data: ArrayList<ResponseModel>) {
        if(data[0].status == "0"){
            UiKitApi.getDefaultInstance().startPaymentUiFlow(
                activity = this@PaymentActivity,
                launcher = launcher,
                transactionDetails = initTransactionDetails,
                customerDetails = customerDetails,
                itemDetails = itemDetails
            )
        }else{
            Toast.makeText(this@PaymentActivity, "Gagal Registrasi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setLoading() {
        loading = LoadingAlertDialog(this@PaymentActivity)
        loading.alertDialogLoading()
    }

    private fun fetchDataPembayaran(idUser: String) {
        viewModel.fetchDataPembayaran(idUser)
    }

    private fun getDataPembayaran() {
        viewModel.getDataPembayaran().observe(this@PaymentActivity){ result ->
            when(result){
                is UIState.Success -> setDataSuccessPembayaran(result.data)
                is UIState.Failure -> setDataFailurePembayaran(result.message)
            }
        }
    }

    private fun setDataFailureRegistrasiPembayaran(message: String) {
        Toast.makeText(this@PaymentActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun fetchDataRegistrasiPembayaran(orderId: String, idUser: String, keterangan: String) {
        viewModel.postRegistrasiPembayaran(orderId, idUser, keterangan)
    }

    private fun setDataFailurePembayaran(message: String) {
        Toast.makeText(this@PaymentActivity, message, Toast.LENGTH_SHORT).show()
        loading.alertDialogCancel()
    }

    private fun setDataSuccessPembayaran(data: ArrayList<PembayaranModel>) {
        Log.d("PaymentActivityTAG", "setDataSuccessPembayaran: ${data.size}")
        if(data.size>0){
            var namaJumlahBulan = ""
            var jumlahBulan = 0
            var harga = 0
            var denda = 0
            var biayaAdmin = 0

            Log.d("MainActivityTag", "setData: $data")

            var no = 1

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

                val totalSatuItem = (value.harga!!.toInt()+value.denda!!.toInt()+value.biayaAdmin!!.toInt()).toDouble()
                val tanggal = "${arrayWaktu[1]} ${arrayWaktu[2]}"

                Log.d("PaymentActivityTAG", "setDataSuccessPembayaran: $totalSatuItem dan tanggal: $tanggal")

                itemDetails.add(
                    ItemDetails(
                        "$no", totalSatuItem, 1, "bulan ($tanggal)"
                    )
                )
                no++
            }

            val total = harga+denda+biayaAdmin
            totalBiaya = total.toDouble()
            Log.d("PaymentActivityTAG", "setDataSuccessPembayaran: $totalBiaya")

            initTransactionDetails = SnapTransactionDetail(
                uuid,
                totalBiaya
            )

            binding.apply {
                tvBulan.text = "$jumlahBulan Bulan"
                tvHarga.text = rupiah.rupiah(harga.toLong())
                tvDenda.text = rupiah.rupiah(denda.toLong())
                tvBiayaAdmin.text = rupiah.rupiah(biayaAdmin.toLong())
                tvTotalTagihan.text = rupiah.rupiah(total.toLong())
                tvBulanTagihan.text = namaJumlahBulan

                isiPembayaranSekarang.visibility = View.VISIBLE
            }
        } else{
            Toast.makeText(this@PaymentActivity, "Terima Kasih Telah Membayar", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@PaymentActivity, MainActivity::class.java))
            finish()
        }

        loading.alertDialogCancel()
    }

    private fun setInitTransactionDetails() {
        initTransactionDetails = SnapTransactionDetail(
            uuid,
            totalBiaya
        )
    }

    private fun setCustomerDetails() {
        var nomorHp = sharedPreferencesLogin.getNomorHp()
        if(nomorHp == ""){
            nomorHp = "0"
        }
        customerDetails = CustomerDetails(
            firstName = sharedPreferencesLogin.getNama(),
            customerIdentifier = "${sharedPreferencesLogin.getIdUser()}",
            email = "mail@mail.com",
            phone = nomorHp
        )
    }

    private fun setLauncher() {
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result?.resultCode == RESULT_OK) {
                result.data?.let {
                    val transactionResult = it.getParcelableExtra<com.midtrans.sdk.uikit.api.model.TransactionResult>(UiKitConstants.KEY_TRANSACTION_RESULT)
//                    Toast.makeText(this, "${transactionResult?.transactionId}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setButton() {
        Log.d("PaymentActivityTAG", "total: $totalBiaya dan biaya satuan: ")
        binding.apply {
            btnBack.setOnClickListener {
                startActivity(Intent(this@PaymentActivity, MainActivity::class.java))
                finish()
            }

            btnDefault.setOnClickListener {
                setButtonKonfirmasiDefault()
            }

            btn6Bulan.setOnClickListener {
                setButtonKonfirmasi6Bulan()
            }

            btn12Bulan.setOnClickListener {
                setButtonKonfirmasi12Bulan()
            }

            btnBayar.setOnClickListener {
                fetchDataRegistrasiPembayaran(uuid, idUser, "Pending")

//                UiKitApi.getDefaultInstance().startPaymentUiFlow(
//                    activity = this@PaymentActivity,
//                    launcher = launcher,
//                    transactionDetails = initTransactionDetails,
//                    customerDetails = customerDetails,
//                    itemDetails = itemDetails
//                )
            }
        }

    }

    private fun setButtonKonfirmasiDefault() {
        val viewAlertDialog = View.inflate(this@PaymentActivity, R.layout.alert_dialog_konfirmasi, null)
        val tvTitleKonfirmasi = viewAlertDialog.findViewById<TextView>(R.id.tvTitleKonfirmasi)
        val tvBodyKonfirmasi = viewAlertDialog.findViewById<TextView>(R.id.tvBodyKonfirmasi)
        val btnKonfirmasi = viewAlertDialog.findViewById<Button>(R.id.btnKonfirmasi)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)
        tvTitleKonfirmasi.text = "Konfirmasi"
        tvBodyKonfirmasi.text = "Kembalikan Pembayaran Default? "

        val alertDialog = AlertDialog.Builder(this@PaymentActivity)
        alertDialog.setView(viewAlertDialog)
        val dialog = alertDialog.create()
        dialog.show()

        btnKonfirmasi.setOnClickListener {
            loading.alertDialogLoading()
            setPostDefaultPembayaran()
            dialog.dismiss()
        }
        btnBatal.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun setPostDefaultPembayaran() {
        viewModel.postDefaultPembayaran(idUser)
    }

    private fun getPostDefaultPembayaran(){
        viewModel.getDefaultPembayaran().observe(this@PaymentActivity){result->
            when(result){
                is UIState.Success-> setDataSuccessDefaultPembayaran(result.data)
                is UIState.Failure-> setDataFailureDefaultPembayaran(result.message)
            }
        }
    }

    private fun setDataSuccessDefaultPembayaran(data: ArrayList<ResponseModel>) {
        if(data.isNotEmpty()){
            if(data[0].status=="1"){
                Toast.makeText(this@PaymentActivity, "Gagal Update Data", Toast.LENGTH_SHORT).show()
            } else if(data[0].status=="0"){
                Toast.makeText(this@PaymentActivity, "Berhasil Update Data", Toast.LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this@PaymentActivity, "Error", Toast.LENGTH_SHORT).show()
        }
        fetchDataPembayaran(idUser)
    }

    private fun setDataFailureDefaultPembayaran(message: String) {
        Toast.makeText(this@PaymentActivity, message, Toast.LENGTH_SHORT).show()
        fetchDataPembayaran(idUser)

    }

    private fun setButtonKonfirmasi6Bulan() {
        val viewAlertDialog = View.inflate(this@PaymentActivity, R.layout.alert_dialog_konfirmasi, null)
        val tvTitleKonfirmasi = viewAlertDialog.findViewById<TextView>(R.id.tvTitleKonfirmasi)
        val tvBodyKonfirmasi = viewAlertDialog.findViewById<TextView>(R.id.tvBodyKonfirmasi)
        val btnKonfirmasi = viewAlertDialog.findViewById<Button>(R.id.btnKonfirmasi)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)
        tvTitleKonfirmasi.text = "Konfirmasi"
        tvBodyKonfirmasi.text = "Lakukan Pembayaran 6 Bulan?"

        val alertDialog = AlertDialog.Builder(this@PaymentActivity)
        alertDialog.setView(viewAlertDialog)
        val dialog = alertDialog.create()
        dialog.show()

        btnKonfirmasi.setOnClickListener {
            setPostEnamBulanPembayaran()
            dialog.dismiss()
        }
        btnBatal.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun setPostEnamBulanPembayaran() {
        viewModel.postEnamBulanPembayaran(idUser)
    }

    private fun getPostEnamBulanPembayaran(){
        viewModel.getEnamBulanPembayaran().observe(this@PaymentActivity){result->
            when(result){
                is UIState.Success-> setDataSuccessEnamBulanPembayaran(result.data)
                is UIState.Failure-> setDataFailureEnamBulanPembayaran(result.message)
            }
        }
    }

    private fun setDataSuccessEnamBulanPembayaran(data: ArrayList<ResponseModel>) {
        if(data[0].status =="0"){
            Toast.makeText(this@PaymentActivity, "Berhasil", Toast.LENGTH_SHORT).show()
        } else{
            Toast.makeText(this@PaymentActivity, "Gagal", Toast.LENGTH_SHORT).show()
        }
        fetchDataPembayaran(idUser)
    }

    private fun setDataFailureEnamBulanPembayaran(message: String) {
        Toast.makeText(this@PaymentActivity, message, Toast.LENGTH_SHORT).show()
        fetchDataPembayaran(idUser)
    }

    private fun setButtonKonfirmasi12Bulan() {
        val viewAlertDialog = View.inflate(this@PaymentActivity, R.layout.alert_dialog_konfirmasi, null)
        val tvTitleKonfirmasi = viewAlertDialog.findViewById<TextView>(R.id.tvTitleKonfirmasi)
        val tvBodyKonfirmasi = viewAlertDialog.findViewById<TextView>(R.id.tvBodyKonfirmasi)
        val btnKonfirmasi = viewAlertDialog.findViewById<Button>(R.id.btnKonfirmasi)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)
        tvTitleKonfirmasi.text = "Konfirmasi"
        tvBodyKonfirmasi.text = "Lakukan Pembayaran 12 Bulan?"

        val alertDialog = AlertDialog.Builder(this@PaymentActivity)
        alertDialog.setView(viewAlertDialog)
        val dialog = alertDialog.create()
        dialog.show()

        btnKonfirmasi.setOnClickListener {
            setPostDuabelasBulanPembayaran()
            dialog.dismiss()
        }
        btnBatal.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun setPostDuabelasBulanPembayaran() {
        viewModel.postDuabelasBulanPembayaran(idUser)
    }

    private fun getPostDuabelasBulanPembayaran(){
        viewModel.getDuabelasBulanPembayaran().observe(this@PaymentActivity){result->
            when(result){
                is UIState.Success-> setDataSuccessDuabelasBulanPembayaran(result.data)
                is UIState.Failure-> setDataFailureDuabelasBulanPembayaran(result.message)
            }
        }
    }

    private fun setDataSuccessDuabelasBulanPembayaran(data: java.util.ArrayList<ResponseModel>) {
        if(data[0].status == "0"){
            Toast.makeText(this@PaymentActivity, "Berhasil", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this@PaymentActivity, "Gagal", Toast.LENGTH_SHORT).show()
        }
        fetchDataPembayaran(idUser)
    }

    private fun setDataFailureDuabelasBulanPembayaran(message: String) {
        Toast.makeText(this@PaymentActivity, message, Toast.LENGTH_SHORT).show()
        fetchDataPembayaran(idUser)
    }

    private fun setSharedPreferencesLogin() {
        sharedPreferencesLogin = SharedPreferencesLogin(this@PaymentActivity)
        idUser = sharedPreferencesLogin.getIdUser().toString()
    }

    private fun buildUiKit() {
        setInitTransactionDetails()
        UiKitApi.Builder()
            .withContext(this.applicationContext)
            .withMerchantUrl(Constant.MIDTRANS_BASE_URL)
            .withMerchantClientKey(Constant.MIDTRANS_CLIENT_KEY)
            .enableLog(true)
            .build()
        uiKitCustomSetting()
    }

    private fun uiKitCustomSetting() {
        val uIKitCustomSetting = UiKitApi.getDefaultInstance().uiKitSetting
        uIKitCustomSetting.saveCardChecked = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            val transactionResult = data?.getParcelableExtra<com.midtrans.sdk.uikit.api.model.TransactionResult>(UiKitConstants.KEY_TRANSACTION_RESULT)
            if (transactionResult != null) {
                loading.alertDialogLoading()
                when (transactionResult.status) {
                    UiKitConstants.STATUS_SUCCESS -> {
//                        Toast.makeText(this, "Transaction Finished. ID: " + transactionResult.transactionId, Toast.LENGTH_LONG).show()
                        fetchDataPembayaran(idUser)
                    }
                    UiKitConstants.STATUS_PENDING -> {
//                        Toast.makeText(this, "Transaction Pending. ID: " + transactionResult.transactionId, Toast.LENGTH_LONG).show()
                        fetchDataPembayaran(idUser)
                    }
                    UiKitConstants.STATUS_FAILED -> {
//                        Toast.makeText(this, "Transaction Failed. ID: " + transactionResult.transactionId, Toast.LENGTH_LONG).show()
                        fetchDataPembayaran(idUser)
                    }
                    UiKitConstants.STATUS_CANCELED -> {
//                        Toast.makeText(this, "Transaction Cancelled", Toast.LENGTH_LONG).show()
                        fetchDataPembayaran(idUser)
                    }
                    UiKitConstants.STATUS_INVALID -> {
//                        Toast.makeText(this, "Transaction Invalid. ID: " + transactionResult.transactionId, Toast.LENGTH_LONG).show()
                        fetchDataPembayaran(idUser)
                    }
                    else -> {
//                        Toast.makeText(this, "Transaction ID: " + transactionResult.transactionId + ". Message: " + transactionResult.status, Toast.LENGTH_LONG).show()
                        fetchDataPembayaran(idUser)
                    }
                }
            } else {
                Toast.makeText(this, "Transaction Invalid", Toast.LENGTH_LONG).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@PaymentActivity, MainActivity::class.java))
        finish()
    }
}