package com.example.gitexplorer.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.gitexplorer.R
import com.example.gitexplorer.data.remote.RepoRepository
import com.example.gitexplorer.data.remote.model.RepoDto
import com.example.gitexplorer.databinding.FragmentDetailsBinding
import com.example.gitexplorer.ui.search.DetailsViewModel
import com.example.gitexplorer.ui.search.DetailsViewModelFactory
import com.squareup.moshi.Moshi


class DetailsFragment : Fragment() {
    private var _b: FragmentDetailsBinding? = null
    private val b get() = _b!!

    private lateinit var repo: RepoRepository
    private lateinit var vm: DetailsViewModel
    private lateinit var dto: RepoDto


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentDetailsBinding.inflate(inflater, container, false).also { _b = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val json = arguments?.getString("repo_json") ?: ""
        val moshi = Moshi.Builder().build().adapter(RepoDto::class.java)
        dto = moshi.fromJson(json)!!

        repo = RepoRepository(requireContext())   // FIX

        val factory = DetailsViewModelFactory(repo)
        vm = ViewModelProvider(this, factory).get(DetailsViewModel::class.java)

        b.tvRepoName.text = dto.name
        b.tvDescription.text = dto.description ?: "No description"
        b.tvLanguage.text = "Language: ${dto.language ?: "N/A"}"
        b.tvStarsForks.text = "Stars: ${dto.stargazersCount}  Forks: ${dto.forksCount}"
        b.tvOwnerName.text = dto.owner.login
        Glide.with(requireContext()).load(dto.owner.avatarUrl).into(b.imgOwner)

        vm.checkFavorite(dto.id)
        vm.isFav.observe(viewLifecycleOwner) { isFav ->
            b.btnToggleFav.text = if (isFav) "Remove from favorites" else "Add to favorites"
        }

        b.btnToggleFav.setOnClickListener {
            vm.toggleFavorite(dto)
        }
    }

    override fun onDestroyView() {
        _b = null
        super.onDestroyView()
    }
}