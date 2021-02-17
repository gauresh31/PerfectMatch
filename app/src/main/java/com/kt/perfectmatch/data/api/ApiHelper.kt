package com.kt.perfectmatch.data.api

class ApiHelper {
    class ApiHelper(private val apiService: APIClient) {
        suspend fun getMatchesData() = apiService.getMatchesData()
    }
}