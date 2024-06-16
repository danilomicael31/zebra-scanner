package com.zebrascanner.receivebroadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build

abstract class ReceiveBroadcast(): BroadcastReceiver() {
  var isRegistered: Boolean = false

  fun register(context: Context, filter: IntentFilter?): Intent? {
    if(!isRegistered) {
      isRegistered = true

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.registerReceiver(this, filter, Context.RECEIVER_EXPORTED)
      }else {
        context.registerReceiver(this, filter)
      }
    }

    return null
  }

  fun unregister(context: Context): Boolean {
    return (isRegistered && _unregister(context))
  }

  private fun _unregister(context: Context): Boolean {
    context.unregisterReceiver(this)
    isRegistered = false
    return true
  }

}
