package com.bhagavad.hifivedemo.create.bean

class CreateResponse {

    data class CreateBean(

        val errorcode: Int,
        val error: Int,

        val message: String,
        val status: Int
    )


}