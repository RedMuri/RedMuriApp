package com.example.redmuriapp.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.redmuriapp.domain.entities.LatestItem

class LatestItemDiffCallback : DiffUtil.ItemCallback<LatestItem>() {

    override fun areItemsTheSame(oldItem: LatestItem, newItem: LatestItem): Boolean =
        oldItem.image_url == newItem.image_url

    override fun areContentsTheSame(oldItem: LatestItem, newItem: LatestItem): Boolean =
        oldItem==newItem
}