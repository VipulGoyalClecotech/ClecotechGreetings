package com.bhagavad.hifivedemo.splash

import android.app.Application
import android.text.TextUtils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bhagavad.hifivedemo.R
import com.bhagavad.hifivedemo.base.BaseViewModel
import com.bhagavad.hifivedemo.server.*
import com.bhagavad.hifivedemo.util.AppConstants


class SplashViewModel(application: Application, serverResponse: serverResponseNavigator) :
    BaseViewModel<SplashNavigator>(application) {
    lateinit var serverResponse: serverResponseNavigator
    var mApplication: Application
    lateinit var mRxApiCallHelper: RxAPICallHelper
    lateinit var mApiInterface: ApiInterface
   var emailStr: String = ""

    init {
        mApplication = application
        this.serverResponse = serverResponse
    }

    /**
     * To pass  result to activity
     */
    private val dataResult = MutableLiveData<String>()

    /*
  * get  result live data
  * */
    fun geResult(): LiveData<String> = dataResult


    /******** API implementation *********/


    /**** login api calling ***/
    fun loginAPI(code:String) {
        mRxApiCallHelper = RxAPICallHelper()
        mRxApiCallHelper.setDisposable(mCompositeDisposable)
        mApiInterface = ApiBuilderSingleton.getInstance()!!
        serverResponse.showLoaderOnRequest(true)
        var jsonObj = JsonElementUtil.getJsonObject(
            "confirm_token",
            code
        )
        mRxApiCallHelper.call(
            mApiInterface.postRequest(AppConstants.LOGIN_API, jsonObj!!),
            "getLoginResponse",
            serverResponse
        )

    }


}