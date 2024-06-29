package com.zebrascanner

import android.content.Context
import android.content.Intent
import android.os.Bundle

class IntentEmitter() {
  var context: Context? = null

  fun setIntentContext(context: Context) {
    this.context = context
  }

  fun requestActiveProfile() {
    val i = Intent()
    i.setAction("com.symbol.datawedge.api.ACTION")
    i.putExtra("com.symbol.datawedge.api.GET_ACTIVE_PROFILE", "")
    context?.sendBroadcast(i)
  }

  fun requestProfileConfig(profileName: String) {
    val pluginName =
      arrayListOf(
        "BARCODE",
        "INTENT",
        "KEYSTROKE",
        "IP",
        "MSR",
        "RFID",
        "SERIAL",
        "VOICE",
        "WORKFLOW",
        "DCP",
        "EKB",
        "BDF",
        "ADF",
        "TOKENS",
      )
    val bConfig = Bundle()
    val bMain = Bundle()

    bConfig.putStringArrayList("PLUGIN_NAME", pluginName)

    bMain.putString("PROFILE_NAME", profileName)
    bMain.putBundle("PLUGIN_CONFIG", bConfig)
    bMain.putString("APP_LIST", "")

    val i = Intent()
    i.setAction("com.symbol.datawedge.api.ACTION")
    i.putExtra("com.symbol.datawedge.api.GET_CONFIG", bMain)

    context?.sendBroadcast(i)
  }

  fun overwriteProfile(bundle: Bundle) {
    val i = Intent()
    bundle.putString("CONFIG_MODE", "OVERWRITE")

    i.setAction("com.symbol.datawedge.api.ACTION")
    i.putExtra("com.symbol.datawedge.api.SET_CONFIG", bundle)

    this.context?.sendBroadcast(i)
  }
}
