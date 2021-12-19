package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PostRepositoryInMemoryImpl : PostRepository {

    private val data = MutableLiveData(
        Post(
            id = 1,
            author = "Нетология. Университет интернет профессий",
            publishedDate = "21 Мая в 18:36",
            postText = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            cntLikes = 0,
            cntVisibility = 9_990_000,
            likeByMe = false
        )
    )

    override fun get(): LiveData<Post> = data

    override fun like() {
        var isLikeByMe = data.value?.likeByMe ?: false
        isLikeByMe = !isLikeByMe

        val cntLikes =
            when (isLikeByMe) {
                true -> data.value?.cntLikes?.plus(1)
                false -> data.value?.cntLikes?.minus(1)
            } ?: 0

        data.value = data.value?.copy(
            likeByMe = isLikeByMe,
            cntLikes = cntLikes
        )
    }

    override fun share() {
        val cntShare = data.value?.cntShare?.plus(1) ?: 0
        data.value = data.value?.copy(cntShare = cntShare)
    }
}
