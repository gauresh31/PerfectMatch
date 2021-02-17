package com.kt.perfectmatch.ui.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.kt.perfectmatch.R
import com.kt.perfectmatch.data.api.APIAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        callAPI()
    }

    private fun callAPI() {
        launch(Dispatchers.Main) {
            // Try catch block to handle exceptions when calling the API.
            try {
                val response = APIAdapter.apiClient.getMatchesData()
                // Check if response was successful.
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body()

                    // Check for null safety.
                    val jsonResp = data
                } else {
                    // Show API error.
                    Toast.makeText(
                        this@MainActivity,
                        "Error Occurred: ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                // Show API error. This is the error raised by the client.
                Toast.makeText(
                    this@MainActivity,
                    "Error Occurred: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}