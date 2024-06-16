package com.zebrascanner

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.facebook.react.bridge.ReactApplicationContext


class Scanner(
  val profileName: String,
  val intentAction: String,
  val reactContext: ReactApplicationContext
) {
  val profileEnabled = true
  val configMode = "CREATE_PROFILE"
  val pluginNameIntent = "INTENT"
  val pluginNameKeystroke = "KEYSTROKE"
  val resetConfig = false
  val intentDelivery = 2
  val intentOutputEnable = true
  val keyStrokeEnable = false
  var bundleProfile = Bundle()
  val bundlePluginConfig = ArrayList<Bundle>()

  init {
    bundleProfile = _createProfile()
  }

  fun createProfile() {
    setIntentConfig()
    setKeyStrokeConfig()
    setAppList()

    bundleProfile.putParcelableArrayList("PLUGIN_CONFIG", bundlePluginConfig);
    sendBroadCastIntent()
  }

  private fun sendResultRequest() {
    val intent = Intent()
    intent.putExtra("SEND_RESULT", "true");
    intent.putExtra("COMMAND_IDENTIFIER", "123456789");
  }

  private fun sendBroadCastIntent() {
    val intent = Intent()
    intent.setAction("com.symbol.datawedge.api.ACTION")

    intent.putExtra("com.symbol.datawedge.api.SET_CONFIG", bundleProfile)
    intent.putExtra("SEND_RESULT", "true");
    intent.putExtra("COMMAND_IDENTIFIER", "123456789");

    reactContext.sendBroadcast(intent)
  }

//  private fun sendBroadCastIntent(bundleParam: Bundle) {
//    val intent = Intent()
//    intent.setAction("com.symbol.datawedge.api.ACTION")
//
//    bundleProfile.putBundle("PLUGIN_CONFIG", bundleParam)
//    intent.putExtra("com.symbol.datawedge.api.SET_CONFIG", bundleProfile)
//    intent.putExtra("SEND_RESULT", "true");
//    intent.putExtra("COMMAND_IDENTIFIER", "123456789");
//
//    reactContext.sendBroadcast(intent)
//  }

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
  }

  private fun setKeyStrokeConfig() {
    val bundleParams = Bundle()

    bundleParams.putString("keystroke_output_enabled", keyStrokeEnable.toString())
    val bundle = createBundleConfig(pluginNameKeystroke, bundleParams)

    bundlePluginConfig.add(bundle);
  }

  private fun setIntentConfig() {
    val bundleParams = Bundle()

    bundleParams.putString("intent_output_enabled", intentOutputEnable.toString())
    bundleParams.putString("intent_action", intentAction)
    bundleParams.putInt("intent_delivery", intentDelivery)

    val bundle = createBundleConfig(pluginNameIntent, bundleParams)

    bundlePluginConfig.add(bundle)
  }

  private fun createBundleConfig(pluginName: String, bundleParam: Bundle): Bundle {
    val bundleConfig = Bundle()

    bundleConfig.putString("PLUGIN_NAME", pluginName)
    bundleConfig.putString("RESET_CONFIG", resetConfig.toString())
    bundleConfig.putBundle("PARAM_LIST", bundleParam)

    return bundleConfig
  }
}
