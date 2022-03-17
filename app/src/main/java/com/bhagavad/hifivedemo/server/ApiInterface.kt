package com.bhagavad.hifivedemo.server

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

public interface ApiInterface {


    @GET
    public fun getRequest(
        @Url url: String
    ): Observable<JsonObject>

   /* @GET
    public fun getRequest(
        @Url url: String, @Header("studentid") studentid: String
    ): Observable<JsonObject>*/

    @POST
    public fun postRequest(
        @Url url: String, @Body body: JsonObject
    ): Observable<JsonObject>

    @DELETE
    public fun deleteRequest(
        @Url url: String, @Body body: JsonObject
    ): Observable<JsonObject>


    /* @POST
     public fun postRequest(
         @Url url: String, @Body body: JsonObject, @Header("studentid") studentid: String
     ): Observable<JsonObject>*/


//    @Multipart
//    @POST
//    abstract fun chatImageUploadAPI(
//        @Url url: String,
//        @Body file : RequestBody ,
//        @Header("studentid") studentid: String
//
//    ): Observable<JsonElement>


    @POST
    abstract fun chatImageUploadAPI(
        @Url url: String,
        @Body file : RequestBody ,
        @Header("studentid") studentid: String

    ):Observable<JsonElement>



}