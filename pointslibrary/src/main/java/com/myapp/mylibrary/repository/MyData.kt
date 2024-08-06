package com.myapp.mylibrary.repository

import android.os.Parcel
import android.os.Parcelable


data class MyData(
    val name: String,
    val age: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().orEmpty(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(age)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MyData> {
        override fun createFromParcel(parcel: Parcel): MyData {
            return MyData(parcel)
        }

        override fun newArray(size: Int): Array<MyData?> {
            return arrayOfNulls(size)
        }
    }
}
