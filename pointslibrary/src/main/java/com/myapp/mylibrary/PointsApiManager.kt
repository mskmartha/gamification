package com.myapp.mylibrary

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.myapp.mylibrary.repository.PointsResponse
import com.myapp.mylibrary.repository.ResponseStatusFalseException
import com.myapp.mylibrary.repository.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class PointsApiManager {

    companion object {


        private var coroutineJob: Job? = null
        private var context: Activity? = null

        fun initialize(ctx: Activity) {
            context = ctx
        }

        suspend fun launchViewPoints(orderId: String) {

            if (context == null || context?.isDestroyed == true) {
                Toast.makeText(
                    context,
                    "Not initialized",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            // Check if a coroutine job is already active and running
            if (coroutineJob?.isActive == true) {
                Log.d("PointsApiManager", "An API call is already in progress.")
                Toast.makeText(
                    context,
                    "View points request is already in progress",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            // Cancel any existing job and start a new one
            coroutineJob?.cancelAndJoin()

            coroutineJob = CoroutineScope(Dispatchers.IO).launch {
                try {
                    Log.d("PointsApiManager", "Starting API call for orderId: $orderId")
                    // Call the API function
                    makeApiCall(context!!, orderId)

                } catch (e: Exception) {
                    Log.e("PointsApiManager", "Error during API call: ${e.localizedMessage}", e)
                } finally {
                    // Reset the coroutine job to null after completion
                    coroutineJob = null
                }
            }
        }

        private suspend fun makeApiCall(context: Activity, orderId: String) {
            val pointsInfo = PointsViewInfo()
            val maxRetries = 5  // Maximum number of retry attempts

            try {
                fetchPointsData(orderId)
                    .retryWhen { cause, attempt ->
                        // Retry on exceptions or when the status is false
                        if (attempt < maxRetries) {
                            val shouldRetry =
                                cause is IOException || cause is HttpException || cause is ResponseStatusFalseException
                            if (shouldRetry) {
                                Log.w(
                                    "ApiService",
                                    "Retry attempt $attempt due to error: ${cause.message}"
                                )
                                delay(10000 * (attempt + 1)) // Exponential backoff
                                true // Retry the flow
                            } else {
                                false // Don't retry further
                            }
                        } else {
                            false // Max retries reached
                        }
                    }
                    .flowOn(Dispatchers.IO)
                    .collect { response ->
                        if (response.isSuccessful) {
                            val pointsResponse = response.body()
                            if (pointsResponse != null) {
                                if (pointsResponse.status) {
                                    // Successful response with true status
                                    pointsInfo.pointsResponse = pointsResponse
                                } else {
                                    // Throw custom exception to trigger retry
                                    throw ResponseStatusFalseException("Status is false. Retrying...")
                                }
                            } else {
                                pointsInfo.error = ErrorMessage(
                                    errorCode = null,
                                    message = "No data available. The response body is empty."
                                )
                            }
                        } else {
                            // Log error with additional information
                            Log.e(
                                "ApiService",
                                "Failed with status code: ${response.code()} - ${response.message()}"
                            )
                            pointsInfo.error = ErrorMessage(
                                errorCode = response.code(),
                                message = response.message()
                                    ?: "An error occurred while fetching the points data."
                            )
                        }
                    }

            } catch (e: ResponseStatusFalseException) {
                Log.e("ApiService", "Max retries reached with false status.")
                pointsInfo.error = ErrorMessage(
                    errorCode = null,
                    message = "Max retries reached without successful status."
                )
            } catch (e: Exception) {
                // Handle any other exceptions
                Log.e("ApiService", "Error fetching points data: ${e.localizedMessage}", e)
                pointsInfo.error = ErrorMessage(
                    errorCode = null,
                    message = e.localizedMessage
                        ?: "An unexpected error occurred while fetching the points data."
                )
            } finally {
                // Start the PointsActivity if no error found
                withContext(Dispatchers.Main) {
                    if (pointsInfo.pointsResponse != null) {
                        Utils.showSuccessDialog(context, pointsInfo.pointsResponse!!)
                    } else {
                        // Double handled just in case
                        Utils.showErrorDialog(
                            context, pointsInfo.error ?: ErrorMessage(
                                errorCode = null,
                                message = "An unexpected error occurred while fetching the points data."
                            )
                        ) {
                            // user pressed retry call the API again and start the process
                            CoroutineScope(Dispatchers.IO).launch {
                                makeApiCall(context, orderId)
                            }
                        }
                    }
                }
            }
        }

        private fun fetchPointsData(orderId: String): Flow<Response<PointsResponse>> = flow {
            emit(RetrofitClient.apiService.getPointsData(orderId))
        }

        fun destroy() {
            context = null
        }

    }
}
