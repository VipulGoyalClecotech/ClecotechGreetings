/*
 *  Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://mindorks.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

@file:Suppress("DEPRECATION")

package com.bhagavad.hifivedemo.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager


/**
 */

object NetworkUtils : BroadcastReceiver() {


//    @Suppress("DEPRECATION")
//    fun isNetworkConnected(): Boolean {
//        val cm = AppApplication.getInstanceValue()!!.getApplicationContext()
//            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val activeNetwork = cm.activeNetworkInfo
//
//        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
//    }

    var connectivityReceiverListener: ConnectivityReceiverListener? = null
    private val TAG = NetworkUtils::class.java!!.getSimpleName()
    fun NetworkUtils() {

    }

    @Suppress("DEPRECATION")
    override fun onReceive(context: Context, arg1: Intent) {


        /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {*/
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting

        if (connectivityReceiverListener != null) {
            connectivityReceiverListener!!.onNetworkConnectionChanged(isConnected)
        }
        /* }
        },3500);*/

    }

    fun isConnected(): Boolean {
        val cm = AppApplication.getInstanceValue()!!.getApplicationContext()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }


    interface ConnectivityReceiverListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }

}// This class is not publicly instantiable
