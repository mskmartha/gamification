package com.myapp.mylibrary

import com.myapp.mylibrary.repository.PointsResponse
import java.io.Serializable


data class PointsViewInfo(
    var pointsResponse: PointsResponse? = null,
    var error: ErrorMessage? = null
) : Serializable

data class ErrorMessage(
    val errorCode: Int? = null,
    val message: String
) : Serializable
