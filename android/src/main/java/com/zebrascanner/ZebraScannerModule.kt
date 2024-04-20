package com.zebrascanner

import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.facebook.react.bridge.LifecycleEventListener
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class ZebraScannerModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext), LifecycleEventListener {
  private val _reactContext: ReactApplicationContext? = reactContext
  private var myBroadcastReceiver = ScannerReceiveBroadcast(reactContext)
  private val _filter: IntentFilter = IntentFilter()
  private var _id: String? = null
  private var _intentAction: String? = null

  init {
    _filter.addCategory(Intent.CATEGORY_DEFAULT)
    _reactContext?.addLifecycleEventListener(this)
  }

  override fun getName(): String {
    return NAME
  }

  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  fun multiply(a: Double, b: Double, promise: Promise) {
    Log.d("TEST", "???")
    promise.resolve(a * b)
  }

  private fun onRegisterReceiver() {
    if (_id == null) return
    myBroadcastReceiver.id = _id
    myBroadcastReceiver.action = _intentAction
    _reactContext?.registerReceiver(myBroadcastReceiver, _filter)
  }

  @ReactMethod
  fun onInit(id: String) {
    _id = id
    onRegisterReceiver()
  }

  @ReactMethod
  fun createProfile(profileName: String, intentAction: String) {
    _intentAction = intentAction
    _filter.addAction(intentAction)
    onRegisterReceiver()

    val scanner = Scanner(profileName, intentAction, _reactContext?.packageName.toString())
    val bundle = scanner.createIntentProfile()

    val i = Intent()
    i.setAction("com.symbol.datawedge.api.ACTION")
    i.putExtra("com.symbol.datawedge.api.SET_CONFIG", bundle)

    _reactContext?.sendBroadcast(i)
  }

  companion object {
    const val NAME = "ZebraScanner"
  }

  override fun onHostResume() {
    onRegisterReceiver()
  }

  override fun onHostPause() {
    _reactContext?.unregisterReceiver(myBroadcastReceiver)
  }

  override fun onHostDestroy() {
    _reactContext?.unregisterReceiver(myBroadcastReceiver)
  }
}
