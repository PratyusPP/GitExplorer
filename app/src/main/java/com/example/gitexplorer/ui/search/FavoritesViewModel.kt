package com.example.gitexplorer.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.gitexplorer.data.remote.RepoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class FavoritesViewModel(repo: RepoRepository) : ViewModel() {
    val favorites = repo.getFavorites()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}

class FavoritesViewModelFactory(
    private val repo: RepoRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            return FavoritesViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

