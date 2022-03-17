package com.bhagavad.hifivedemo.server

import com.google.gson.JsonObject



object JsonElementUtil {

    fun getJsonObject(vararg nameValuePair: String): JsonObject? {
        var HashMap: JsonObject? = null

        if (null != nameValuePair && nameValuePair.size % 2 == 0) {

            HashMap = JsonObject()

            var i = 0

            while (i < nameValuePair.size) {
                HashMap.addProperty(nameValuePair[i], nameValuePair[i + 1])
                i += 2
            }

        }

        return HashMap
    }
}
