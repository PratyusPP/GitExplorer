package com.example.gitexplorer.data.remote

import com.example.gitexplorer.data.remote.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService{
    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 30
    ): SearchResponse
}