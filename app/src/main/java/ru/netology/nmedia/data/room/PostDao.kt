package ru.netology.nmedia.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.netology.nmedia.data.model.PostInfoEntity

@Dao
interface PostDao {
    @Query("SELECT * FROM PostInfoEntity ORDER BY id DESC")
    fun getAll(): LiveData<List<PostInfoEntity>>

    @Query("SELECT COUNT(*) == 0 FROM PostInfoEntity")
    suspend fun isEmpty(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: PostInfoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(posts: List<PostInfoEntity>)

    @Query("DELETE FROM PostInfoEntity WHERE id = :id")
    suspend fun removeById(id: Long)
}
