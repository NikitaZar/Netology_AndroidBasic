package ru.netology.nmedia.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import java.text.SimpleDateFormat
import java.util.*

class PostRepositoryInMemoryImpl : PostRepository {

    private var nextPostId = 1L
    private val data = MutableLiveData(
        listOf(
            Post(
                id = nextPostId++,
                author = "Нетология. Университет интернет профессий",
                publishedDate = "21 Мая в 18:36",
                postText = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
                cntLikes = 0,
                cntVisibility = 9_990_000,
                likeByMe = false
            ),
            Post(
                id = nextPostId++,
                author = "Нетология. Университет интернет профессий",
                publishedDate = "21 Мая в 18:36",
                postText = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
                cntLikes = 10,
                cntVisibility = 30,
                likeByMe = false
            ),
            Post(
                id = nextPostId++,
                author = "Нетология. Университет интернет профессий",
                publishedDate = "21 Мая в 18:36",
                postText = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
                cntLikes = 10,
                cntVisibility = 30,
                likeByMe = false
            ),
            Post(
                id = nextPostId++,
                author = "Нетология. Университет интернет профессий",
                publishedDate = "21 Мая в 18:36",
                postText = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
                cntLikes = 10,
                cntVisibility = 30,
                likeByMe = false,
                uriVideo = "https://www.youtube.com/watch?v=qXQYd9y0neU"
            )
        ).reversed()
    )

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
    }

    override fun shareById(id: Long) {
        data.value = data.value?.map {
            when (it.id == id) {
                true -> it.copy(cntShare = it.cntShare + 1)
                false -> it
            }
        }
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
            return
        }
        data.value = data.value?.map {
            if (it.id != post.id) it else it.copy(postText = post.postText)
        }
    }

    override fun removeById(id: Long) {
        data.value = data.value?.filter { it.id != id }
        Log.i("data", data.value.toString())
    }
}

fun getCurrentTimeAsString(): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy 'в' HH:mm:ss")
    return sdf.format(Date())
}