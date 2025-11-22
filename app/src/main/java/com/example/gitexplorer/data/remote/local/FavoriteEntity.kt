package com.example.gitexplorer.data.remote.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val ownerLogin: String,
    val ownerAvatarUrl: String,
    val htmlUrl:String,
    val type: String,
    val description: String?,
    val stars: Int,
    val forks: Int,
    val language: String?
)