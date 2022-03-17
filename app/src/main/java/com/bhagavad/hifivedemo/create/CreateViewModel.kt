package com.bhagavad.hifivedemo.create

import android.app.Application
import android.text.TextUtils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bhagavad.hifivedemo.R
import com.bhagavad.hifivedemo.base.BaseViewModel
import com.bhagavad.hifivedemo.server.*
import com.bhagavad.hifivedemo.util.AppConstants
import org.json.JSONArray


class CreateViewModel(application: Application, serverResponse: serverResponseNavigator) :
    BaseViewModel<CreateNavigator>(application) {
    lateinit var serverResponse: serverResponseNavigator
    var mApplication: Application
    lateinit var mRxApiCallHelper: RxAPICallHelper
    lateinit var mApiInterface: ApiInterface
    var messageStr: String = ""
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

    fun selectFriendsClick() {

        getNavigator().onSelectFriendsClick()

    }
    fun selectGreetingClick() {

        getNavigator().onSelectGreetingClick()

    }
    fun listItemClick(position:Int)
    {
        getNavigator().listItemClick(position)
    }

    fun createClick() {
        if (TextUtils.isEmpty(messageStr.trim())) {
            dataResult.value = getStringfromVM(R.string.please_enter_message)
            return;
        }

        else {
            dataResult.value = "";
            return
        }

    }


    /******** API implementation *********/

    /**** user listing api calling ***/
    fun userListingAPI() {
        mRxApiCallHelper = RxAPICallHelper()
        mRxApiCallHelper.setDisposable(mCompositeDisposable)
        mApiInterface = ApiBuilderSingleton.getInstance()!!
        serverResponse.showLoaderOnRequest(true)
        var jsonObj = JsonElementUtil.getJsonObject()
        mRxApiCallHelper.call(
            mApiInterface.getRequest(AppConstants.View_GREETINGS),
            "getUserListingResponse",
            serverResponse
        )

    }




    /**** create api calling ***/
    fun createGreetingAPI(member_ids: String, greeting_id:String) {
        mRxApiCallHelper = RxAPICallHelper()
        mRxApiCallHelper.setDisposable(mCompositeDisposable)
        mApiInterface = ApiBuilderSingleton.getInstance()!!
        serverResponse.showLoaderOnRequest(true)
        var jsonObj = JsonElementUtil.getJsonObject(
            "member_ids",
            member_ids, "greeting_id",
            greeting_id, "message",
            messageStr
        )
     //   jsonObj!!.add("member_ids",member_ids)



        mRxApiCallHelper.call(
            mApiInterface.postRequest(AppConstants.CREATE_GREETINGS, jsonObj!!),
            "createGreetingResponse",
            serverResponse
        )

    }

}