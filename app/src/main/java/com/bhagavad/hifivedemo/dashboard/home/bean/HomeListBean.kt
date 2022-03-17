package com.bhagavad.hifivedemo.dashboard.home.bean


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class HomeListBean(
    @SerializedName("name")
    val name: String? = "",
    @SerializedName("id")
    val id: String? = "",
    @SerializedName("status")
    val status: String? = "",
    @SerializedName("checked")
    var checked: Int? = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(id)
        parcel.writeString(status)
         checked?.let { parcel.writeInt(it) }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<HomeListBean> {
            override fun createFromParcel(parcel: Parcel): HomeListBean {
                return HomeListBean(parcel)
            }

            override fun newArray(size: Int): Array<HomeListBean?> {
                return arrayOfNulls(size)
            }
        }
    }
}