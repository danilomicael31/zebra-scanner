package com.zebrascanner

import android.content.Intent
import android.os.Bundle
import com.facebook.react.bridge.ReactApplicationContext

class Scanner(
    val profileName: String,
    val intentAction: String,
    val reactContext: ReactApplicationContext
) {
  val profileEnabled = true
  val configMode = "CREATE_IF_NOT_EXIST"
  val pluginNameIntent = "INTENT"
  val pluginNameKeystroke = "KEYSTROKE"
  val resetConfig = false
  val intentDelivery = 2
  val intentOutputEnable = true
  val keyStrokeEnable = false
  var bundleProfile = Bundle()

  init {
    bundleProfile = _createProfile()
  }

  fun createProfile() {
    setIntentConfig()
    setKeyStrokeConfig()
    setAppList()
  }

  private fun sendBroadCastIntent(bundleParam: Bundle) {
    val intent = Intent()
    intent.setAction("com.symbol.datawedge.api.ACTION")

    bundleProfile.putBundle("PLUGIN_CONFIG", bundleParam)
    intent.putExtra("com.symbol.datawedge.api.SET_CONFIG", bundleProfile)

    reactContext.sendBroadcast(intent)
  }

  private fun _createProfile(): Bundle {
    val bundleMain = Bundle()
    bundleMain.putString("PROFILE_NAME", profileName)
    bundleMain.putString("PROFILE_ENABLED", profileEnabled.toString())
    bundleMain.putString("CONFIG_MODE", configMode)

    return bundleMain
  }

  private fun setAppList() {
    val bundleApp = Bundle()
    val packageName = reactContext.packageName

    bundleApp.putString("PACKAGE_NAME", packageName)
    bundleApp.putStringArray("ACTIVITY_LIST", arrayOf("*"))

    bundleProfile.putParcelableArray("APP_LIST", arrayOf<Bundle>(bundleApp))

    val i = Intent()
    i.setAction("com.symbol.datawedge.api.ACTION")
    i.putExtra("com.symbol.datawedge.api.SET_CONFIG", bundleProfile)

    reactContext.sendBroadcast(i)
  }

  private fun setKeyStrokeConfig() {
    val bundleParams = Bundle()

    bundleParams.putString("keystroke_output_enabled", keyStrokeEnable.toString())
    val bundleConfig = createBundleConfig(pluginNameKeystroke, bundleParams)
    sendBroadCastIntent(bundleConfig)
  }
  private fun setIntentConfig() {
    val bundleParams = Bundle()

    bundleParams.putString("intent_output_enabled", intentOutputEnable.toString())
    bundleParams.putString("intent_action", intentAction)
    bundleParams.putInt("intent_delivery", intentDelivery)

    val bundleConfig = createBundleConfig(pluginNameIntent, bundleParams)
    sendBroadCastIntent(bundleConfig)
  }

  private fun createBundleConfig(pluginName: String, bundleParam: Bundle): Bundle {
    val bundleConfig = Bundle()

    bundleConfig.putString("PLUGIN_NAME", pluginName)
    bundleConfig.putString("RESET_CONFIG", resetConfig.toString())
    bundleConfig.putBundle("PARAM_LIST", bundleParam)

    return bundleConfig
  }
}
