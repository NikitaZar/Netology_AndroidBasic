package ru.netology.nmedia.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import ru.netology.nmedia.NewPostResultContract
import ru.netology.nmedia.androidUtils.AndroidUtils
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

        val newPostLauncher = registerForActivityResult(NewPostResultContract()) { postText ->
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
//                    finishPostEdit(binding.postText, binding.editGroup, viewModel)
                }
            }
        )

        binding.addButton.setOnClickListener {
            newPostLauncher.launch()
        }

//        binding.saveButton.setOnClickListener {
//            with(binding.postText) {
//                if (text.isNullOrBlank()) {
//                    Toast.makeText(
//                        this@MainActivity,
//                        R.string.content_cant_be_empty,
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    return@setOnClickListener
//                }
//                viewModel.changePostText(text.toString())
//                viewModel.save()
//                finishPostEdit(binding.postText, binding.editGroup, viewModel)
//            }
//        }
//
//        binding.cancelEditImage.setOnClickListener {
//            finishPostEdit(binding.postText, binding.editGroup, viewModel)
//        }

        binding.postsList.adapter = adapter

        viewModel.data.observe(this) { posts ->
            val newPost = adapter.itemCount < posts.size
            adapter.submitList(posts) {
                if (newPost) {
                    binding.postsList.smoothScrollToPosition(0)
                }
            }
        }

//        viewModel.edited.observe(this) { post ->
//            if (post.id == 0L) {
//                return@observe
//            }
//            with(binding.postText) {
//                requestFocus()
//                setText(post.postText)
//            }
//            binding.editGroup.isVisible = true
//            binding.editText.text = post.postText
//        }
    }
}

fun finishPostEdit(text: EditText, editGroup: Group, viewModel: PostViewModel) {
    text.setText("")
    text.clearFocus()
    AndroidUtils.hideKeyboard(text)
    viewModel.cancelEdit()
    editGroup.isVisible = false
}