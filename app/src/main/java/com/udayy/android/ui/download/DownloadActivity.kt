package com.udayy.android.ui.download

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.udayy.android.BuildConfig
import com.udayy.android.R
import com.udayy.android.interfaces.OnLocationFileSelected
import com.udayy.android.ui.download.viewmodel.DownloadViewModel
import com.udayy.android.utils.CustomInputDialog
import com.udayy.android.utils.CustomProgressDialog
import kotlinx.android.synthetic.main.activity_download.*
import java.io.File


/**
 * Created by premchand.
 */
class DownloadActivity : AppCompatActivity() {
    private lateinit var downloadViewModel: DownloadViewModel
    private lateinit var url: String
    private val PERMISSIONS_REQUEST_STORAGE: Int = 101
    private lateinit var cProgressDialog: CustomProgressDialog

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

        downloadViewModel = ViewModelProvider(this).get(DownloadViewModel::class.java)
        registerObservers()

        btn_download.setOnClickListener { startDownload() }
    }

    private fun registerObservers() {
        downloadViewModel.downloadSuccessLiveData.removeObservers(this)
        downloadViewModel.downloadSuccessLiveData.observe(
            this,
            Observer { downloadFileName -> showDownloadFile(downloadFileName) })
    }

    private fun showDownloadFile(downloadFileName: String) {
        cProgressDialog.dismissProgressBar()
        setupNotificationForDownloadSuccess(downloadFileName);
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun startDownload() {
        url = et_uri.text?.trim().toString()
        if (Patterns.WEB_URL.matcher(url).matches()) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    ), PERMISSIONS_REQUEST_STORAGE
                )
            } else {
                getStorageAndFileName()
            }
        } else {
            Toast.makeText(this, "Please enter valid url.", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_STORAGE) {
            getStorageAndFileName()
        } else {
            Toast.makeText(this, "Please provide the storage permission.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun getStorageAndFileName() {
        CustomInputDialog(this, object : OnLocationFileSelected {
            override fun onLocationFileSelected(location: String, name: String) {
                cProgressDialog = CustomProgressDialog(this@DownloadActivity)
                cProgressDialog.showProgressBar()
                downloadViewModel.downloadFile(url, location, name)
            }
        }).showDialog()
    }

    private fun setupNotificationForDownloadSuccess(filePath: String) {
        val CHANNEL_ID = BuildConfig.APPLICATION_ID
        val CHANNEL_NAME = getString(R.string.app_name)
        val file = File(filePath)
        val intent = Intent(this, DownloadActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        var notificationChannel: NotificationChannel? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(true)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            val manager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(notificationChannel)
        }
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setColor(ContextCompat.getColor(this, R.color.white))
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        this.resources,
                        R.mipmap.ic_launcher_round
                    )
                )
                .setContentTitle(CHANNEL_NAME)
                .setContentText("${file.name} is downloaded.")
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText("${file.name} is downloaded.")
                )
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
        val notificationManager =
            this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(100, notificationBuilder.build())

        Log.e("File ", file.path)
    }

}