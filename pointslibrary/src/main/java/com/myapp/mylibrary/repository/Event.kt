package com.myapp.mylibrary.repository

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Event(
    @SerializedName("id") val id: Int,
    @SerializedName("orderNumber") val orderNumber: String,
    @SerializedName("eventName") val eventName: String,
    @SerializedName("eventUserId") val eventUserId: String,
    @SerializedName("eventTs") val eventTs: Long,
    @SerializedName("createTs") val createTs: Long,
    @SerializedName("kafkaTs") val kafkaTs: Long
) : Serializable