package com.udayy.android.ui.download.viewmodel

import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udayy.android.App
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class DownloadViewModel : ViewModel() {
    val TAG = "DownloadViewModel"

    val downloadSuccessLiveData = MutableLiveData<String>()

    fun downloadFile(downloadUrl: String, location: String, name: String) {
        val splitUrl = downloadUrl?.split("/")
        val fileExtension = (splitUrl?.get(splitUrl.size - 1).toString()).split(".")[1]
        val downloadFileName = "$name.$fileExtension"
        var fileStorage: File? = null
        var outputFile: File? = null
        viewModelScope.launch(IO) {
            val url = URL(downloadUrl)
            val c: HttpURLConnection =
                url.openConnection() as HttpURLConnection
            c.setRequestMethod("GET")
            c.connect()
            //Get File if SD card is present
            if (isSDCardPresent()) {
                fileStorage = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        .toString() + "/$location"
                )
            } else Toast.makeText(
                App.instance,
                "Oops!! There is no SD Card.",
                Toast.LENGTH_SHORT
            ).show()

            //If File is not present create directory
            if (!fileStorage!!.exists()) {
                fileStorage!!.mkdir()
                Log.e(TAG, "Directory Created.")
            }

            outputFile = File(fileStorage, downloadFileName)

            //Create New File if not present
            if (!outputFile!!.exists()) {
                outputFile!!.createNewFile()
                Log.e(TAG, "File Created")
            }

            val fos = FileOutputStream(outputFile)
            val `is`: InputStream = c.inputStream
            val buffer = ByteArray(1024)
            var len1 = 0

            while (`is`.read(buffer).also { len1 = it } != -1) {
                fos.write(buffer, 0, len1) //Write new file
            }

            //Close all connection after doing task
            fos.close()
            `is`.close()

            downloadSuccessLiveData.postValue(outputFile!!.path.toString())
        }
    }

    //Check If SD Card is present or not method
    private fun isSDCardPresent(): Boolean {
        return Environment.getExternalStorageState().equals(
            Environment.MEDIA_MOUNTED
        )
    }
}