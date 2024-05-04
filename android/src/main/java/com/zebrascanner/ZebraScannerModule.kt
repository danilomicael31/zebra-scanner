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
    if (_id == null || _reactContext == null) return
    myBroadcastReceiver.id = _id
    myBroadcastReceiver.action = _intentAction
    myBroadcastReceiver.register(_reactContext, _filter)
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

    if (_reactContext != null) {
      val scanner = Scanner(profileName, intentAction, _reactContext)
      scanner.createProfile()
    }
  }

  companion object {
    const val NAME = "ZebraScanner"
  }

  override fun onHostResume() {
    onRegisterReceiver()
  }

  override fun onHostPause() {
    if (_reactContext != null) myBroadcastReceiver.unregister(_reactContext)
  }

  override fun onHostDestroy() {
    if (_reactContext != null) myBroadcastReceiver.unregister(_reactContext)
  }
}
