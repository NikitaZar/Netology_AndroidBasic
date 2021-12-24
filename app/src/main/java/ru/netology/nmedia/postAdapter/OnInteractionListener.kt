package ru.netology.nmedia.postAdapter

import ru.netology.nmedia.repository.Post

interface OnInteractionListener {
    fun onLike(post:Post)
    fun onShare(post: Post)
    fun onRemove(post: Post)
}