package com.kt.perfectmatch.ui.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.kt.perfectmatch.db.AppDatabase
import com.kt.perfectmatch.db.MatchDao
import com.kt.perfectmatch.db.Matches

class MatchesViewModel(application: Application) : AndroidViewModel(application) {
    private val matchesDao: MatchDao? = AppDatabase.getDatabase(application)?.matchDao()
    private val matchesList: LiveData<List<Matches>>? = matchesDao?.getAllData

    suspend fun insert(matches: Matches) {
        matchesDao?.insert(matches)
    }

    suspend fun update(matches: Matches) {
        matchesDao?.update(matches)
    }

    suspend fun deleteAll() {
        matchesDao?.deleteAll()
    }
}