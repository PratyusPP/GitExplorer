package com.example.gitexplorer.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.gitexplorer.R
import com.example.gitexplorer.data.remote.RepoRepository
import com.example.gitexplorer.data.remote.model.OwnerDto
import com.example.gitexplorer.data.remote.model.RepoDto
import com.example.gitexplorer.databinding.FragmentFavouritesBinding
import com.example.gitexplorer.ui.search.FavoritesViewModel
import com.example.gitexplorer.ui.search.FavoritesViewModelFactory
import com.example.gitexplorer.ui.search.adapters.FavoriteAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FavouritesFragment : Fragment() {

    private var _b: FragmentFavouritesBinding? = null
    private val b get() = _b!!

    private lateinit var repo: RepoRepository
    private lateinit var vm: FavoritesViewModel
    private lateinit var adapter: FavoriteAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentFavouritesBinding.inflate(inflater, container, false).also { _b = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        repo = RepoRepository(requireContext())   // FIX

        vm = ViewModelProvider(this, FavoritesViewModelFactory(repo)).get(FavoritesViewModel::class.java)
        adapter = FavoriteAdapter { fav ->
            // convert FavoriteEntity to RepoDto to reuse details screen
            val dto = RepoDto(
                id = fav.id,
                name = fav.name,
                fullName = fav.name, // or fav.name again if you don't have fullName stored
                description = fav.description,
                language = fav.language,
                forksCount = fav.forks,
                stargazersCount = fav.stars,
                htmlUrl = fav.htmlUrl,
                fork = false, // default, you can change
                owner = OwnerDto(
                    login = fav.ownerLogin,
                    id = fav.id,
                    avatarUrl = fav.ownerAvatarUrl,
                    htmlUrl = fav.htmlUrl,
                    type = fav.type
                )
            )
            val moshi = Moshi.Builder().build().adapter(RepoDto::class.java)
            val json = moshi.toJson(dto)
            val bundle = Bundle().apply { putString("repo_json", json) }
            findNavController().navigate(R.id.action_favorites_to_details, bundle)
        }
        b.rvFavs.adapter = adapter

        lifecycleScope.launch {
            vm.favorites.collectLatest { list ->
                adapter.submitList(list)
                b.tvFavStatus.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
                b.tvFavStatus.text = "No favorites yet"
            }
        }
    }

    override fun onDestroyView() {
        _b = null
        super.onDestroyView()
    }
}