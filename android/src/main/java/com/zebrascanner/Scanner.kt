package com.zebrascanner

import android.content.Intent
import android.os.Bundle
import com.facebook.react.bridge.ReactApplicationContext

class Scanner(
    private val profileName: String,
    private val intentAction: String,
    private val keyStrokeEnable: Boolean = false,
    private val reactContext: ReactApplicationContext
) {
  private val profileEnabled = true
  private val configMode = "CREATE_IF_NOT_EXIST"
  private val pluginNameIntent = "INTENT"
  private val pluginNameKeystroke = "KEYSTROKE"
  private val resetConfig = false
  private val intentDelivery = 2
  private val intentOutputEnable = true
  private var bundleProfile = Bundle()
  private val bundlePluginConfig = ArrayList<Bundle>()

  init {
    bundleProfile = _createProfile()
  }

  fun createProfile() {
    setIntentConfig()
    setKeyStrokeConfig()
    setAppList()

    bundleProfile.putParcelableArrayList("PLUGIN_CONFIG", bundlePluginConfig)
    sendBroadCastIntent()
  }

  private fun sendBroadCastIntent() {
    val intent = Intent()
    intent.setAction("com.symbol.datawedge.api.ACTION")

    intent.putExtra("com.symbol.datawedge.api.SET_CONFIG", bundleProfile)
    intent.putExtra("SEND_RESULT", "COMPLETE_RESULT")
    intent.putExtra("COMMAND_IDENTIFIER", "PROFILE_CREATED")

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

    bundleProfile.putParcelableArray("APP_LIST", arrayOf(bundleApp))
  }

  private fun setKeyStrokeConfig() {
    val bundleParams = Bundle()

    bundleParams.putString("keystroke_output_enabled", keyStrokeEnable.toString())
    val bundle = createBundleConfig(pluginNameKeystroke, bundleParams)

    bundlePluginConfig.add(bundle)
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
