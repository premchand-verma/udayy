package com.udayy.android.utils

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ProgressBar
import com.udayy.android.R
import kotlinx.android.synthetic.main.dialog_progess.*

/**
 * Created by premchand.
 */
class CustomProgressDialog(ctx: Activity?) {
    var dialog: Dialog? = null

    private fun createProgressDialog(ctx: Activity?) {
        dialog = Dialog(ctx!!, R.style.Theme_Transparent)
        val window = dialog!!.window
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.dialog_progess)
        dialog!!.progress!!.indeterminateDrawable.setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setCancelable(true)
    }

    fun showProgressBar() {
        dialog!!.show()
    }

    fun dismissProgressBar() {
        dialog!!.dismiss()
    }

    init {
        createProgressDialog(ctx)
    }
}