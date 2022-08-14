package ru.netology.nmedia.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.data.model.PostInfoEntity

@Dao
interface PostDao {
    @Query("SELECT COUNT(*) == 0 FROM PostInfoEntity")
    suspend fun isEmpty(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: PostInfoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(posts: List<PostInfoEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnore(posts: List<PostInfoEntity>)

    @Query("DELETE FROM PostInfoEntity WHERE id = :id")
    suspend fun removeById(id: Long)

    @Query("SELECT * FROM PostInfoEntity WHERE isHidden = 1 ORDER BY id DESC")
    fun getAllHiddenPosts(): List<PostInfoEntity>

    @Query("SELECT * FROM PostInfoEntity WHERE isHidden = 0 ORDER BY id DESC")
    fun getAllVisiblePosts(): Flow<List<PostInfoEntity>>
}
