package ru.netology.nmedia.services

data class Like(
    val userId: Long,
    val userName: String,
    val postId: String,
    val postAuthor: String
)
