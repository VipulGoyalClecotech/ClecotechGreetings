package com.bhagavad.hifivedemo.create

import android.view.View

interface CreateNavigator {

    fun onSelectFriendsClick()
    fun onSelectGreetingClick()
    fun listItemClick(position:Int)
}