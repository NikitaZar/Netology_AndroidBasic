package ru.netology.nmedia.repository

data class Post(
    val id: Long,
    val author: String,
    val publishedDate: String,
    val postText: String,
    var cntLikes: Int = 0,
    var cntShare: Int = 0,
    val cntVisibility: Int = 0,
    val likeByMe: Boolean
)
