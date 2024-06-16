package com.zebrascanner.receivebroadcast

import android.content.Context
import android.content.Intent
import android.util.Log
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.zebrascanner.Profile
import com.zebrascanner.receivebroadcast.ReceiveBroadcast


class ScannerReceiveBroadcast(reactContext: ReactApplicationContext?) : ReceiveBroadcast() {
  private val _reactContext: ReactApplicationContext = reactContext!!
  var id: String? = null
  var action: String? = null
val profile = Profile()
  override fun onReceive(context: Context?, intent: Intent?) {
    val intentAction = intent?.action

    profile.sendBroadcast(_reactContext)
    if (intentAction == action.toString()) {
      try {
        displayScanResult(intent)
      } catch (e: Exception) {
        //  Catch if the UI does not exist when we receive the broadcast
      }
    }
  }

  private fun displayScanResult(initiatingIntent: Intent) {
    val decodedData = initiatingIntent.getStringExtra("com.symbol.datawedge.data_string")
    val key = "onScanner-$id"

    _reactContext
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
      .emit(key, decodedData.toString())
  }
}
