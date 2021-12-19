package ru.netology.nmedia.postAdapter

import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.repository.Post

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onLikeListener: OnLikeListener,
    private val onShareListener: OnShareListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            publishedDate.text = post.publishedDate
            postText.text = post.postText
            cntVisibility.text = valueCntPresenter(post.cntVisibility)
            icLike.setImageResource(likeByMeIcon(post.likeByMe))
            icLike.setOnClickListener{
                onLikeListener(post)
            }
            cntLikes.text = valueCntPresenter(post.cntLikes)
            icShare.setOnClickListener{
                onShareListener(post)
            }
            cntShare.text = valueCntPresenter(post.cntShare)
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