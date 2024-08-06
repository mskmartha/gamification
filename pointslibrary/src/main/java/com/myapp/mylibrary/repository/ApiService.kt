package com.myapp.mylibrary.repository

import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Path

interface ApiService {

    @GET("gamification/handoff-time/{orderNumber}/v1")
    suspend fun getPointsData(@Path("orderNumber") orderNumber: String): Response<PointsResponse>

}
