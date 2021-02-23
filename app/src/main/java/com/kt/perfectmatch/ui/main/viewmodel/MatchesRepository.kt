package com.kt.perfectmatch.ui.main.viewmodel

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.kt.perfectmatch.db.AppDatabase
import com.kt.perfectmatch.db.MatchDao
import com.kt.perfectmatch.db.Matches


class MatchesRepository(application: Application?){
    private var matchDao: MatchDao? = null
    private var allMatches: LiveData<List<Matches>>? = null

    init {
        matchDao = application?.let {
            AppDatabase.getDatabase(it)?.matchDao() }
        allMatches = matchDao?.getAllData()
    }


    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    fun getAllMatches(): LiveData<List<Matches>>? {
        return allMatches
    }

    // You must call this on a non-UI thread or your app will crash.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    fun insert(match: Matches) {
        matchDao?.insert(match)
    }


    private class insertAsyncTask internal constructor(dao: MatchDao?) :
        AsyncTask<Matches?, Void?, Void?>() {
        private val mAsyncTaskDao: MatchDao?

//        override fun doInBackground(vararg params: Matches): Void? {
//            mAsyncTaskDao?.insert(params[0])
//            return null
//        }

        init {
            mAsyncTaskDao = dao
        }

        override fun doInBackground(vararg match: Matches?): Void? {

            return null
        }
    }
}