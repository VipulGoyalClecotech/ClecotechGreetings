package com.bhagavad.hifivedemo.dashboard.home.bean

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class HomeListResponse {

    data class HomeListBean(
        val data: Data,
        val error_code: Int,
        val error: Int,
        val message: String,
        val status: Int
    )

    data class Data
        (
        val Greetings: List<Greeting>
    )

    data class Greeting(

        @SerializedName("id")
        val id: String? = "",
        @SerializedName("title")
        val title: String? = "",
        @SerializedName("image")
        val image: String? = "",
        @SerializedName("message")
        val message: String? = "",
        @SerializedName("created_at")
        val created_at: String? = "",
        @SerializedName("receiver")
        val receiver: UserData,
        @SerializedName("sender")
        val sender: UserData

    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(UserData::class.java.classLoader)!!,
            parcel.readParcelable(UserData::class.java.classLoader)!!



        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(id)
            parcel.writeString(title)
            parcel.writeString(created_at)
            parcel.writeString(image)
            parcel.writeString(message)
            parcel.writeParcelable(receiver,flags)
            parcel.writeParcelable(sender,flags)

        }

        override fun describeContents(): Int {
            return 0
        }

        companion object {
            @JvmField
            val CREATOR = object : Parcelable.Creator<Greeting> {
                override fun createFromParcel(parcel: Parcel): Greeting {
                    return Greeting(parcel)
                }

                override fun newArray(size: Int): Array<Greeting?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    data class UserData
        (
        @SerializedName("id")
        val id: String? = "",
        @SerializedName("email")
        val email: String? = ""
    ): Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString()



            ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(id)
            parcel.writeString(email)


        }

        override fun describeContents(): Int {
            return 0
        }

        companion object {
            @JvmField
            val CREATOR = object : Parcelable.Creator<UserData> {
                override fun createFromParcel(parcel: Parcel): UserData {
                    return UserData(parcel)
                }

                override fun newArray(size: Int): Array<UserData?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }
}