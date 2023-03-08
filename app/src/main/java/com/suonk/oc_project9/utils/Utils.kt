package com.suonk.oc_project9.utils

import android.content.Context
import android.net.wifi.WifiManager
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

object Utils {

    fun convertDollarToEuro(dollars: Int): Int {
        return (dollars * 0.96).roundToInt()
    }

    fun convertEuroToDollar(euro: Int): Int {
        return (euro * 1.04).roundToInt()
    }

    fun convertDollarToEuro(dollars: Double): Double {
        return (dollars * 0.96)
    }

    fun convertEuroToDollar(euro: Double): Double {
        return (euro * 1.04)
    }

    fun convertSquareMetreToSquareFoot(value: Double): Double {
        return value * 10.763867361111
    }

    fun convertSquareFootToSquareMetre(value: Double): Double {
        return value / 10.763867361111
    }

    val todayDate: String
        get() {
            val dateFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd")
            return dateFormat.format(Date())
        }

    fun isInternetAvailable(context: Context): Boolean {
        val wifi = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifi.isWifiEnabled
    }
}