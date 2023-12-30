package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.user.akun

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
class AkunViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    var _akun = MutableLiveData<UIState<ArrayList<UsersModel>>>()
    var _responsePostUpdateUser = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    var _perumahan = MutableLiveData<UIState<ArrayList<PerumahanModel>>>()
    var _blokPerumahan = MutableLiveData<UIState<ArrayList<PerumahanModel>>>()
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

    fun postUpdateUser(
        idUser:String, nama: String, idBlok:String,
        noAlamat: String, nomorHp: String, username: String,
        password: String, usernameLama:String
    ){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val data = api.postUpdateUser("", idUser, nama, idBlok, noAlamat, nomorHp, username, password, usernameLama)
                _responsePostUpdateUser.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _responsePostUpdateUser.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }



    fun getData():LiveData<UIState<ArrayList<UsersModel>>> = _akun
    fun getUpdateData(): LiveData<UIState<ArrayList<ResponseModel>>> = _responsePostUpdateUser
    fun getDataPerumahan(): LiveData<UIState<ArrayList<PerumahanModel>>> = _perumahan
    fun getDataBlokPerumahan(): LiveData<UIState<ArrayList<PerumahanModel>>> = _blokPerumahan
}