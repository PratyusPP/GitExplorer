package com.example.gitexplorer.data.remote


import android.content.Context
import com.example.gitexplorer.data.remote.RetrofitClient
import com.example.gitexplorer.data.remote.local.AppDatabase
import com.example.gitexplorer.data.remote.local.FavoriteEntity
import com.example.gitexplorer.data.remote.model.RepoDto
import kotlinx.coroutines.flow.Flow

class RepoRepository(context: Context) {
    private val api = RetrofitClient.api
    private val dao = AppDatabase.get(context).repoDao()

    suspend fun searchRepos(query: String, page: Int = 1, perPage: Int = 30) =
        api.searchRepositories(query, page, perPage)

    // Favorites
    fun getFavorites(): Flow<List<FavoriteEntity>> = dao.getAllFavorites()

    suspend fun addFavorite(dto: RepoDto) {
        val fav = FavoriteEntity(
            id = dto.id,
            name = dto.name,
            ownerLogin = dto.owner.login,
            ownerAvatarUrl = dto.owner.avatarUrl,
            type = dto.owner.type,
            htmlUrl = dto.owner.htmlUrl,
            description = dto.description,
            stars = dto.stargazersCount,
            forks = dto.forksCount,
            language = dto.language
        )
        dao.insert(fav)
    }

    suspend fun removeFavorite(entity: FavoriteEntity) = dao.delete(entity)

    suspend fun isFavorite(id: Long) = dao.isFavorite(id)
}