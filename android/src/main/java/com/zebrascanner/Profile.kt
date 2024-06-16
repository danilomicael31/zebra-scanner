package com.zebrascanner

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.facebook.react.bridge.ReactContext
import com.zebrascanner.receivebroadcast.ResultReceiveBroadcast


class Profile() {
//  private val filter: IntentFilter = IntentFilter()
  private var profileReceiveBroadcast = ResultReceiveBroadcast()
//  private var _reactContext = null

  init {
//    registerReceiver()
//    filter.addCategory(Intent.CATEGORY_DEFAULT)
//    reactContext?.addLifecycleEventListener(this)
  }


  fun getFilter(): IntentFilter {
    val filter = IntentFilter()
    filter.addAction("com.symbol.datawedge.api.RESULT_ACTION") //RESULT_ACTION
    filter.addCategory(Intent.CATEGORY_DEFAULT)
    return filter

//    filter.addAction("com.symbol.datawedge.api.RESULT_GET_PROFILES_LIST")
//    profileReceiveBroadcast.register(_reactContext, filter)
  }

  fun sendBroadcast(_reactContext: ReactContext) {
    val i = Intent()
//    i.addCategory(Intent.CATEGORY_DEFAULT)
    i.setAction("com.symbol.datawedge.api.ACTION")
    i.putExtra("com.symbol.datawedge.api.GET_PROFILES_LIST", "")
    _reactContext.sendBroadcast(i)
  }
}
