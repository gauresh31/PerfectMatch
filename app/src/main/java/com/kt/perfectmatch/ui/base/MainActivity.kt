package com.kt.perfectmatch.ui.base

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kt.perfectmatch.R
import com.kt.perfectmatch.data.api.APIAdapter
import com.kt.perfectmatch.db.AppDatabase
import com.kt.perfectmatch.db.MatchDao
import com.kt.perfectmatch.db.Matches
import com.kt.perfectmatch.ui.main.adapter.MatchesRecyclerAdapter
import com.kt.perfectmatch.utils.CustomProgressDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private var matchDao: MatchDao? = null
    private lateinit var recyclerView: RecyclerView
    private var dbClient: AppDatabase? = null
    private lateinit var customProgressDialog :CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        setContentView(R.layout.activity_main)

        customProgressDialog = CustomProgressDialog()
        dbClient = AppDatabase.getDatabase(this@MainActivity)

        recyclerView = findViewById(R.id.recyclerview_matches)
        recyclerView.layoutManager = LinearLayoutManager(this)

        matchDao = dbClient?.matchDao()
        updateUI()
    }

    private fun updateUI() {
        val matchesList: List<Matches?>? = matchDao?.getAll()
        if (null != matchesList && matchesList.isNotEmpty()) {
            val adapter = MatchesRecyclerAdapter(this@MainActivity, matchesList)
            recyclerView.adapter = adapter
        } else {
            callAPI()
        }
    }

    private fun callAPI() {
        launch(Dispatchers.Main) {
            // Try catch block to handle exceptions when calling the API.
            try {
                val response = APIAdapter.apiClient.getMatchesData()
                // Check if response was successful.
                if (response.isSuccessful && response.body() != null) {
                    val resultSet = response.body()?.data

                    if (resultSet != null) {
                        for (i in 0 until resultSet.count()) {
                            val find = Matches()

                            val email = resultSet[i].email ?: "N/A"
                            Log.d("email: ", email)
                            find.setEmail(email)

                            val picture = resultSet[i].picture?.large ?: "N/A"
                            find.setPicture(picture)

                            val city = resultSet[i].location?.city ?: ""
                            val state = resultSet[i].location?.state ?: ""
                            val country = resultSet[i].location?.country ?: ""
                            find.setLocation("$city, $state, $country")

                            val age = resultSet[i].dob?.age ?: ""
                            find.setAge("$age yrs")

                            val nat = resultSet[i].nat ?: ""
                            find.setNat(nat)

                            val first = resultSet[i].name?.first ?: ""
                            val last = resultSet[i].name?.last ?: ""
                            find.setFullName("$first $last")

                            saveData(find)
                        }
                        updateUI()
                    }
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

    private suspend fun saveData(find: Matches) {
        dbClient?.matchDao()?.insert(find)
    }
}