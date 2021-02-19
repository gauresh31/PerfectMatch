package com.kt.perfectmatch.db

import android.content.Context
import androidx.room.Room

class DatabaseClient {
    private var mCtx: Context? = null
    private var mInstance: DatabaseClient? = null

    //our app database object
    private var appDatabase: AppDatabase? = null

    private fun DatabaseClient(mCtx: Context) {
        this.mCtx = mCtx

        //creating the app database with Room database builder
        appDatabase = Room.databaseBuilder(mCtx, AppDatabase::class.java, "MyMatches").build()
    }

    @Synchronized
    fun getInstance(mCtx: Context): DatabaseClient? {
        if (mInstance == null) {
//            mInstance = DatabaseClient(mCtx)
        }
        return mInstance
    }

    fun getAppDatabase(): AppDatabase? {
        return appDatabase
    }
}