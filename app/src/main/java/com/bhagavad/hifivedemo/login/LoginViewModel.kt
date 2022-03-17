package com.bhagavad.hifivedemo.login

import android.app.Application
import android.text.TextUtils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bhagavad.hifivedemo.R
import com.bhagavad.hifivedemo.base.BaseViewModel
import com.bhagavad.hifivedemo.server.*




class LoginViewModel(application: Application, serverResponse: serverResponseNavigator) :
    BaseViewModel<LoginNavigator>(application) {
    lateinit var serverResponse: serverResponseNavigator
    var mApplication: Application
   // lateinit var mRxApiCallHelper: RxAPICallHelper
   // lateinit var mApiInterface: ApiInterface
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



    fun loginClick() {
        if (TextUtils.isEmpty(emailStr.trim())) {
            dataResult.value = getStringfromVM(R.string.please_enter_email)
            return;
        }

        else {
            dataResult.value = "";
            return
        }
    }

}