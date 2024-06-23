package com.zebrascanner.receivebroadcast

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.zebrascanner.ManagerAppList
import com.zebrascanner.ZebraProfile

class ResultReceiveBroadcast(private val managerAppList: ManagerAppList) : ReceiveBroadcast() {

  override fun onReceive(context: Context?, intent: Intent?) {
    if (intent == null || context == null) return

    managerAppList.setContext(context)

    if (intent.hasExtra("RESULT_LIST")) {
      val resultList: ArrayList<Bundle> = intent.getParcelableArrayListExtra("RESULT_LIST")!!

      if (isAppAlreadyAssociated(resultList)) {
        return requestProfileList(context)
      }
    }

    if (intent.hasExtra("com.symbol.datawedge.api.RESULT_GET_PROFILES_LIST")) {
      return getProfileList(intent, context)
    }

    if (intent.hasExtra("com.symbol.datawedge.api.RESULT_GET_CONFIG")) {
      return getProfileConfig(intent)
    }
  }

  private fun isAppAlreadyAssociated(results: List<Bundle>): Boolean {
    return results.any {
      it.containsKey("RESULT_CODE") && it["RESULT_CODE"] == "APP_ALREADY_ASSOCIATED"
    }
  }

  private fun requestProfileList(context: Context) {
    val i = Intent()
    i.setAction("com.symbol.datawedge.api.ACTION")
    i.putExtra("com.symbol.datawedge.api.GET_PROFILES_LIST", "")
    context.sendBroadcast(i)
  }

  private fun getProfileConfig(intent: Intent) {
    val config = intent.getBundleExtra("com.symbol.datawedge.api.RESULT_GET_CONFIG")
    val profileName = config?.getString("PROFILE_NAME")

    if (profileName != null) {
      val profile = ZebraProfile(profileName, config)
      managerAppList.addProfile(profile)
    }
  }

  private fun getProfileList(intent: Intent, context: Context) {
    val profilesList =
        intent.getStringArrayExtra("com.symbol.datawedge.api.RESULT_GET_PROFILES_LIST")

    if (profilesList != null) {
      val profileNames = profilesList.map { item -> item.toString() ?: "" }
      managerAppList.setProfileNameList(ArrayList(profileNames))

      profilesList.forEach { profile -> getProfileAssociatedAppList(profile.toString(), context) }
    }
  }

  private fun getProfileAssociatedAppList(profileName: String, context: Context) {
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

    context.sendBroadcast(i)
  }
}
