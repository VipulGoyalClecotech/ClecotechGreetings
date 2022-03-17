package com.bhagavad.hifivedemo.server

interface serverResponseNavigator {

    fun showLoaderOnRequest(isShowLoader: Boolean);
    fun onResponse(eventType:String , response:String)
    fun onRequestFailed(eventType:String , response:String)
    fun onRequestRetry();
    fun onSessionExpire()
    fun onMinorUpdate()
    fun onAppHardUpdate()
    fun noNetwork()
}