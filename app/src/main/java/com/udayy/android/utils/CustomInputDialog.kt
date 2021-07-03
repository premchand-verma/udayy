package com.udayy.android.utils

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.udayy.android.interfaces.OnLocationFileSelected
import kotlinx.android.synthetic.main.dialog_location_name.*
import com.udayy.android.R

/**
 * Created by premchand.
 */
class CustomInputDialog(ctx: Activity?, selectedListener: OnLocationFileSelected) {

    init {
        createProgressDialog(ctx, selectedListener)
    }

    var dialog: Dialog? = null

    private fun createProgressDialog(ctx: Activity?, selectedListener: OnLocationFileSelected) {
        dialog = Dialog(ctx!!, R.style.Theme_Transparent)
        val window = dialog!!.window
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.dialog_location_name)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setCancelable(true)

        dialog!!.btn_ok.setOnClickListener{
            if(dialog!!.et_file.text.toString().trim().isEmpty()){
                Toast.makeText(ctx, "Please enter folder.", Toast.LENGTH_SHORT).show()
            }else if(dialog!!.et_file_name.text.toString().trim().isEmpty()){
                Toast.makeText(ctx, "Please enter file name.", Toast.LENGTH_SHORT).show()
            }else{
                selectedListener.onLocationFileSelected(dialog!!.et_file.text.toString(), dialog!!.et_file_name.text.toString())
                dismissDialog()
            }
        }
    }

    fun showDialog() {
        dialog!!.show()
    }

    fun dismissDialog() {
        dialog!!.dismiss()
    }
}