package com.bhagavad.hifivedemo.toolbar

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel


class ToolBarViewModel(application: Application): AndroidViewModel(application) {

    var mToolBarNavigator: ToolBarNavigator ? = null;

    fun setToolBarNavigator(toolbarNavigate : ToolBarNavigator)
    {
        mToolBarNavigator = toolbarNavigate

    }


    fun backClick() {
        Log.e("BackClickkk", "ToolBar BackClickkk")
        mToolBarNavigator!!.backClick()

    }



}