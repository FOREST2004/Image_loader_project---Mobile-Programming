// app/kotlin+java/com.example.imageloaderproject/LoadImageTask.kt

package com.example.imageloaderproject

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.widget.ImageView
import android.widget.TextView
import java.io.InputStream
import java.net.URL

class LoadImageTask(
    private val imageView: ImageView,
    private val statusTextView: TextView
) : AsyncTask<String, Void, Bitmap?>() {

    override fun doInBackground(vararg params: String): Bitmap? {
        return try {
            val url = URL(params[0])
            val connection = url.openConnection()
            connection.connectTimeout = 5000
            val inputStream = connection.getInputStream()
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun onPostExecute(result: Bitmap?) {
        if (result != null) {
            imageView.setImageBitmap(result)
            statusTextView.text = ""
        } else {
            statusTextView.text = "Failed to load image"
        }
    }
}