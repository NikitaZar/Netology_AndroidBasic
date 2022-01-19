package ru.netology.nmedia.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.androidUtils.LongArg
import ru.netology.nmedia.databinding.FragmentDetailPostBinding
import ru.netology.nmedia.fragments.EditPostFragment.Companion.textArg
import ru.netology.nmedia.postAdapter.OnInteractionListener
import ru.netology.nmedia.postAdapter.PostViewHolder
import ru.netology.nmedia.postAdapter.valueCntPresenter
import ru.netology.nmedia.repository.Post
import ru.netology.nmedia.viewModel.PostViewModel

class DetailPostFragment : Fragment() {

    companion object {
        var Bundle.postId: Long by LongArg
    }

    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentDetailPostBinding.inflate(layoutInflater, container, false)

        val postId = arguments?.postId ?: -1
        val post = viewModel.data.value?.find { post -> post.id == postId }

        post?.let {
            with(binding.postContent) {
                val postVieHolder = PostViewHolder(
                    this,
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
                            findNavController().navigateUp()
                        }

                        override fun onVideoShare(post: Post) {
                            val shareVideoIntent =
                                Intent(Intent.ACTION_VIEW, Uri.parse(post.uriVideo))
                            startActivity(shareVideoIntent)
                        }

                        override fun onDetailPost(postId: Long) {}
                    })

                viewModel.edited.observe(viewLifecycleOwner) { post ->
                    if (post.id != 0L) {
                        findNavController().navigate(
                            R.id.action_detailPostFragment_to_editPostFragment,
                            Bundle().apply { textArg = post.postText })
                    }
                }

                viewModel.data.observe(viewLifecycleOwner) { ListPost ->
                    ListPost.find { it.id == post.id }?.let {
                        postVieHolder.bind(it)
                    }

                }
            }
        }

        return binding.root
    }
}