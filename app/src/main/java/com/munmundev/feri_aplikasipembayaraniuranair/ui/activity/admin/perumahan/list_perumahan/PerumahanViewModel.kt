package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.admin.perumahan.list_perumahan

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
class PerumahanViewModel @Inject constructor(
    private val api: ApiService
) : ViewModel() {
    val _perumahan = MutableLiveData<UIState<ArrayList<PerumahanModel>>>()
    val _responseTambahPerumahan = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    val _responseUpdatePerumahan = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    val _responseHapusPerumahan = MutableLiveData<UIState<ArrayList<ResponseModel>>>()

    fun fetchDataPerumahan(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val dataPerumahan = api.getAllPerumahan("")
                _perumahan.postValue(UIState.Success(dataPerumahan))
            } catch (ex: Exception){
                _perumahan.postValue(UIState.Failure("Error perumahan: ${ex.message}"))
            }
        }
    }

    fun postTambahPerumahan(namaPerumahan: String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val postData = api.postTambahPerumahan("", namaPerumahan)
                _responseTambahPerumahan.postValue(UIState.Success(postData))
            } catch (ex: Exception){
                _responseTambahPerumahan.postValue(UIState.Failure("Error pada ${ex.message}"))
            }
        }
    }

    fun postUpdatePerumahan(namaPerumahan: String, idPerumahan: String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val postData = api.postUpdatePerumahan("", idPerumahan, namaPerumahan)
                _responseUpdatePerumahan.postValue(UIState.Success(postData))
            } catch (ex: Exception){
                _responseUpdatePerumahan.postValue(UIState.Failure("Error pada ${ex.message}"))
            }
        }
    }

    fun postHapusPerumahan(idPerumahan: String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val postData = api.postHapusPerumahan("", idPerumahan)
                _responseHapusPerumahan.postValue(UIState.Success(postData))
            } catch (ex: Exception){
                _responseHapusPerumahan.postValue(UIState.Failure("Error pada ${ex.message}"))
            }
        }
    }

    fun getDataPerumahan(): LiveData<UIState<ArrayList<PerumahanModel>>> = _perumahan
    fun getPostPerumahan(): LiveData<UIState<ArrayList<ResponseModel>>> = _responseTambahPerumahan
    fun getPostUpdatePerumahan(): LiveData<UIState<ArrayList<ResponseModel>>> = _responseUpdatePerumahan
    fun getPostHapusPerumahan(): LiveData<UIState<ArrayList<ResponseModel>>> = _responseHapusPerumahan

}