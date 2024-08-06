package com.myapp.mylibrary.repository

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PointsResponse(
    @SerializedName("thresholdTime") val thresholdTime: Int,
    @SerializedName("handOffTime") val handOffTime: Int,
    @SerializedName("msg") val msg: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("status") val status: Boolean,
    @SerializedName("accruedPoints") val accruedPoints: String,
    @SerializedName("events") val events: List<Event>
) : Serializable
