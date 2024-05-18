package com.zebrascanner

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.modules.core.DeviceEventManagerModule


class ScannerReceiveBroadcast(reactContext: ReactApplicationContext?) : BroadcastReceiver() {
    private val _reactContext: ReactApplicationContext = reactContext!!
    var id: String? = null
    var action: String? = null
    var isRegistered: Boolean = false

    override fun onReceive(context: Context?, intent: Intent?) {
        val intentAction = intent?.action

        if (intentAction == action.toString()) {
            try {
                displayScanResult(intent)
            } catch (e: Exception) {
                //  Catch if the UI does not exist when we receive the broadcast
            }
        }
    }

  fun register(reactContext: ReactApplicationContext, filter: IntentFilter?): Intent? {
      if(!isRegistered) {
        isRegistered = true
//        return reactContext.registerReceiver(this, filter)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
          reactContext.registerReceiver(this, filter, Context.RECEIVER_EXPORTED)
        }else {
          reactContext.registerReceiver(this, filter)
        }
      }

    return null
  }

  fun unregister(context: Context): Boolean {
    return (isRegistered && _unregister(context))
  }

  private fun _unregister(context: Context): Boolean {
    context.unregisterReceiver(this)
    isRegistered = false
    return true
  }


  private fun displayScanResult(initiatingIntent: Intent) {
//        val decodedSource = initiatingIntent.getStringExtra("com.symbol.datawedge.source")
        val decodedData = initiatingIntent.getStringExtra("com.symbol.datawedge.data_string")
//        val decodedLabelType = initiatingIntent.getStringExtra("com.symbol.datawedge.label_type")
//        val event = Arguments.createMap()
        val key = "onScanner-$id"
//        Log.d("SCANNER", decodedData.toString())

        _reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                .emit(key, decodedData.toString())
    }
}
