package ru.netology.nmedia.repository

import androidx.lifecycle.MutableLiveData

interface PostRepository {
    fun getAll(): MutableLiveData<List<Post>>
    fun likeById(id: Long)
    fun shareById(id: Long)
    fun save(post: Post)
    fun removeById(id: Long)

}