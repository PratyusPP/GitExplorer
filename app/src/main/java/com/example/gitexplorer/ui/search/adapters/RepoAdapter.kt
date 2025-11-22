package com.example.gitexplorer.ui.search.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gitexplorer.data.remote.model.RepoDto
import com.example.gitexplorer.databinding.ItemRepoBinding
import androidx.recyclerview.widget.ListAdapter




class RepoAdapter(private val itemClick: (RepoDto, isFavClick: Boolean) -> Unit) :
    ListAdapter<RepoDto, RepoAdapter.VH>(DIFF) {

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<RepoDto>() {
            override fun areItemsTheSame(oldItem: RepoDto, newItem: RepoDto) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: RepoDto, newItem: RepoDto) = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VH(private val b: ItemRepoBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(item: RepoDto) {
            b.tvName.text = item.name
            b.tvOwner.text = item.owner.login
            b.tvStars.text = item.stargazersCount.toString()
            Glide.with(b.imgAvatar.context).load(item.owner.avatarUrl).into(b.imgAvatar)

            b.root.setOnClickListener { itemClick(item, false) }
            b.btnFav.setOnClickListener {
                // toggle favorite by calling repository directly (simple approach)
                itemClick(item, true)
            }
        }
    }
}