package ru.netology.nmedia.entity

import androidx.room.*
import ru.netology.nmedia.repository.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val publishedDate: String,
    val postText: String,
    var cntLikes: Int = 0,
    var cntShare: Int = 0,
    val cntVisibility: Int = 0,
    val likeByMe: Boolean,
    var uriVideo: String = ""
) {
    fun toDto() = Post(
        id,
        author,
        publishedDate,
        postText,
        cntLikes,
        cntShare,
        cntVisibility,
        likeByMe,
        uriVideo
    )

    companion object {
        fun fromDto(post: Post) = with(post) {
            PostEntity(
                id,
                author,
                publishedDate,
                postText,
                cntLikes,
                cntShare,
                cntVisibility,
                likeByMe,
                uriVideo
            )
        }
    }
}
