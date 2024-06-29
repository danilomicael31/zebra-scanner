package com.zebrascanner

import android.content.Context
import android.os.Bundle

class ManagerAppList {
  private var rightProfile: ZebraProfile? = null
  private var wrongProfile: ZebraProfile? = null
  private var profileName: String? = null
  private var packageName: String? = null
  private var rightProfileName = ""
  private val intentEmitter = IntentEmitter()

  fun checkActiveProfile(profileName: String) {
    if(profileName != this.profileName && profileName != rightProfileName) {
      intentEmitter.requestProfileConfig(profileName)
      intentEmitter.requestProfileConfig(this.profileName!!)
    }
  }

  fun addProfile(profile: ZebraProfile) {
    if(profile.profileName == this.profileName)
      this.rightProfile = profile

    if (profile.profileName != this.profileName)
        this.wrongProfile = profile

    if(wrongProfile != null && rightProfile != null) {
      switchAppAssociation()
    }
  }

  fun setProfileName(profileName: String) {
    this.profileName = profileName
  }

  fun setPackageName(packageName: String) {
    this.packageName = packageName
  }

  fun setContext(context: Context) {
    this.intentEmitter.setIntentContext(context)
  }

  private fun switchAppAssociation() {
      removeAppFromWrongProfile()
      addAppOnRightProfile()
  }

  private fun addAppOnRightProfile() {
    val rightProfileBundle = rightProfile!!.config
    val appList = getAppList(rightProfileBundle)

    val newAppList = appList.map { item ->
      createAppListBundle(item)
    }.toMutableList()

    val bundleApp = Bundle()
    bundleApp.putString("PACKAGE_NAME", this.packageName)
    bundleApp.putStringArray("ACTIVITY_LIST", arrayOf("*"))
    newAppList.add(bundleApp)

    rightProfileBundle.putParcelableArray("APP_LIST", newAppList.toTypedArray())
    intentEmitter.overwriteProfile(rightProfileBundle)
  }

  private fun removeAppFromWrongProfile() {
    if (wrongProfile != null) {
      val wrongProfileBundle = wrongProfile!!.config
      val appList = getAppList(wrongProfileBundle)

      val newAppList = appList.map { item ->
        createAppListBundle(item)
      }.toMutableList()

      wrongProfileBundle.putParcelableArray("APP_LIST", newAppList.toTypedArray())
      intentEmitter.overwriteProfile(wrongProfileBundle)
    }
  }

  private fun createAppListBundle(bundle: Bundle): Bundle {
    val packageName = bundle.getString("PACKAGE_NAME")
    val activities = bundle.getStringArrayList("ACTIVITY_LIST")
      ?.toTypedArray()

    bundle.putString("PACKAGE_NAME", packageName.toString())
    bundle.putStringArray("ACTIVITY_LIST", activities)
    return bundle
  }

  private fun getAppList(bundle: Bundle): ArrayList<Bundle> {
    val appList = bundle.getParcelableArrayList<Bundle>("APP_LIST")

    val newAppList = appList?.filter { item ->
      val packageName = item.getString("PACKAGE_NAME").toString()
      packageName != this.packageName
    } ?: return ArrayList<Bundle>()

    return ArrayList(newAppList)
  }
}
