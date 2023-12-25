package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.user.riwayat_pembayaran

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
class RiwayatPembayaranViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    private var _riwayatPembayaran = MutableLiveData<UIState<ArrayList<PembayaranModel>>>()

    fun fetchRiwayatPembayaran(idUser: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = api.getRiwayatPembayaranUser("", idUser)
                _riwayatPembayaran.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _riwayatPembayaran.postValue(UIState.Failure("Error Pada : ${ex.message}"))
            }
        }
    }

    fun getRiwayatPembayaran(): LiveData<UIState<ArrayList<PembayaranModel>>> = _riwayatPembayaran

}