package com.zebrascanner

import android.os.Bundle

class Scanner(val profileName: String, val intentAction: String, val packageName: String) {
  val profileEnabled = true
  val configMode = "CREATE_IF_NOT_EXIST"
  val pluginName = "INTENT"
  val resetConfig = false
  val intentDelivery = 2
  val intentOutputEnable = true

  fun createIntentProfile(): Bundle {
    val bMain = Bundle()
    val bConfig = Bundle()
    val bundleApp1 = Bundle()
    val bParams = Bundle()

    bMain.putString("PROFILE_NAME", profileName)
    bMain.putString("PROFILE_ENABLED", profileEnabled.toString())
    bMain.putString("CONFIG_MODE", configMode)

    bParams.putString("intent_output_enabled", intentOutputEnable.toString())
    bParams.putString("intent_action", intentAction)
    bParams.putInt("intent_delivery", intentDelivery)

    bConfig.putString("PLUGIN_NAME", pluginName)
    bConfig.putString("RESET_CONFIG", resetConfig.toString())
    bConfig.putBundle("PARAM_LIST", bParams)

    bundleApp1.putString("PACKAGE_NAME", packageName)
    bundleApp1.putStringArray("ACTIVITY_LIST", arrayOf("*"))

    bMain.putBundle("PLUGIN_CONFIG", bConfig)
    bMain.putParcelableArray("APP_LIST", arrayOf(bundleApp1))

    return bMain
  }
}
