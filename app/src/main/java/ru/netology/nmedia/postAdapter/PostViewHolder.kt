package ru.netology.nmedia.postAdapter

import android.widget.PopupMenu
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
            icLike.setImageResource(likeByMeIcon(post.likeByMe))
            icLike.setOnClickListener {
                onInteractionListener.onLike(post)
            }
            cntLikes.text = valueCntPresenter(post.cntLikes)
            icShare.setOnClickListener {
                onInteractionListener.onShare(post)
            }
            cntShare.text = valueCntPresenter(post.cntShare)
        }
        binding.moreButtonImageView.setOnClickListener {
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
    }
}

fun valueCntPresenter(value: Int) = when (value) {
    in 0..999 -> value.toString()
    in 1000..1099 -> "${String.format("%.1f", (value.toDouble() / 1_000))}K"
    in 1100..999_999 -> "${(value / 1_000)}K"
    else -> "${String.format("%.1f", (value.toDouble() / 1_000_000))}M"
}

fun likeByMeIcon(isLikedByMe: Boolean) =
    when (isLikedByMe) {
        true -> R.drawable.ic_baseline_favorite_24
        false -> R.drawable.ic_favorite_border
    }