package com.zebrascanner

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import com.facebook.react.bridge.LifecycleEventListener
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.module.annotations.ReactModule

class ZebraScannerModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext), LifecycleEventListener {
  private val _reactContext: ReactApplicationContext? = reactContext
  private var myBroadcastReceiver = ScannerReceiveBroadcast(reactContext)
  private val _filter: IntentFilter =  IntentFilter()
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
    if(_id == null) return
    myBroadcastReceiver.id = _id
    myBroadcastReceiver.action = _intentAction
    _reactContext?.registerReceiver(myBroadcastReceiver, _filter, Context.RECEIVER_NOT_EXPORTED)
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

    val config = generateConfigScannerData(profileName, intentAction)
    val bundle = createIntentProfile(config)

    val i = Intent()
    i.setAction("com.symbol.datawedge.api.ACTION")
    i.putExtra("com.symbol.datawedge.api.SET_CONFIG", bundle)

    _reactContext?.sendBroadcast(i)
  }

 fun createIntentProfile (config: ScannerConfig): Bundle {
    val bMain = Bundle()
    val bConfig = Bundle()
    val bundleApp1 = Bundle()
    val bParams = Bundle()

    bMain.putString("PROFILE_NAME", config.profileName)
    bMain.putString("PROFILE_ENABLED", config.profileEnabled.toString())
    bMain.putString("CONFIG_MODE", config.configMode)

    bParams.putString("intent_output_enabled", config.pluginConfig.outputConfig.intentOutputEnable.toString())
    bParams.putString("intent_action", config.pluginConfig.outputConfig.intentAction)
    bParams.putInt("intent_delivery", config.pluginConfig.outputConfig.intentDelivery)

    bConfig.putString("PLUGIN_NAME", config.pluginConfig.pluginName)
    bConfig.putString("RESET_CONFIG", config.pluginConfig.resetConfig.toString())
    bConfig.putBundle("PARAM_LIST", bParams)

    bundleApp1.putString("PACKAGE_NAME", _reactContext?.packageName)
    bundleApp1.putStringArray("ACTIVITY_LIST", arrayOf("*"))

    bMain.putBundle("PLUGIN_CONFIG", bConfig)
    bMain.putParcelableArray("APP_LIST", arrayOf(bundleApp1))

    return bMain
  }

  fun generateConfigScannerData (profileName: String, intentAction: String): ScannerConfig {
      val profileEnabled = true
      val configMode = "CREATE_IF_NOT_EXIST"
      val pluginName = "INTENT"
      val resetConfig = false
      val intentDelivery = 2
      val intentOutputEnable = true

      val outputConfig = OutputConfig(intentOutputEnable, intentAction, intentDelivery)
      val pluginConfig = PluginConfig(resetConfig, pluginName, outputConfig)
      return ScannerConfig(profileName, profileEnabled, configMode, pluginConfig)
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
