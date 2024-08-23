package com.myapp.mylibrary

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.myapp.mylibrary.repository.PointsResponse

object Utils {
    fun showErrorDialog(context: Activity, errorMessage: ErrorMessage, onRetry: () -> Unit) {
        AlertDialog.Builder(context)
            .setTitle("Error")
            .setMessage(errorMessage.message)
            .setIcon(R.drawable.error)
            .setNegativeButton("Cancel") { dialog, _ ->
                // Respond to negative button press
                dialog.dismiss()
            }
            .setPositiveButton("Retry") { dialog, _ ->
                // Respond to positive button press
                dialog.dismiss()
                onRetry()

            }
            .show()
    }

    fun showSuccessDialog(context: Activity, pointsResponse: PointsResponse) {
        AlertDialog.Builder(context)
            .setTitle("Points")
            .setMessage("Congratulations!! You completed handoff on time.")
            .setIcon(R.drawable.correct)
            .setNegativeButton("Cancel") { dialog, _ ->
                // Respond to negative button press
                dialog.dismiss()
            }
            .setPositiveButton("View points") { dialog, _ ->
                // Respond to positive button press
                dialog.dismiss()
                startPointsActivity(context, pointsResponse)
            }
            .show()
    }

    private fun startPointsActivity(
        context: Activity,
        pointsViewInfo: PointsResponse
    ) {
        val intent = Intent(context, PointsActivity::class.java)
        intent.putExtra("data", pointsViewInfo)
        context.startActivity(intent)
    }

    fun parseJson(jsonString: String): PointsResponse? {
        val gson = Gson()
        return try {
            gson.fromJson(jsonString, PointsResponse::class.java)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            null
        }
    }

    fun testJsonParsing() {

        val sampleJson = """
{
    "thresholdTime": 300000,
    "handOffTime": 189717,
    "msg": "Handoff time 189717ms is less than or equal to the threshold time 300000ms",
    "userId": "SMAR602",
    "status": true,
    "accruedPoints": "500",
    "events": [
        {
            "id": 4051,
            "orderNumber": "70908099",
            "eventName": "ARRIVED",
            "eventUserId": "SMAR602",
            "eventTs": 1717630770,
            "createTs": 1719257281,
            "kafkaTs": 1719257281
        },
        {
            "id": 4052,
            "orderNumber": "70908099",
            "eventName": "PICKED_UP",
            "eventUserId": "SMAR602",
            "eventTs": 1717630890,
            "createTs": 1719257471,
            "kafkaTs": 1719257471
        }
    ]
}
"""


        val pointsResponse = parseJson(sampleJson)

        if (pointsResponse != null) {
            println("Parsed successfully: $pointsResponse")
        } else {
            println("Failed to parse JSON.")
        }
    }


}
