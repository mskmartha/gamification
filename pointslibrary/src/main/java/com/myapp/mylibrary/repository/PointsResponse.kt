package com.myapp.mylibrary.repository

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PointsResponse(
    @SerializedName("thresholdTime") val thresholdTime: Int? = null,
    @SerializedName("handOffTime") val handOffTime: Int? = null,
    @SerializedName("msg") val msg: String? = null,
    @SerializedName("userId") val userId: String? = null,
    @SerializedName("status") val status: Boolean,
    @SerializedName("accruedPoints") val accruedPoints: String? = null,
    @SerializedName("events") val events: List<Event>? = null
) : Serializable
