package com.zebrascanner

import android.content.Intent
import android.content.IntentFilter
import com.facebook.react.bridge.LifecycleEventListener
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.zebrascanner.receivebroadcast.ResultReceiveBroadcast
import com.zebrascanner.receivebroadcast.ScannerReceiveBroadcast

class ZebraScannerModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext), LifecycleEventListener {
  private val _reactContext: ReactApplicationContext = reactContext
  private val managerAppList = ManagerAppList()

  private var myBroadcastReceiver = ScannerReceiveBroadcast(reactContext)
  private var resultReceiveBroadcast = ResultReceiveBroadcast(managerAppList)

  private val _filter: IntentFilter = IntentFilter()
  private var _id: String? = null
  private var _intentAction: String? = null

  init {
    _filter.addCategory(Intent.CATEGORY_DEFAULT)
    _reactContext.addLifecycleEventListener(this)

    resultReceiveBroadcast.register(_reactContext, getResultFilter())
  }

  override fun getName(): String {
    return NAME
  }

  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  fun multiply(a: Double, b: Double, promise: Promise) {
    promise.resolve(a * b)
  }

  private fun onRegisterReceiver() {
    if (_id == null) return

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
  fun createProfile(profileName: String, intentAction: String, keystrokeEnabled: Boolean = false) {
    _intentAction = intentAction
    _filter.addAction(intentAction)
    onRegisterReceiver()

    val scanner = Scanner(profileName, intentAction, keystrokeEnabled, _reactContext)
    managerAppList.setProfileName(profileName)
    managerAppList.setPackageName(_reactContext.packageName)

    scanner.createProfile()
  }

  companion object {
    const val NAME = "ZebraScanner"
  }

  override fun onHostResume() {
    onRegisterReceiver()
  }

  override fun onHostPause() {
    myBroadcastReceiver.unregister(_reactContext)
  }

  override fun onHostDestroy() {
    myBroadcastReceiver.unregister(_reactContext)
  }

  private fun getResultFilter(): IntentFilter {
    val filter = IntentFilter()
    filter.addCategory(Intent.CATEGORY_DEFAULT)
    filter.addAction("com.symbol.datawedge.api.RESULT_ACTION")
    return filter
  }
}
