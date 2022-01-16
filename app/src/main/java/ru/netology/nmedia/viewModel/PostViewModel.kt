package ru.netology.nmedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.repository.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryFileImpl

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val empty = Post(
        id = 0,
        postText = "",
        author = "",
        likeByMe = false,
        publishedDate = ""
    )
    private val repository: PostRepository = PostRepositoryFileImpl(application)
    val data = repository.getAll()
    val edited = MutableLiveData(empty)

    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)

    fun edit(post: Post) {
        edited.value = post
    }

    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }

    fun changePostText(postText: String) {
        edited.value?.let {
            val text = postText.trim()
            if (it.postText == postText) {
                return
            }
            edited.value = it.copy(postText = text)
        }
    }
}