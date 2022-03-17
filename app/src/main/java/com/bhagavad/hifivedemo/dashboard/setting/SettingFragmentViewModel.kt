package com.bhagavad.hifivedemo.dashboard.setting

import android.app.Application
import com.bhagavad.hifivedemo.base.BaseViewModel
import com.bhagavad.hifivedemo.server.*
import com.bhagavad.hifivedemo.util.AppConstants


class SettingFragmentViewModel (application: Application, serverResponse: serverResponseNavigator) : BaseViewModel<SettingFragmentNavigator>(application)
{
    var mApplication: Application
    lateinit var serverResponse: serverResponseNavigator
    lateinit var mRxApiCallHelper: RxAPICallHelper
    lateinit var mApiInterface: ApiInterface



    init
    {
        mApplication = application
        this.serverResponse = serverResponse
    }


    fun onCreateClick() {
        getNavigator().onCreateClick()
    }

    fun onLogoutClick() {
        getNavigator().onLogoutClick()
    }

    /******** API implementation *********/


    /**** login api calling ***/
    fun logoutAPI() {
        mRxApiCallHelper = RxAPICallHelper()
        mRxApiCallHelper.setDisposable(mCompositeDisposable)
        mApiInterface = ApiBuilderSingleton.getInstance()!!
        serverResponse.showLoaderOnRequest(true)
        var jsonObj = JsonElementUtil.getJsonObject()
        mRxApiCallHelper.call(
            mApiInterface.deleteRequest(AppConstants.LOGOUT_API, jsonObj!!),
            "getLogoutResponse",
            serverResponse
        )

    }

}