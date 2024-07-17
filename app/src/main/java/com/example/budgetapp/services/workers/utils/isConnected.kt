package com.example.budgetapp.services.workers.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

fun isConnected(context: Context): Boolean {
    var connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

    when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
        else -> return false
    }
}