package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.admin.biaya

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.munmundev.feri_aplikasipembayaraniuranair.data.database.api.ApiService
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.BiayaModel
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.ResponseModel
import com.munmundev.feri_aplikasipembayaraniuranair.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BiayaViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    val _biaya = MutableLiveData<UIState<ArrayList<BiayaModel>>>()
    val _responsePostUpdateBiaya = MutableLiveData<UIState<ArrayList<ResponseModel>>>()

    fun fetchDataBiaya(){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val dataBiaya = api.getAdminBiaya("")
                _biaya.postValue(UIState.Success(dataBiaya))
            } catch (ex: Exception){
                _biaya.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdateDataBiaya(biaya:String, denda:String, biayaAdmin:String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val dataBiaya = api.postUpdateBiaya("", biaya, denda, biayaAdmin)
                _responsePostUpdateBiaya.postValue(UIState.Success(dataBiaya))
            } catch (ex: Exception){
                _responsePostUpdateBiaya.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun getDataBiaya(): LiveData<UIState<ArrayList<BiayaModel>>> = _biaya
    fun getUpdateBiaya(): LiveData<UIState<ArrayList<ResponseModel>>> = _responsePostUpdateBiaya
}