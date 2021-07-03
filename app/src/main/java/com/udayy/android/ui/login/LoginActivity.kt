package com.udayy.android.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.udayy.android.R
import com.udayy.android.ui.download.DownloadActivity
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Created by premchand.
 */
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login.setOnClickListener { login() }
    }

    private fun login() {
        val phoneNo = et_phone_no.text?.toString()?.trim()
        val otp = et_otp.text?.toString()?.trim()
        if (phoneNo.isNullOrEmpty() || phoneNo.length < 10){
            Toast.makeText(this, "Please enter a valid mobile no.", Toast.LENGTH_SHORT).show()
        }else if(otp.isNullOrEmpty() || !otp.equals("1234")){
            Toast.makeText(this, "Please enter otp: 1234", Toast.LENGTH_SHORT).show()
        }else{
            startActivity(Intent(this, DownloadActivity::class.java))
        }
    }
}