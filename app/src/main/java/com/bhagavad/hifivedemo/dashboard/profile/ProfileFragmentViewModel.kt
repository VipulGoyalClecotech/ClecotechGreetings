package com.bhagavad.hifivedemo.dashboard.profile

import android.app.Application
import com.bhagavad.hifivedemo.base.BaseViewModel
import com.bhagavad.hifivedemo.server.*



class ProfileFragmentViewModel (application: Application, serverResponse: serverResponseNavigator) : BaseViewModel<ProfileFragmentNavigator>(application)
{
    var mApplication: Application
    lateinit var serverResponse: serverResponseNavigator



    init
    {
        mApplication = application
        this.serverResponse = serverResponse
    }


    fun onCreateClick() {
        getNavigator().onCreateClick()
    }

}