package ru.netology.nmedia.postAdapter

import android.util.Log
import android.view.View
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.repository.Post

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            publishedDate.text = post.publishedDate
            postText.text = post.postText
            cntVisibility.text = valueCntPresenter(post.cntVisibility)
            btLike.isChecked = post.likeByMe
            btLike.text = valueCntPresenter(post.cntLikes)
            btLike.setOnClickListener {
                onInteractionListener.onLike(post)
            }
            btShare.text = valueCntPresenter(post.cntShare)
            btShare.setOnClickListener {
                onInteractionListener.onShare(post)
            }
            videoGroup.isVisible = (post.uriVideo != "")
            btOptions.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.post_menu)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }
                            else -> false
                        }

                    }
                }.show()
            }
            videoGroup.referencedIds.forEach { id ->
                binding.root.findViewById<View>(id).setOnClickListener {
                    onInteractionListener.onVideoShare(post)
                    Log.i("onVideoShare", "onVideoShare")
                }
            }

            root.setOnClickListener {
                onInteractionListener.onDetailPost(post.id)
            }
        }
    }
}

fun valueCntPresenter(value: Int) = when (value) {
    in 0..999 -> value.toString()
    in 1000..1099 -> "${String.format("%.1f", (value.toDouble() / 1_000))}K"
    in 1100..999_999 -> "${(value / 1_000)}K"
    else -> "${String.format("%.1f", (value.toDouble() / 1_000_000))}M"
}