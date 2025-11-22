package com.example.gitexplorer.ui.search

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.gitexplorer.data.remote.RepoRepository
import com.example.gitexplorer.data.remote.local.FavoriteEntity
import com.example.gitexplorer.data.remote.model.RepoDto
import kotlinx.coroutines.launch

class DetailsViewModel(private val repo: RepoRepository) : ViewModel() {

    private val _isFav = MutableLiveData<Boolean>(false)
    val isFav: LiveData<Boolean> = _isFav

    fun checkFavorite(id: Long) {
        viewModelScope.launch {
            val fav = repo.isFavorite(id)
            _isFav.value = fav
        }
    }

    fun toggleFavorite(dto: RepoDto) {
        viewModelScope.launch {
            val currentlyFav = repo.isFavorite(dto.id)
            if (currentlyFav) {
                repo.removeFavorite(FavoriteEntity(dto.id, dto.name, dto.owner.login, dto.owner.avatarUrl,dto.owner.htmlUrl,dto.owner.type,dto.description,dto.stargazersCount, dto.forksCount, dto.language))
                _isFav.value = false
            } else {
                repo.addFavorite(dto)
                _isFav.value = true
            }
        }
    }
}

class DetailsViewModelFactory(
    private val repo: RepoRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
            return DetailsViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}