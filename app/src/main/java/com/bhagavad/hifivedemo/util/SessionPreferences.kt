package com.bhagavad.hifivedemo.util

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.bhagavad.hifivedemo.splash.bean.UserDataResponse
import com.google.gson.Gson


class SessionPreferences {
    companion object {
        val PREFS_NAME = "HiFive_Demo"
        public var userDataResult: UserDataResponse.UserDataBean? = null

        init {

        }

        fun saveStringPref(
            context: Context,
            key: String?,
            value: String?
        ) {
            val settings: SharedPreferences
            val editor: SharedPreferences.Editor
            settings = context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            editor = settings.edit()
            editor.putString(key, value)
            editor.commit()
        }






        fun saveLongPref(
            context: Context,
            key: String?,
            value: Long
        ) {
            val settings: SharedPreferences
            val editor: SharedPreferences.Editor
            settings = context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            editor = settings.edit()
            editor.putLong(key, value)
            editor.commit()
        }


        fun saveBooleanPref(
            context: Context,
            key: String?,
            value: Boolean
        ) {
            val settings: SharedPreferences
            val editor: SharedPreferences.Editor
            settings = context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            editor = settings.edit()
            editor.putBoolean(key, value)
            editor.commit()
        }

        fun loadStringPref(context: Context, key: String?): String? {
            val settings: SharedPreferences
            settings = context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            return settings.getString(key, "")
        }

        fun loadBooleanPref(
            context: Context,
            key: String?
        ): Boolean {
            val settings: SharedPreferences
            settings = context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            return settings.getBoolean(key, false)
        }


        fun saveUserDataPref(context: Context,userData: UserDataResponse.UserDataBean) {
            var gson = Gson();
            var str = gson.toJson(userData)
            val settings: SharedPreferences
            val editor: SharedPreferences.Editor
            settings = context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            editor = settings.edit()
            editor.putString("userDataValue", str)
            editor.commit()

        }
        fun loadUserDataPref(context: Context): UserDataResponse.UserDataBean? {
            var gson = Gson();
            val settings: SharedPreferences
            settings = context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            var json =settings.getString("userDataValue", "")
            if (json != null && !TextUtils.isEmpty(json)) {
                userDataResult = gson.fromJson(json, UserDataResponse.UserDataBean::class.java)
                return userDataResult
            } else {
                return null;
            }

        }

        fun clearAllPreferenceData(context: Context) {
            val settings: SharedPreferences
            settings = context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            val editor = settings.edit()
            editor.clear()
            editor.commit()
        }
    }

}