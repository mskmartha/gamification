package com.myapp.samplepoc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
// import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
//import com.myapp.mylibrary.PointsApiManager
//import com.myapp.mylibrary.Utils
import com.myapp.samplepoc.ui.theme.SamplePOCTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        PointsApiManager.initialize(this@MainActivity)
        // enableEdgeToEdge()
        setContent {

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
//                                PointsApiManager.launchViewPoints("12349000131")
                            }

                          //  Utils.testJsonParsing()
                        }) {
                            Text(text = "View Points")
                        }
                    }
                }
            }

    }
    override fun onDestroy() {
        super.onDestroy()
//        PointsApiManager.destroy()
    }
}

