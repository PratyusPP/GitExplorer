package com.example.gitexplorer.ui.search

import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.gitexplorer.data.remote.RepoRepository
import com.example.gitexplorer.data.remote.model.RepoDto
import com.example.gitexplorer.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SearchViewModel(private val repo: RepoRepository) : ViewModel() {

    private val _state = MutableLiveData<Resource<List<RepoDto>>>()
    val state: LiveData<Resource<List<RepoDto>>> = _state

    private var searchJob: Job? = null
    private var currentPage = 1
    private var lastQuery: String = ""

    fun search(query: String) {
        // debounce 500ms
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            if (query.isBlank()) {
                _state.value = Resource.Success(emptyList())
                return@launch
            }
            _state.value = Resource.Loading
            try {
                currentPage = 1
                val response = repo.searchRepos(query, currentPage)
                _state.value = Resource.Success(response.items)
                lastQuery = query
            } catch (e: Exception) {
                _state.value = Resource.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun loadMore() {
        if (lastQuery.isBlank()) return
        viewModelScope.launch {
            _state.value = Resource.Loading
            try {
                currentPage += 1
                val response = repo.searchRepos(lastQuery, currentPage)
                val old = (_state.value as? Resource.Success)?.data ?: emptyList()
                _state.value = Resource.Success(old + response.items)
            } catch (e: Exception) {
                _state.value = Resource.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }
}

class SearchViewModelFactory(
    private val repo: RepoRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>,
        extras: CreationExtras
    ): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
