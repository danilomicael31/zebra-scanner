package com.zebrascanner

data class ScannerConfig(
    val profileName: String,
    val profileEnabled: Boolean? = true, // Default value of true
    val configMode: String,
    val pluginConfig: PluginConfig,
)
