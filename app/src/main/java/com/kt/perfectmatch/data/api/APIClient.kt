package com.kt.perfectmatch.data.api

import com.kt.perfectmatch.data.model.MatchesNestedModel
import retrofit2.Response
import retrofit2.http.GET

interface APIClient {

    @GET("/api/?results=10")
    suspend fun getMatchesData(): Response<MatchesNestedModel>
}