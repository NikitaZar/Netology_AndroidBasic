package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ru.netology.nmedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var post = Post(
            author = getResources().getString(R.string.sample_author),
            publishedDate = getResources().getString(R.string.sample_date),
            postText = getResources().getString(R.string.sample_text),
            cntLikes = 1101,
            cntVisibility = 9_990_000
        )

        with(binding) {
            author.text = post.author
            publishedDate.text = post.publishedDate
            postText.text = post.postText
            cntLikes.text = valueCntPresenter(post.cntLikes)
            cntShare.text = valueCntPresenter(post.cntShare)
            cntVisibility.text = valueCntPresenter(post.cntVisibility)

            icLike.setOnClickListener {
                post.likeByMe = !post.likeByMe
                if (post.likeByMe) {
                    post = post.copy(cntLikes = post.cntLikes++)
                    icLike.setImageResource(R.drawable.ic_baseline_favorite_24)
                } else {
                    post = post.copy(cntLikes = post.cntLikes--)
                    icLike.setImageResource(R.drawable.ic_favorite_border)
                }
                cntLikes.text = valueCntPresenter(post.cntLikes)

                Log.i("call", "Like")
            }

            icShare.setOnClickListener {
                post.cntShare++
                cntShare.text = valueCntPresenter(post.cntShare)
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