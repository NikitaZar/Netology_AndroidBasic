package ru.netology.nmedia.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.fragments.EditPostFragment.Companion.textArg
import ru.netology.nmedia.postAdapter.OnInteractionListener
import ru.netology.nmedia.postAdapter.PostsAdapter
import ru.netology.nmedia.repository.Post
import ru.netology.nmedia.viewModel.PostViewModel

class FeedFragment : Fragment() {
    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)


    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(layoutInflater, container, false)

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
            findNavController().navigate(R.id.action_feedFragment_to_editPostFragment)
        }

        viewModel.edited.observe(viewLifecycleOwner) { post ->
            if (post.id != 0L) {
                findNavController().navigate(
                    R.id.action_feedFragment_to_editPostFragment,
                    Bundle().apply {
                        textArg = post.postText
                    })
            }
        }

        binding.postsList.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val newPost = adapter.itemCount < posts.size
            adapter.submitList(posts) {
                if (newPost) {
                    binding.postsList.smoothScrollToPosition(0)
                }
            }
        }
        return binding.root
    }
}