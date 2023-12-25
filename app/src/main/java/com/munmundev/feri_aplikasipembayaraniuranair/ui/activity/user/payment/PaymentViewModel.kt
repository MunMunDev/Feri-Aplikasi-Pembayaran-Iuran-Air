package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.user.payment

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
class PaymentViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    val _pembayaran = MutableLiveData<UIState<ArrayList<PembayaranModel>>>()
    val _responseRegistrasiPembayaran = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    val _responseDefaultPembayaran = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    val _responseEnamBulanPembayaran = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    val _responseDuabelasBulanPembayaran = MutableLiveData<UIState<ArrayList<ResponseModel>>>()

    fun fetchDataPembayaran(idUser:String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val dataPembayaran = api.getPembayaranUser("", idUser)
                _pembayaran.postValue(UIState.Success(dataPembayaran))
            } catch (ex: Exception){
                _pembayaran.postValue(UIState.Failure("Error pada: ${ex.message}"))
            }
        }
    }

    fun postRegistrasiPembayaran(orderId: String, idUser: String, keterangan:String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val dataRegistrasiPembayaran = api.postRegistrasiPembayaran(
                    "", orderId, idUser, keterangan
                )
                _responseRegistrasiPembayaran.postValue(UIState.Success(dataRegistrasiPembayaran))
            } catch (ex: Exception){
                _responseRegistrasiPembayaran.postValue(UIState.Failure("Error pada : ${ex.message}"))
            }
        }
    }

    fun postDefaultPembayaran(idUser: String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val dataRegistrasiPembayaran = api.postDefaultPembayaran("", idUser)
                _responseDefaultPembayaran.postValue(UIState.Success(dataRegistrasiPembayaran))
            } catch (ex: Exception){
                _responseDefaultPembayaran.postValue(UIState.Failure("Error pada : ${ex.message}"))
            }
        }
    }
    fun postEnamBulanPembayaran(idUser: String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val dataRegistrasiPembayaran = api.postEnamBulanPembayaran("", idUser)
                _responseEnamBulanPembayaran.postValue(UIState.Success(dataRegistrasiPembayaran))
            } catch (ex: Exception){
                _responseEnamBulanPembayaran.postValue(UIState.Failure("Error pada : ${ex.message}"))
            }
        }
    }
    fun postDuabelasBulanPembayaran(idUser: String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val dataRegistrasiPembayaran = api.postDuaBelasBulanPembayaran("", idUser)
                _responseDuabelasBulanPembayaran.postValue(UIState.Success(dataRegistrasiPembayaran))
            } catch (ex: Exception){
                _responseDuabelasBulanPembayaran.postValue(UIState.Failure("Error pada : ${ex.message}"))
            }
        }
    }

    fun getDataPembayaran(): LiveData<UIState<ArrayList<PembayaranModel>>> = _pembayaran
    fun getRegistrasiPembayaran(): LiveData<UIState<ArrayList<ResponseModel>>> = _responseRegistrasiPembayaran
    fun getDefaultPembayaran(): LiveData<UIState<ArrayList<ResponseModel>>> = _responseDefaultPembayaran
    fun getEnamBulanPembayaran(): LiveData<UIState<ArrayList<ResponseModel>>> = _responseEnamBulanPembayaran
    fun getDuabelasBulanPembayaran(): LiveData<UIState<ArrayList<ResponseModel>>> = _responseDuabelasBulanPembayaran
}