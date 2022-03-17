package com.bhagavad.hifivedemo.dashboard.home

import android.app.Application
import com.bhagavad.hifivedemo.base.BaseViewModel
import com.bhagavad.hifivedemo.server.*
import com.bhagavad.hifivedemo.util.AppConstants
import org.json.JSONArray


class HomeListFragmentViewModel (application: Application, serverResponse: serverResponseNavigator) : BaseViewModel<HomeListFragmentNavigator>(application)
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



    fun listItemClick(position:Int)
{
    getNavigator().listItemClick(position)
}



    fun onNoRecordClick() {
        getNavigator().onNoRecordClick()
    }
    fun onFilterClick() {
        getNavigator().onFilterClick()
    }

    fun onCreateClick() {
        getNavigator().onCreateClick()
    }
    /**** listing with filter api calling ***/
    fun listingWithFilterAPI(page:String, senderId:String,receiverId:String) {
        mRxApiCallHelper = RxAPICallHelper()
        mRxApiCallHelper.setDisposable(mCompositeDisposable)
        mApiInterface = ApiBuilderSingleton.getInstance()!!
        serverResponse.showLoaderOnRequest(true)
        var jsonObj = JsonElementUtil.getJsonObject(
            "page",
            page, "sent",
            senderId, "received",
            receiverId
        )
        mRxApiCallHelper.call(
            mApiInterface.postRequest(AppConstants.GREETING_LISTING, jsonObj!!),
            "greetingListingResponse",
            serverResponse
        )

    }

}