package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.admin.perumahan.list_blok

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
class BlokPerumahanViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    val _blokPerumahan = MutableLiveData<UIState<ArrayList<PerumahanModel>>>()
    val _responseTambahBlokPerumahan = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    val _responseUpdateBlokPerumahan = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    val _responseHapusBlokPerumahan = MutableLiveData<UIState<ArrayList<ResponseModel>>>()

    fun fetchDataPerumahan(idPerumahan: String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val dataBlokPerumahan = api.getBlokPerumahan("", idPerumahan)
                _blokPerumahan.postValue(UIState.Success(dataBlokPerumahan))
            } catch (ex: Exception){
                _blokPerumahan.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postTambahDataPerumahan(idPerumahan: String, namaBlokPerumahan: String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val tambahBlokPerumahan = api.postTambahBlokPerumahan("", idPerumahan, namaBlokPerumahan)
                _responseTambahBlokPerumahan.postValue(UIState.Success(tambahBlokPerumahan))
            } catch (ex: Exception){
                _responseTambahBlokPerumahan.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }
    fun postUpdateDataPerumahan(idPerumahan: String, idBlokPerumahan: String, namaBlokPerumahan: String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val hapusBlokPerumahan = api.postUpdateBlokPerumahan("", idPerumahan, idBlokPerumahan, namaBlokPerumahan)
                _responseUpdateBlokPerumahan.postValue(UIState.Success(hapusBlokPerumahan))
            } catch (ex: Exception){
                _responseUpdateBlokPerumahan.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }
    fun postHapusDataPerumahan(idBlokPerumahan: String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val hapusBlokPerumahan = api.postHapusBlokPerumahan("", idBlokPerumahan)
                _responseHapusBlokPerumahan.postValue(UIState.Success(hapusBlokPerumahan))
            } catch (ex: Exception){
                _responseHapusBlokPerumahan.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun getBlokPerumahan(): LiveData<UIState<ArrayList<PerumahanModel>>> = _blokPerumahan
    fun getTambahPerumahan(): LiveData<UIState<ArrayList<ResponseModel>>> = _responseTambahBlokPerumahan
    fun getUpdatePerumahan(): LiveData<UIState<ArrayList<ResponseModel>>> = _responseUpdateBlokPerumahan
    fun getHapusPerumahan(): LiveData<UIState<ArrayList<ResponseModel>>> = _responseHapusBlokPerumahan
}