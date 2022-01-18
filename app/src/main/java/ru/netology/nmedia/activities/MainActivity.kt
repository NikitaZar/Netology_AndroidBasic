package ru.netology.nmedia.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.nmedia.EditPostResultContract
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.postAdapter.OnInteractionListener
import ru.netology.nmedia.postAdapter.PostsAdapter
import ru.netology.nmedia.repository.Post
import ru.netology.nmedia.viewModel.PostViewModel
import java.util.*

class MainActivity : AppCompatActivity() {
    private val viewModel: PostViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val newPostLauncher = registerForActivityResult(EditPostResultContract()) { postText ->
            postText ?: return@registerForActivityResult
            viewModel.changePostText(postText)
            viewModel.save()
        }

        val editPostLauncher = registerForActivityResult(EditPostResultContract()) { postText ->
            postText ?: return@registerForActivityResult
            viewModel.changePostText(postText)
            viewModel.save()
        }

        val adapter = PostsAdapter(
            object : OnInteractionListener {
                override fun onLike(post: Post) {
                    viewModel.likeById(post.id)
                }

                override fun onShare(post: Post) {
                    viewModel.shareById(post.id)
                }

                override fun onEdit(post: Post) {
                    viewModel.edit(post)
                }

                override fun onRemove(post: Post) {
                    viewModel.removeById(post.id)
                }

                override fun onVideoShare(post: Post) {
                    val shareVideoIntent = Intent(Intent.ACTION_VIEW, Uri.parse(post.uriVideo))
                    startActivity(shareVideoIntent)
                }
            }
        )

        binding.addButton.setOnClickListener {
            newPostLauncher.launch("")
        }

        viewModel.edited.observe(this) { post ->
            if (post.id != 0L) {
                editPostLauncher.launch(post.postText)
            }
        }

        binding.postsList.adapter = adapter

        viewModel.data.observe(this) { posts ->
            val newPost = adapter.itemCount < posts.size
            adapter.submitList(posts) {
                if (newPost) {
                    binding.postsList.smoothScrollToPosition(0)
                }
            }
        }
    }
}