package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*

class PostRepositoryFileImpl(val context: Context) : PostRepository {

    companion object {
        private const val fileName = "posts.json"
        private const val defaultId = 1L
    }

    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type

    private var nextPostId = defaultId
    private val data = MutableLiveData(emptyList<Post>())

    init {
        val file = context.filesDir.resolve(fileName)
        if (file.exists()) {
            context.openFileInput(fileName).bufferedReader().use {
                data.value = gson.fromJson(it, type)
                nextPostId = data.value?.maxOfOrNull { it.id }?.inc() ?: defaultId
            }
        } else {
            sync()
        }
    }

    override fun getAll() = data

    override fun likeById(id: Long) {
        data.value = data.value?.map {
            when (it.id == id) {
                true -> {
                    val cntLikes = if (it.likeByMe) {
                        it.cntLikes - 1
                    } else {
                        it.cntLikes + 1
                    }
                    it.copy(likeByMe = !it.likeByMe, cntLikes = cntLikes)
                }
                false -> it
            }
        }
        sync()
    }

    override fun shareById(id: Long) {
        data.value = data.value?.map {
            when (it.id == id) {
                true -> it.copy(cntShare = it.cntShare + 1)
                false -> it
            }
        }
        sync()
    }

    override fun save(post: Post) {
        if (post.id == 0L) {
            val posts = data.value ?: emptyList()
            data.value = listOf(
                post.copy(
                    id = nextPostId++,
                    author = "Me",
                    likeByMe = false,
                    publishedDate = getCurrentTimeAsString()
                )
            ) + posts
            sync()
            return
        }
        data.value = data.value?.map {
            if (it.id != post.id) it else it.copy(postText = post.postText)
        }
        sync()
    }

    override fun removeById(id: Long) {
        data.value = data.value?.filter { it.id != id }
        sync()
    }


    private fun getCurrentTimeAsString(): String {
        val sdf = SimpleDateFormat("dd.MM.yyyy 'в' HH:mm:ss")
        return sdf.format(Date())
    }

    private fun sync() {
        context.openFileOutput(fileName, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(data.value))
        }
    }
}