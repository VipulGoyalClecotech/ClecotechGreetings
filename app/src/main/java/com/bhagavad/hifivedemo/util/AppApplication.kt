package com.bhagavad.hifivedemo.util

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
//import com.jakewharton.threetenabp.AndroidThreeTen

class AppApplication : Application() {


    var mContext: Context? = null;

    override fun onCreate() {
        super.onCreate()
        mContext = this;
        instance = this
        // Initialize ThreeTenABP library
       // AndroidThreeTen.init(this)

    }
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this);
    }

    companion object {

        var instance: AppApplication? = null
        @Synchronized
        fun getInstanceValue(): AppApplication? {
            if (instance == null) {
                instance =
                    AppApplication();
            }
            Log.e("" ,"(instance == null) :::::  "+((instance == null)));
            return instance;
        }
    }
}
