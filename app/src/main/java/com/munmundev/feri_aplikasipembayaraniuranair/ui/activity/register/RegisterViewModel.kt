package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.munmundev.feri_aplikasipembayaraniuranair.data.database.api.ApiService
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.PerumahanModel
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.ResponseModel
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.UsersModel
import com.munmundev.feri_aplikasipembayaraniuranair.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private var _registerUser = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    var _perumahan = MutableLiveData<UIState<ArrayList<PerumahanModel>>>()
    var _blokPerumahan = MutableLiveData<UIState<ArrayList<PerumahanModel>>>()

    fun postRegisterUser(nama:String, idBlok:String, alamat:String, nomorHp:String, username:String, password:String, sebagai:String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val registerUser = api.addUser("", nama, idBlok, alamat, nomorHp, username, password, sebagai)
                _registerUser.postValue(UIState.Success(registerUser))
            } catch (ex: Exception){
                _registerUser.postValue(UIState.Failure("Gagal : ${ex.message}"))
            }
        }
    }

    fun fetchDataPerumahan(){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val dataPerumahan = api.getAllPerumahan("")
                _perumahan.postValue(UIState.Success(dataPerumahan))
            } catch (ex: Exception){
                _perumahan.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun fetchDataBlokPerumahan(){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val dataBlokPerumahan = api.getAllBlokPerumahan("")
                _blokPerumahan.postValue(UIState.Success(dataBlokPerumahan))
            } catch (ex: Exception){
                _blokPerumahan.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun getRegisterUser(): LiveData<UIState<ArrayList<ResponseModel>>> = _registerUser
    fun getDataPerumahan(): LiveData<UIState<ArrayList<PerumahanModel>>> = _perumahan
    fun getDataBlokPerumahan(): LiveData<UIState<ArrayList<PerumahanModel>>> = _blokPerumahan
}