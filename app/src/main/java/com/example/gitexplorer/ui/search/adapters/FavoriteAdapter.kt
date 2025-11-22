package com.example.gitexplorer.ui.search.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.gitexplorer.data.remote.local.FavoriteEntity
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gitexplorer.databinding.ItemRepoBinding


class FavoriteAdapter(private val itemClick: (FavoriteEntity) -> Unit) :
    ListAdapter<FavoriteEntity, FavoriteAdapter.VH>(DIFF) {

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<FavoriteEntity>() {
            override fun areItemsTheSame(oldItem: FavoriteEntity, newItem: FavoriteEntity) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: FavoriteEntity, newItem: FavoriteEntity) = oldItem == newItem
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
        fun bind(item: FavoriteEntity) {
            b.tvName.text = item.name
            b.tvOwner.text = item.ownerLogin
            b.tvStars.text = item.stars.toString()
            Glide.with(b.imgAvatar.context).load(item.ownerAvatarUrl).into(b.imgAvatar)
            b.root.setOnClickListener { itemClick(item) }
            b.btnFav.visibility = View.GONE
        }
    }
}