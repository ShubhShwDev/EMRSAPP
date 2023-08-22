package com.maxi.dogapi.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Base64

import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*


object Utils {

    fun hasInternetConnection(context: Context?): Boolean {
        if (context == null)
            return false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    fun getBase64Image(bitmap: Bitmap): String? {
        try {
            val buffer: ByteBuffer = ByteBuffer.allocate(
                bitmap.rowBytes *
                        bitmap.height
            )
            bitmap.copyPixelsToBuffer(buffer)
            val data: ByteArray = buffer.array()
            return Base64.encodeToString(data, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
    fun checkCurrentDate():String{
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        return currentDate
//        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
//
//        var calender:Calendar=Calendar.getInstance()
//       val currentTime:Date= calender.time

    }

    fun checkCurrentTime():String {
        val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        return currentTime
    }
    }