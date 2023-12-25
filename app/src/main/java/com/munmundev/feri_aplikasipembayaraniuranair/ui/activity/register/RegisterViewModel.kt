package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.munmundev.feri_aplikasipembayaraniuranair.data.database.api.ApiService
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
    private var _cekUser = MutableLiveData<UIState<ArrayList<UsersModel>>>()
    private var _registerUser = MutableLiveData<UIState<ArrayList<ResponseModel>>>()

    fun cekUser(username:String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val cekUser = api.cekUserRegistrasi("", username)
                _cekUser.postValue(UIState.Success(cekUser))
            } catch (ex: Exception){
                UIState.Failure("Gagal menjalankan data: ${ex.message}")
            }
        }
    }

    fun registerUser(nama:String, alamat:String, nomorHp:String, username:String, password:String, sebagai:String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val registerUser = api.addUser("", nama, alamat, nomorHp, username, password, sebagai)
                _registerUser.postValue(UIState.Success(registerUser))
                Log.d("TAG", "registerUser: berhasil: ${registerUser} ")
            } catch (ex: Exception){
                Log.d("TAG", "registerUser: Gagal: ${ex.message}")
                _registerUser.postValue(UIState.Failure("Gagal bang"))
            }
        }
    }

    fun getCekUser(): LiveData<UIState<ArrayList<UsersModel>>> = _cekUser
    fun getRegisterUser(): LiveData<UIState<ArrayList<ResponseModel>>> = _registerUser
}