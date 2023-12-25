package com.munmundev.feri_aplikasipembayaraniuranair.utils

class HurufAcak {
    fun getHurufAcak(): String{
        var str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        var hurufAcak = "1"
        for(i in 1..12){
            hurufAcak+=str.random()
        }
        return hurufAcak
    }
    fun getHurufAngkaAcak(): String{
        var str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
        var hurufAcak = "1"
        for(i in 1..12){
            hurufAcak+=str.random()
        }
        return hurufAcak
    }
}