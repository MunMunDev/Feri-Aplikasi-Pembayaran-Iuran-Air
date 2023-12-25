package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.user.akun

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.munmundev.feri_aplikasipembayaraniuranair.data.database.api.ApiService
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
    fun postData(usersModel: UsersModel){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val data = api.postUpdateUser(
                    "", usersModel.idUser!!, usersModel.nama!!,
                    usersModel.alamat!!, usersModel.nomorHp!!,
                    usersModel.username!!, usersModel.password!!
                )
                if(data[0].status == "0"){
                    _akun.postValue(UIState.Success(arrayListOf(usersModel)))
                }
                else{
                    _akun.postValue(UIState.Success(arrayListOf()))
                }
            } catch (ex: Exception){
                _akun.postValue(UIState.Failure("Error Pada ${ex.message}"))
            }
        }
    }



    fun getData():LiveData<UIState<ArrayList<UsersModel>>> = _akun
}