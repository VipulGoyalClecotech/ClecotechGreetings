package com.bhagavad.hifivedemo.server


import android.util.Log
import com.bhagavad.hifivedemo.util.NetworkUtils

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject

/**
 * Common API Call Helper to make API call.
 */
class RxAPICallHelper {

    private val TAG = RxAPICallHelper::class.java.simpleName
    private var serverCallback: serverResponseNavigator? = null

    private var disposable: CompositeDisposable? = null


    fun setDisposable(disposable: CompositeDisposable) {
        this.disposable = disposable
    }

    fun call(
        observable: Observable<*>?, eventType: String
        , serverCallback: serverResponseNavigator
    ) {
        this.serverCallback = serverCallback

        if (!NetworkUtils.isConnected()) {
            serverCallback!!.noNetwork()
        } else {

            requireNotNull(observable) { throw IllegalArgumentException("Callback must not be null.") }

            requireNotNull(serverCallback == null) {
                throw IllegalArgumentException("Callback must not be null.")
            }

            disposable!!.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    //Rxjava->   rxlive cycle trilo  ,,,,,, bindtolifecycle,,,,,,,
                    //life cycle observable
//                    Log.e("RxApiCallHelper", "t.toString() ::: " + t);

                    //  Log.e("RxApiCallHelper", "t.toString() ::: " + t.toString());
                    val jsonObject = JSONObject(t.toString())
                    val status = jsonObject.optString("status", "")
                    val message = jsonObject.optString("message", "")
                    val error_code = jsonObject.optString("error", "")

                    // Log.e("api_status--->", "----->::: " +status);
                    //status =0 and if not verified email
                    if (error_code.equals("461", ignoreCase = true)) {
                        SESSION_TIME_OUT_MESSAGE = message
                        serverCallback!!.onSessionExpire()

                    } else if ( error_code.equals("463", ignoreCase = true)) {
                        serverCallback!!.onResponse(t.toString(), eventType)

                    } else if (error_code.equals(
                            "467",
                            ignoreCase = true
                        )
                    ) {    //call on response method so easily redirect on otp screen as per error_code
                        serverCallback!!.onAppHardUpdate()

                    } else if (error_code.equals(
                            "466",
                            ignoreCase = true
                        )
                    ) {    //call on response method so easily redirect on otp screen as per error_code
                        serverCallback!!.onMinorUpdate()

                    }
                    // else if (status.equals("1", ignoreCase = true)) {
                    else if ((status.equals("1", ignoreCase = true)) ||
                        (status.equals("OK"))
                    ) {

                        Log.e(TAG, "status>>>>> " + status + "  eventType::::: " + eventType);
                        serverCallback!!.onResponse(eventType, t.toString())
                    } else {
                        serverCallback!!.onRequestFailed(eventType, message)
                    }
                }, { throwable ->
                    if (throwable != null) {
                        serverCallback!!.onRequestRetry()
                        //rxAPICallback.onFailed(throwable);
                    } else {
                        serverCallback!!.onRequestRetry()
                        //rxAPICallback.onFailed(new Exception("Error: Something went wrong in api call."));
                    }
                }
                ))
        }
    }

    companion object {
        var SESSION_TIME_OUT_MESSAGE = ""
    }
}
