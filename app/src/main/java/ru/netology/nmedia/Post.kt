package ru.netology.nmedia

data class Post(
    val author: String,
    val publishedDate: String,
    val postText: String,
    var cntLikes: Int = 0,
    var cntShare: Int = 0,
    val cntVisibility: Int = 0,
    var likeByMe: Boolean = false
)
