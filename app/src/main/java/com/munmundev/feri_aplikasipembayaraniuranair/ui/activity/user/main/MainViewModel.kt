package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.user.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.munmundev.feri_aplikasipembayaraniuranair.data.database.api.ApiService
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.PembayaranModel
import com.munmundev.feri_aplikasipembayaraniuranair.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor (
    private val api: ApiService
): ViewModel() {
    private var _dataPembayaranUser = MutableLiveData<UIState<ArrayList<PembayaranModel>>>()

    fun fetchData(idUser: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = api.getPembayaranUser("", idUser)
                _dataPembayaranUser.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _dataPembayaranUser.postValue(UIState.Failure("Kesalahan pada $ex ")) // Tidak bagus menampilkan data exception untuk dilihat user
            }
        }
    }

    fun getData(): LiveData<UIState<ArrayList<PembayaranModel>>> = _dataPembayaranUser

}