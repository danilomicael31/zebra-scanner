package com.zebrascanner.receivebroadcast

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.zebrascanner.ManagerAppList
import com.zebrascanner.ZebraProfile


class ProfileReceiveBroadcast() : ReceiveBroadcast() {
  var managerAppList = ManagerAppList()

  override fun onReceive(context: Context?, intent: Intent?) {
    if (intent == null || context == null)
      return

    managerAppList.setContext(context)

    if (intent.hasExtra("com.symbol.datawedge.api.RESULT_GET_PROFILES_LIST")) {
      return getProfileList(intent, context)
    }

    if (intent.hasExtra("com.symbol.datawedge.api.RESULT_GET_CONFIG")) {
      val b = intent.extras

      val config: Bundle? = b?.getBundle("com.symbol.datawedge.api.RESULT_GET_CONFIG")

      if (config != null && !config.isEmpty) {
        val profileName = config.getString("PROFILE_NAME")

        if (profileName != null) {
          if (profileName == "Zebra Scanner") {
            val appProfileConfig = ZebraProfile(profileName, config)
            managerAppList.setAppZebraProfileConfig(appProfileConfig)
          }
          val profile = ZebraProfile(profileName, config)
          managerAppList.addProfile(profile)
        }

      }
    }
  }

  private fun getProfileList(intent: Intent, context: Context) {
    val profilesList = intent
      .getStringArrayExtra("com.symbol.datawedge.api.RESULT_GET_PROFILES_LIST")

    if (profilesList != null) {
      val profileNames = profilesList.map { item -> item.toString() ?: "" }
      managerAppList.setProfileNameList(ArrayList(profileNames))

      profilesList.forEach { profile ->
        getProfileAssociatedAppList(profile.toString(), context)
      }
    }
  }

  private fun getProfileAssociatedAppList(profileName: String, context: Context) {
    val pluginName = ArrayList<String>()
    val bConfig = Bundle()
    val bMain = Bundle()

    pluginName.add("BARCODE")
    pluginName.add("INTENT"); //to add more plugins

    bConfig.putString("APP_LIST", "") //empty
    bConfig.putStringArrayList("PLUGIN_NAME", pluginName)

    bMain.putString("PROFILE_NAME", profileName)
    bMain.putBundle("PLUGIN_CONFIG", bConfig);

    Log.d("MAIN", bMain.toString())

    val i = Intent()
    i.setAction("com.symbol.datawedge.api.ACTION")
    i.putExtra("com.symbol.datawedge.api.GET_CONFIG", bMain)

    context.sendBroadcast(i);
  }

}

