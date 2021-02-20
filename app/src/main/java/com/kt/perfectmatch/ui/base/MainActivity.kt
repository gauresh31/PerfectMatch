package com.kt.perfectmatch.ui.base

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kt.perfectmatch.R
import com.kt.perfectmatch.data.api.APIAdapter
import com.kt.perfectmatch.db.AppDatabase
import com.kt.perfectmatch.db.MatchDao
import com.kt.perfectmatch.db.Matches
import com.kt.perfectmatch.ui.main.adapter.MatchesRecyclerAdapter
import com.kt.perfectmatch.ui.main.viewmodel.MatchesViewModel
import com.kt.perfectmatch.utils.CustomProgressDialog
import com.kt.perfectmatch.utils.PreferenceUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private lateinit var matchesViewModel: MatchesViewModel
    private lateinit var matchesAdapter: MatchesRecyclerAdapter
    private var matchDao: MatchDao? = null
    private lateinit var recyclerView: RecyclerView
    private var dbClient: AppDatabase? = null
    private lateinit var customProgressDialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        setContentView(R.layout.activity_main)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        init()
        checkData()
    }

    private fun init() {
        customProgressDialog = CustomProgressDialog()
        dbClient = AppDatabase.getDatabase(this@MainActivity)

        recyclerView = findViewById(R.id.recyclerview_matches)
        matchesAdapter = MatchesRecyclerAdapter(this@MainActivity)
        recyclerView.adapter = matchesAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        matchesViewModel = ViewModelProvider(this).get(MatchesViewModel::class.java)
        matchesViewModel.getAllMatches()?.observe(this@MainActivity,
            { _matches ->
                CoroutineScope(Dispatchers.Main).launch {
                    matchesAdapter.updateData(requireNotNull(_matches))
                }
            })

        matchDao = dbClient?.matchDao()
    }

    private fun checkData() {
        val matchesList: List<Matches?>? = matchDao?.getAll()
        if (null != matchesList && matchesList.isNotEmpty()) {
            matchesAdapter.updateData(matchesList as List<Matches>)
        } else {
            callAPI(1)
        }
    }

    private fun callAPI(initVal: Int) {
        var progress : ProgressDialog = ProgressDialog(this@MainActivity)
        progress.setMessage("Downloading data......")
        progress.setCancelable(false)
        progress.show()
        launch(Dispatchers.Main) {
            // Try catch block to handle exceptions when calling the API.
            try {
                val response = APIAdapter.apiClient.getMatchesData()
                // Check if response was successful.
                if (response.isSuccessful && response.body() != null) {
                    progress.dismiss()
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
                            find.setAge("$age yrs, ")

                            val nat = resultSet[i].nat ?: ""
                            find.setNat(nat)

                            val first = resultSet[i].name?.first ?: ""
                            val last = resultSet[i].name?.last ?: ""
                            find.setFullName("$first $last")

                            saveData(find)
                        }
                        if(initVal==1){
                            checkData()
                        }
                    }
                } else {
                    progress.dismiss()
                    // Show API error.
                    Toast.makeText(
                        this@MainActivity,
                        "Error Occurred: ${response.message()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                progress.dismiss()
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
//        matchesViewModel.insert(find)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_download, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        R.id.download -> {
            var i: Int =
                PreferenceUtils.getIntPreference(this@MainActivity, getString(R.string.str_count))
            if (i < 3) {
                PreferenceUtils.setIntPreference(this@MainActivity, getString(R.string.str_count), ++i)
                callAPI(0)
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "Download Limit Reached",
                    Toast.LENGTH_LONG
                ).show()
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}