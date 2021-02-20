package com.kt.perfectmatch.ui.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.kt.perfectmatch.db.AppDatabase
import com.kt.perfectmatch.db.MatchDao
import com.kt.perfectmatch.db.Matches

class MatchesViewModel(application: Application) : AndroidViewModel(application) {
    private var mRepository: MatchesRepository = MatchesRepository(application)
    private val matchesDao: MatchDao? = AppDatabase.getDatabase(application)?.matchDao()
    private var matchesList: LiveData<List<Matches>>? = mRepository.getAllMatches()


    fun getAllMatches(): LiveData<List<Matches>>? {
        return matchesList
    }

    fun insert(match: Matches) {
        mRepository.insert(match)
    }

    fun getCount() {
        matchesDao?.getDataCount()
    }

    suspend fun update(matches: Matches) {
        matchesDao?.update(matches)
    }

    suspend fun deleteAll() {
        matchesDao?.deleteAll()
    }
}