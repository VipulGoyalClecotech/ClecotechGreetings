package com.bhagavad.hifivedemo.splash.bean

class LoginResponse {

    data class LoginBean(
        val data: DataBean,
        val errorcode: Int,
        val error: Int,
      //  val error_line: Int,
       // val type: Int,
        val message: String,
        val status: Int
    )

    data class DataBean(
        val user: UserDataResponse.UserDataBean

    )
}