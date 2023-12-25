package com.munmundev.feri_aplikasipembayaraniuranair.ui.activity.admin.pembayaran.list_blok

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.munmundev.feri_aplikasipembayaraniuranair.data.database.api.ApiService
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.PerumahanModel
import com.munmundev.feri_aplikasipembayaraniuranair.data.model.UsersModel
import com.munmundev.feri_aplikasipembayaraniuranair.utils.network.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListBlokViewModel @Inject constructor(
    private val api: ApiService
): ViewModel() {
    var _blok = MutableLiveData<UIState<ArrayList<PerumahanModel>>>()
    var _user = MutableLiveData<UIState<ArrayList<UsersModel>>>()

    fun fetchDataBlok(idPerumahan: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = api.getBlokPerumahan("", idPerumahan)
                _blok.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _blok.postValue(UIState.Failure("Error Blok: ${ex.message}"))
            }
        }
    }

    fun fetchDataUser(idBlok: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = api.getAdminListUserPembayaran("", idBlok)
                _user.postValue(UIState.Success(data))
            } catch (ex: Exception){
                _user.postValue(UIState.Failure("Error User: ${ex.message}"))
            }
        }
    }

    fun getDataBlok():LiveData<UIState<ArrayList<PerumahanModel>>> = _blok
    fun getDataUser(): LiveData<UIState<ArrayList<UsersModel>>> = _user
}