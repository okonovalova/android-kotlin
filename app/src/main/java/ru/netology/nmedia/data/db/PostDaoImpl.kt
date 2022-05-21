package ru.netology.nmedia.data.db

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import ru.netology.nmedia.data.model.PostInfo

class PostDaoImpl(private val db: SQLiteDatabase) : PostDao {
    companion object {
        val DDL = """
        CREATE TABLE ${PostColumns.TABLE} (
            ${PostColumns.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${PostColumns.COLUMN_AUTHOR} TEXT NOT NULL,
            ${PostColumns.COLUMN_CONTENT} TEXT NOT NULL,
            ${PostColumns.COLUMN_IS_LIKED} BOOLEAN NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_LIKES_COUNT} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_SHARED_COUNT} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_VIEWS_COUNT} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_DATE} TEXT,
            ${PostColumns.COLUMN_LINK_PART} TEXT,
            ${PostColumns.COLUMN_VIDEO_PREVIEW_URL} TEXT,
            ${PostColumns.COLUMN_VIDEO_URL} TEXT
        );
        """.trimIndent()
    }

    object PostColumns {
        const val TABLE = "posts"
        const val COLUMN_ID = "id"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_IS_LIKED = "isLiked"
        const val COLUMN_LIKES_COUNT = "likesCount"
        const val COLUMN_SHARED_COUNT = "sharedCount"
        const val COLUMN_VIEWS_COUNT = "viewCount"
        const val COLUMN_DATE = "date"
        const val COLUMN_LINK_PART = "linkPart"
        const val COLUMN_VIDEO_PREVIEW_URL = "videoPreviewUrl"
        const val COLUMN_VIDEO_URL = "videoUrl"

        val ALL_COLUMNS = arrayOf(
            COLUMN_ID,
            COLUMN_AUTHOR,
            COLUMN_CONTENT,
            COLUMN_IS_LIKED,
            COLUMN_LIKES_COUNT,
            COLUMN_SHARED_COUNT,
            COLUMN_VIEWS_COUNT,
            COLUMN_DATE,
            COLUMN_LINK_PART,
            COLUMN_VIDEO_PREVIEW_URL,
            COLUMN_VIDEO_URL
        )
    }

    override fun getAll(): List<PostInfo> {
        val posts = mutableListOf<PostInfo>()
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

    override fun save(postInfo: PostInfo): PostInfo {
        val values = ContentValues().apply {
            if (postInfo.id != 0L) {
                put(PostColumns.COLUMN_ID, postInfo.id)
            }
            put(PostColumns.COLUMN_AUTHOR, postInfo.authorName)
            put(PostColumns.COLUMN_CONTENT, postInfo.content)
            put(PostColumns.COLUMN_IS_LIKED, postInfo.isLiked)
            put(PostColumns.COLUMN_LIKES_COUNT, postInfo.likesCount)
            put(PostColumns.COLUMN_SHARED_COUNT, postInfo.sharedCount)
            put(PostColumns.COLUMN_VIEWS_COUNT, postInfo.viewsCount)
            put(PostColumns.COLUMN_DATE, postInfo.date)
            put(PostColumns.COLUMN_LINK_PART, postInfo.linkPart)
            put(PostColumns.COLUMN_VIDEO_PREVIEW_URL, postInfo.videoPreviewUrl)
            put(PostColumns.COLUMN_VIDEO_URL, postInfo.videoUrl)
        }
        val id = db.replace(PostColumns.TABLE, null, values)
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        ).use {
            it.moveToNext()
            return map(it)
        }
    }

    override fun likeById(id: Long) {
        db.execSQL(
            """
                UPDATE posts SET
                likesCount = likesCount + CASE WHEN isLiked THEN -1 ELSE 1 END,
                isLiked = CASE WHEN isLIKED THEN 0 ELSE 1 END
                WHERE id = ?;
            """.trimIndent(), arrayOf(id)
        )
    }

    override fun removeById(id: Long) {
        db.delete(
            PostColumns.TABLE,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf((id.toString()))
        )
    }

    override fun shareById(id: Long) {
        db.execSQL(
            """
                UPDATE posts SET
                sharedCount = sharedCount + 1
                WHERE id = ?;
            """.trimIndent(), arrayOf(id)
        )
    }

    private fun map(cursor: Cursor): PostInfo {
        with(cursor) {
            return PostInfo(
                id = getLong(getColumnIndexOrThrow(PostColumns.COLUMN_ID)),
                likesCount = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKES_COUNT)),
                sharedCount = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_SHARED_COUNT)),
                viewsCount = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_VIEWS_COUNT)),
                isLiked = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_IS_LIKED)) != 0,
                authorName = getString(getColumnIndexOrThrow(PostColumns.COLUMN_AUTHOR)),
                date = getString(getColumnIndexOrThrow(PostColumns.COLUMN_DATE)),
                content = getString(getColumnIndexOrThrow(PostColumns.COLUMN_CONTENT)),
                linkPart = getString(getColumnIndexOrThrow(PostColumns.COLUMN_LINK_PART)),
                videoPreviewUrl = getString(getColumnIndexOrThrow(PostColumns.COLUMN_VIDEO_PREVIEW_URL)),
                videoUrl = getString(getColumnIndexOrThrow(PostColumns.COLUMN_VIDEO_URL))
            )
        }
    }
}