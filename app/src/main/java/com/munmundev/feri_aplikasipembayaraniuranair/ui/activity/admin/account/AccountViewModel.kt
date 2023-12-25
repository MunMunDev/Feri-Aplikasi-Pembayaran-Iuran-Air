package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.admin.account

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
class AccountViewModel @Inject constructor(
    private val api: ApiService
) : ViewModel() {
    var _user = MutableLiveData<UIState<ArrayList<UsersModel>>>()
    var _responsePostTambahUser = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    var _responsePostUpdateUser = MutableLiveData<UIState<ArrayList<ResponseModel>>>()
    var _perumahan = MutableLiveData<UIState<ArrayList<PerumahanModel>>>()
    var _blokPerumahan = MutableLiveData<UIState<ArrayList<PerumahanModel>>>()

    fun fetchDataUsers(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val dataUser = api.getAllUser("")
                _user.postValue(UIState.Success(dataUser))
            } catch (ex: Exception){
                _user.postValue(UIState.Failure("Error User: ${ex.message}"))
            }
        }
    }

    fun postTambahUser(nama:String, alamat:String, nomorHp:String, username:String, password:String, sebagai:String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val data = api.addUser("", nama, alamat, nomorHp, username, password, sebagai)
                _responsePostTambahUser.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _responsePostTambahUser.postValue(UIState.Failure("Error: ${ex.message}"))
            }
        }
    }

    fun postUpdateUser(idUser:String, nama:String, alamat:String, nomorHp:String, username:String, password:String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val data = api.postUpdateUser("", idUser, nama, alamat, nomorHp, username, password)
                _responsePostTambahUser.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _responsePostTambahUser.postValue(UIState.Failure("Error: ${ex.message}"))
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

    fun getDataUsers(): LiveData<UIState<ArrayList<UsersModel>>> = _user
    fun getTambahData(): LiveData<UIState<ArrayList<ResponseModel>>> = _responsePostTambahUser
    fun getUpdateData(): LiveData<UIState<ArrayList<ResponseModel>>> = _responsePostUpdateUser
    fun getDataPerumahan(): LiveData<UIState<ArrayList<PerumahanModel>>> = _perumahan
    fun getDataBlokPerumahan(): LiveData<UIState<ArrayList<PerumahanModel>>> = _blokPerumahan
}