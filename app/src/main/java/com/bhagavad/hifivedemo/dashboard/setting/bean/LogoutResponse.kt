package com.bhagavad.hifivedemo.dashboard.setting.bean

class LogoutResponse {

    data class LogoutBean(
   //     val data: DataBean,
        val errorcode: Int,
        val error: Int,
        val message: String,
        val status: Int
    )

   // data class DataBean(
     //   val user: UserDataResponse.UserDataBean

    //)
}