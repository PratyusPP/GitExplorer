package com.example.gitexplorer.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.gitexplorer.R
import com.example.gitexplorer.data.remote.RepoRepository
import com.example.gitexplorer.data.remote.model.RepoDto
import com.example.gitexplorer.databinding.FragmentSearchBinding
import com.example.gitexplorer.ui.search.SearchViewModel
import com.example.gitexplorer.ui.search.SearchViewModelFactory
import com.squareup.moshi.Moshi

import androidx.navigation.fragment.findNavController
import com.example.gitexplorer.data.remote.local.AppDatabase
import com.example.gitexplorer.ui.search.adapters.RepoAdapter
import com.example.gitexplorer.util.Resource


class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var repo: RepoRepository
    private lateinit var vm: SearchViewModel
    private lateinit var adapter: RepoAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentSearchBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        repo = RepoRepository(requireContext())   // FIX

        val factory = SearchViewModelFactory(repo)
        vm = ViewModelProvider(this, factory).get(SearchViewModel::class.java)

        adapter = RepoAdapter { dto, favClick ->
            if (!favClick) {
                // open details - serialize dto to JSON string to pass
                val moshi = Moshi.Builder().build().adapter(RepoDto::class.java)
                val json = moshi.toJson(dto)
                val action = com.example.gitexplorer.R.id.action_search_to_details
                val bundle = Bundle().apply { putString("repo_json", json) }
                findNavController().navigate(action, bundle)
            } else {
                // handled inside adapter callback
            }
        }

        binding.rvRepos.adapter = adapter

        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { vm.search(it) }
                binding.searchView.clearFocus()
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                vm.search(newText ?: "")
                return true
            }
        })

        binding.btnFavorites.setOnClickListener {
            findNavController().navigate(R.id.favoritesFragment)
        }

        vm.state.observe(viewLifecycleOwner) { res ->
            when (res) {
                is Resource.Loading -> {
                    binding.tvStatus.visibility = View.VISIBLE
                    binding.tvStatus.text = "Loading..."
                }
                is Resource.Success -> {
                    binding.tvStatus.visibility =
                        if (res.data.isEmpty()) View.VISIBLE else View.GONE
                    binding.tvStatus.text = if (res.data.isEmpty()) "No results found" else ""
                    adapter.submitList(res.data)
                }
                is Resource.Error -> {
                    binding.tvStatus.visibility = View.VISIBLE
                    binding.tvStatus.text = "Error: ${res.message}"
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}