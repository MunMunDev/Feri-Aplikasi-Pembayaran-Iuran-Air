package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.admin.pembayaran.riwayat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.munmundev.feri_aplikasipembayaraniuranair.data.database.api.ApiService
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.PembayaranModel
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.ResponseModel
import com.munmundev.feri_aplikasipembayaraniuranair.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminRiwayatPembayaranViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    var _riwayatPembayaran = MutableLiveData<UIState<ArrayList<PembayaranModel>>>()
    var _hapusPembayaran = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    var _updatePembayaran = MutableLiveData<UIState<ArrayList<ResponseModel>>>()

    fun fetchData(idUser:String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = api.getRiwayatPembayaranUser("", idUser)
                _riwayatPembayaran.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _riwayatPembayaran.postValue(UIState.Failure("Gagal: ${ex.message}"))
            }
        }
    }
    fun deletePembayaran(idPembayaran: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = api.postAdminHapusPembayaran("", idPembayaran)
                _hapusPembayaran.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _hapusPembayaran.postValue(UIState.Failure("Gagal: ${ex.message}"))
            }
        }
    }
    fun updatePembayaran(pembayaranModel: PembayaranModel){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = api.postAdminUpdatePembayaran(
                    "", pembayaranModel.idPembayaran!!, pembayaranModel.harga!!,
                    pembayaranModel.denda!!, pembayaranModel.biayaAdmin!!, pembayaranModel.tenggatWaktu!!,
                    pembayaranModel.waktuPembayaran!!)
                _updatePembayaran.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _updatePembayaran.postValue(UIState.Failure("Gagal: ${ex.message}"))
            }
        }
    }
    fun getData(): LiveData<UIState<ArrayList<PembayaranModel>>> = _riwayatPembayaran
    fun getDeletePembayaran(): LiveData<UIState<ArrayList<ResponseModel>>> = _hapusPembayaran
    fun getUpdatePembayaran(): LiveData<UIState<ArrayList<ResponseModel>>> = _updatePembayaran
}