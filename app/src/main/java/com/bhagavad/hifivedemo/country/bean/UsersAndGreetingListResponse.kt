package com.bhagavad.hifivedemo.country.bean

import android.os.Parcelable
import com.bhagavad.hifivedemo.splash.bean.LoginResponse
import com.bhagavad.hifivedemo.splash.bean.UserDataResponse
import kotlinx.android.parcel.Parcelize

 public class UsersAndGreetingListResponse  {
    data class ListDataBean(
        val message: String,
        val errorcode: Int,

        val status: Int,
        val error: Int,
        val data: DataBean
    )

     data class DataBean(
         val user: List<UserData>,
         val Greeting: List<GreetingData>

     )
    @Parcelize
    data class UserData(
        var id: String,
        var name: String,
        var employee_id: String,

        var isSelect : Boolean
    ) : Parcelable

     @Parcelize
     data class GreetingData(
         var id: String,
         var name: String,
         var image: String,

         var isSelect : Boolean
     ) : Parcelable

}