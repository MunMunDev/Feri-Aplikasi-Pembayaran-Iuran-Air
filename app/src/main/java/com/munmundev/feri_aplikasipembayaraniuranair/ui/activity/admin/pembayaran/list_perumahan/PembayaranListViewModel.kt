package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.admin.pembayaran.list_perumahan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.munmundev.feri_aplikasipembayaraniuranair.data.database.api.ApiService
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.PerumahanModel
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.ResponseModel
import com.munmundev.feri_aplikasipembayaraniuranair.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PembayaranListViewModel @Inject constructor(
    private val api : ApiService
): ViewModel() {
    var _perumahan = MutableLiveData<UIState<ArrayList<PerumahanModel>>>()
    var _pembayaranBulanIni = MutableLiveData<UIState<ArrayList<ResponseModel>>>()

    fun fetchDataPerumahan(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = api.getAllPerumahan("")
                _perumahan.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _perumahan.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postSemuaPembayaranBulanIni(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = api.postAdminTambahSemuaPembayaranBulanIni("")
                _pembayaranBulanIni.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _pembayaranBulanIni.postValue(UIState.Failure("Error Response: ${ex.message}"))
            }
        }
    }

    fun getDataPerumahan(): LiveData<UIState<ArrayList<PerumahanModel>>> = _perumahan
    fun getPostSemuaPembayaranBulanIni(): LiveData<UIState<ArrayList<ResponseModel>>> = _pembayaranBulanIni
}