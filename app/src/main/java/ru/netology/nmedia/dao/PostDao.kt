package ru.netology.nmedia.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.netology.nmedia.entity.PostEntity

@Dao
interface PostDao {
    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAll(): LiveData<List<PostEntity>>

    @Query("SELECT * FROM PostEntity WHERE id = :id")
    fun getPostById(id: Long): LiveData<PostEntity>

    @Insert
    fun insert(post: PostEntity)

    @Query("UPDATE PostEntity SET postText = :postText WHERE id = :id")
    fun updateContent(id: Long, postText: String)

    fun save(post: PostEntity) =
        if (post.id != 0L) updateContent(post.id, post.postText) else insert(post)

    @Query(
        """UPDATE PostEntity SET cntLikes = cntLikes + CASE 
            WHEN likeByMe THEN -1 ELSE 1 END,
               likeByMe = CASE WHEN likeByMe THEN 0 ELSE 1 END 
            WHERE id = :id"""
    )
    fun likeById(id: Long)

    @Query("UPDATE PostEntity SET cntShare = cntShare + 1 WHERE id = :id")
    fun shareById(id: Long)

    @Query("DELETE FROM PostEntity WHERE id =:id")
    fun removeById(id: Long)
}