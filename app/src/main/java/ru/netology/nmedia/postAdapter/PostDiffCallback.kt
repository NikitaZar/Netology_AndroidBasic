package ru.netology.nmedia.postAdapter

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import ru.netology.nmedia.repository.Post

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {

    override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Post, newItem: Post) = false //oldItem == newItem


}