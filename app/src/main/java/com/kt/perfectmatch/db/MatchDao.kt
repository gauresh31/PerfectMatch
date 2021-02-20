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

    @Query("SELECT * FROM matches")
    fun getAllData(): LiveData<List<Matches>>

    @Query("SELECT COUNT(id) FROM matches")
    fun getDataCount(): LiveData<Integer>
}
