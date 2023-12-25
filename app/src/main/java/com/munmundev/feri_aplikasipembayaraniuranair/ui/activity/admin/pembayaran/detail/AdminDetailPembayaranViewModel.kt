package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.admin.pembayaran.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.munmundev.feri_aplikasipembayaraniuranair.data.database.api.ApiService
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.BiayaModel
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.PembayaranModel
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.ResponseModel
import com.munmundev.feri_aplikasipembayaraniuranair.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminDetailPembayaranViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private var _pembayaran = MutableLiveData<UIState<ArrayList<PembayaranModel>>>()
    private var _responseTambahData = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private var _responseKonfirmasiData = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private var _responseKonfirmasiSemuaData = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    private var _biaya = MutableLiveData<UIState<ArrayList<BiayaModel>>>()

    fun fetchDataPembayaran(idUser:String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = api.getPembayaranUser("", idUser)
                _pembayaran.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _pembayaran.postValue(UIState.Failure("Gagal ambil database: ${ex.message}"))
            }
        }
    }

    fun postTambahPembayaran(pembayaran: PembayaranModel){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = api.postAdminTambahPembayaran(
                    "",
                    pembayaran.idUser!!,
                    pembayaran.harga!!,
                    pembayaran.denda!!,
                    pembayaran.biayaAdmin!!,
                    pembayaran.tenggatWaktu!!,
                    pembayaran.waktuPembayaran!!
                )
                _responseTambahData.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _responseTambahData.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postKonfirmasiDatapembayaran(idPembayaran: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = api.postAdminKonfirmasiPembayaran("", idPembayaran)
                _responseKonfirmasiData.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _responseKonfirmasiData.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postKonfirmasiSemuaDataPembayaran(idUser: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = api.postAdminKonfirmasiAllPembayaran("", idUser)
                _responseKonfirmasiSemuaData.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _responseKonfirmasiSemuaData.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun fetchDataBiaya(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = api.getAdminBiaya("")
                _biaya.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _biaya.postValue(UIState.Failure("Gagal ambil database: ${ex.message}"))
            }
        }
    }

    fun getData(): LiveData<UIState<ArrayList<PembayaranModel>>> = _pembayaran
    fun getTambahPembayaran(): LiveData<UIState<ArrayList<ResponseModel>>> = _responseTambahData
    fun getKonfirmasiDataPembayaran(): LiveData<UIState<ArrayList<ResponseModel>>> = _responseKonfirmasiData
    fun getKonfirmasiSemuaDataPembayaran(): LiveData<UIState<ArrayList<ResponseModel>>> = _responseKonfirmasiSemuaData
    fun getDataBiaya(): LiveData<UIState<ArrayList<BiayaModel>>> = _biaya
}