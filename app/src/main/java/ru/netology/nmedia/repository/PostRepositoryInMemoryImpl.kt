package ru.netology.nmedia.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PostRepositoryInMemoryImpl : PostRepository {

    private var post = Post(
        id = 1,
        author = "Нетология. Университет интернет профессий",
        publishedDate = "21 Мая в 18:36",
        postText = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
        cntLikes = 0,
        cntVisibility = 9_990_000,
        likeByMe = false
    )

    private val data = MutableLiveData(post)

    override fun get(): LiveData<Post> = data

    override fun like() {

        val cntLikes =
            when (!post.likeByMe) {
            true -> post.cntLikes + 1
            false -> post.cntLikes - 1
        }

        Log.i("cntLikes", post.cntLikes.toString())

        post = post.copy(
            likeByMe = !post.likeByMe,
            cntLikes = cntLikes
        )
        data.value = post
    }

    override fun share() {
        post = post.copy(cntShare = ++post.cntShare)
        Log.i("cntShare", post.cntShare.toString())
        data.value = post
    }
}
