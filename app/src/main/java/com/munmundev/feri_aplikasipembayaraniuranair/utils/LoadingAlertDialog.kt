package com.munmundev.feri_aplikasipembayaraniuranair.utils

import android.content.Context
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.munmundev.feri_aplikasipembayaraniuranair.R

class LoadingAlertDialog(context: Context) {
    lateinit var dialog: AlertDialog
    val valueContext = context

    fun alertDialogLoading(){
        val view = View.inflate(valueContext, R.layout.alert_dialog_loading, null)
        val alertDialogBuilder = AlertDialog.Builder(valueContext)
        alertDialogBuilder.setView(view)

        dialog = alertDialogBuilder.create()
        dialog.setCancelable(false)
        dialog.show()
    }

    fun alertDialogCancel(){
        dialog.dismiss()
    }
}