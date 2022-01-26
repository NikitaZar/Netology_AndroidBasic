package ru.netology.nmedia.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ru.netology.nmedia.repository.Post

class PostDaoImpl(private val db: SQLiteDatabase) : PostDao {
    companion object {
        val DDL = """
        CREATE TABLE ${PostColumns.TABLE} (
            ${PostColumns.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${PostColumns.COLUMN_AUTHOR} TEXT NOT NULL,
            ${PostColumns.COLUMN_PUBLISHED_DATE} TEXT NOT NULL,
            ${PostColumns.COLUMN_POST_TEXT} TEXT NOT NULL,
            ${PostColumns.COLUMN_CNT_LIKES} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_CNT_SHARE} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_CNT_VISIBILITY} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_LIKED_BY_ME} BOOLEAN NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_URI_VIDEO} TEXT
        );
        """.trimIndent()
    }

    object PostColumns {
        const val TABLE = "posts"
        const val COLUMN_ID = "id"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_PUBLISHED_DATE = "publishedDate"
        const val COLUMN_POST_TEXT = "postText"
        const val COLUMN_CNT_LIKES = "cntLikes"
        const val COLUMN_CNT_SHARE = "cntShare"
        const val COLUMN_CNT_VISIBILITY = "cntVisibility"
        const val COLUMN_LIKED_BY_ME = "likedByMe"
        const val COLUMN_URI_VIDEO = "uriVideo"
        val ALL_COLUMNS = arrayOf(
            COLUMN_ID,
            COLUMN_AUTHOR,
            COLUMN_PUBLISHED_DATE,
            COLUMN_POST_TEXT,
            COLUMN_CNT_LIKES,
            COLUMN_CNT_SHARE,
            COLUMN_CNT_VISIBILITY,
            COLUMN_LIKED_BY_ME,
            COLUMN_URI_VIDEO
        )
    }

    override fun getAll(): List<Post> {
        val posts = mutableListOf<Post>()
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            null,
            null,
            null,
            null,
            "${PostColumns.COLUMN_ID} DESC"
        ).use {
            while (it.moveToNext()) {
                posts.add(map(it))
            }
        }
        return posts
    }

    override fun save(post: Post): Post {
        val values = ContentValues().apply {
            // TODO: remove hardcoded values
            put(PostColumns.COLUMN_AUTHOR, "Me")
            put(PostColumns.COLUMN_POST_TEXT, post.postText)
            put(PostColumns.COLUMN_PUBLISHED_DATE, "now")
        }
        val id = if (post.id != 0L) {
            db.update(
                PostColumns.TABLE,
                values,
                "${PostColumns.COLUMN_ID} = ?",
                arrayOf(post.id.toString()),
            )
            post.id
        } else {
            db.insert(PostColumns.TABLE, null, values)
        }
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
        ).use {
            it.moveToNext()
            return map(it)
        }
    }

    override fun likeById(id: Long) {
        db.execSQL(
            """
           UPDATE posts SET
               likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
               likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
           WHERE id = ?;
        """.trimIndent(), arrayOf(id)
        )
    }

    override fun removeById(id: Long) {
        db.delete(
            PostColumns.TABLE,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    private fun map(cursor: Cursor): Post {
        with(cursor) {
            return Post(
                id = getLong(getColumnIndexOrThrow(PostColumns.COLUMN_ID)),
                author = getString(getColumnIndexOrThrow(PostColumns.COLUMN_AUTHOR)),
                publishedDate = getString(getColumnIndexOrThrow(PostColumns.COLUMN_PUBLISHED_DATE)),
                postText = getString(getColumnIndexOrThrow(PostColumns.COLUMN_POST_TEXT)),
                cntLikes = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_CNT_LIKES)),
                cntShare = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_CNT_SHARE)),
                cntVisibility = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_CNT_VISIBILITY)),
                likeByMe = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKED_BY_ME)) != 0,
                uriVideo = getString(getColumnIndexOrThrow(PostColumns.COLUMN_URI_VIDEO)),
            )
        }
    }
}