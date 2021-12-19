package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.repository.Post
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        viewModel.data.observe(this) { post ->
            with(binding) {
                author.text = post.author
                publishedDate.text = post.publishedDate
                postText.text = post.postText
                cntShare.text = valueCntPresenter(post.cntShare)
                cntVisibility.text = valueCntPresenter(post.cntVisibility)
                icLike.setImageResource(likeByMeIcon(post.likeByMe))
                cntLikes.text = valueCntPresenter(post.cntLikes)
            }
        }

        binding.icLike.setOnClickListener {
            viewModel.like()
        }

        binding.icShare.setOnClickListener {
            viewModel.share()
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