package com.munmundev.feri_aplikasipembayaraniuranair.utils.network

sealed class UIState<out R> {
    data class Success<out T>(val data: T): UIState<T>()
    data class Failure(val message: String): UIState<Nothing>()
}