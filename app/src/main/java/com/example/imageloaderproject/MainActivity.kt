// app/kotlin+java/com.example.imageloaderproject/MainActivity.kt

package com.example.imageloaderproject

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.net.NetworkCapabilities
import android.os.AsyncTask
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var urlEditText: EditText
    private lateinit var loadButton: Button
    private lateinit var imageView: ImageView
    private lateinit var statusTextView: TextView
    private lateinit var connectivityReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_main)

            // Initialize views
            urlEditText = findViewById(R.id.urlEditText)
            loadButton = findViewById(R.id.loadButton)
            imageView = findViewById(R.id.imageView)
            statusTextView = findViewById(R.id.statusTextView)

            // Set click listener for load button
            loadButton.setOnClickListener {
                val url = urlEditText.text.toString()
                loadImage(url)
            }

            // Check initial connectivity
            checkConnectivity()

            // Register connectivity receiver
            connectivityReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    checkConnectivity()
                }
            }

            // Start the service
            val serviceIntent = Intent(this, ImageLoaderService::class.java)
            startService(serviceIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(connectivityReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        try {
            unregisterReceiver(connectivityReceiver)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadImage(url: String) {
        if (!isConnected()) {
            return
        }

        statusTextView.text = "Loading..."
        LoadImageTask(imageView, statusTextView).execute(url)
    }

    private fun checkConnectivity() {
        val isConnected = isConnected()
        loadButton.isEnabled = isConnected
        if (!isConnected) {
            statusTextView.text = "No internet connection"
        } else {
            statusTextView.text = ""
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo
            @Suppress("DEPRECATION")
            networkInfo?.isConnected == true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    // AsyncTask cho tải ảnh
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
}