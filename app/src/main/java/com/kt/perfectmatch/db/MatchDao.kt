package com.kt.perfectmatch.db

import androidx.room.*


@Dao
public interface MatchDao {
    @Query("SELECT * FROM matches")
    fun getAll(): List<Matches?>?

    @Insert
    fun insert(matches: Matches?)

    @Delete
    fun delete(matches: Matches?)

    @Update
    fun update(matches: Matches?)
}