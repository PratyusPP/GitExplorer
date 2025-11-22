package com.example.gitexplorer.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResponse(
    @field:Json(name = "total_count") val totalCount: Int,
    @field:Json(name = "incomplete_results") val incompleteResults: Boolean,
    @field:Json(name = "items") val items: List<RepoDto>
)

@JsonClass(generateAdapter = true)
data class RepoDto(
    val id: Long,
    val name: String,
    @field:Json(name = "full_name") val fullName: String,
    val description: String?,
    val language: String?,
    @field:Json(name = "forks_count") val forksCount: Int,
    @field:Json(name = "stargazers_count") val stargazersCount: Int,
    @field:Json(name = "html_url") val htmlUrl: String,
    val fork: Boolean,
    val owner: OwnerDto
)

@JsonClass(generateAdapter = true)
data class OwnerDto(
    val login: String,
    val id: Long,
    @field:Json(name = "avatar_url") val avatarUrl: String,
    @field:Json(name = "html_url") val htmlUrl: String,
    val type: String
)