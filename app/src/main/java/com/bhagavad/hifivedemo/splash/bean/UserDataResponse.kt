package com.bhagavad.hifivedemo.splash.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class UserDataResponse {

    @Parcelize
    data class UserDataBean(
        val id: String,
        val email: String,
        val name: String,
        val confirm_token: String,
        val authentication_token: String,
        val organization_id: String,
        val updated_at: String,
        val employee_id: String,
        val created_at: String

    ): Parcelable


}