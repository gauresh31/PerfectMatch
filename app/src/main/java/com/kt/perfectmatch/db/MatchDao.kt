package com.kt.perfectmatch.db

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
public interface MatchDao {
    @Query("SELECT * FROM matches")
    fun getAll(): List<Matches?>?

    @Insert
    fun insert(matches: Matches)

    @Update
    fun update(matches: Matches?)

    @Query("DELETE FROM matches")
    suspend fun deleteAll()

    @get:Query("SELECT * FROM matches")
    val getAllData: LiveData<List<Matches>>
}