package com.gurpreetsk.internal

object Utils {
    fun getiOSStrings(
        keyValueMap: Map<ResourceKey, ResourceValue>
    ): StringBuilder {
        return StringBuilder()
            .apply {
                keyValueMap.forEach { key, value -> append(getiOSStringResource(key, value)) }
            }
    }

    fun getAndroidStrings(
        keyValueMap: Map<ResourceKey, ResourceValue>
    ): StringBuilder = StringBuilder().apply {
        append("<resources>\n")
        keyValueMap.forEach { key, value -> append(getAndroidStringResource(key, value)) }
        return append("</resources>\n")
    }

    private fun getAndroidStringResource(
        key: ResourceKey,
        value: ResourceValue
    ): String = "  <string name=\"${key.key.trim().replace(" ", "_")}\">\"${value.value.trim()}\"</string>\n"

    private fun getiOSStringResource(
        key: ResourceKey,
        value: ResourceValue
    ): String = "${key.key.replace(" ", "_")}=${value.value.trim().replace("%s", "%@")}\n"
}