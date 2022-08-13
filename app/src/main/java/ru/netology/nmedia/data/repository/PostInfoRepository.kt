package ru.netology.nmedia.data.repository

import androidx.lifecycle.LiveData
import ru.netology.nmedia.domain.model.PostInfo

interface PostInfoRepository {
    val data: LiveData<List<PostInfo>>
    suspend fun getAll()
    suspend fun save(post: PostInfo)
    suspend fun removeById(id: Long)
    suspend fun likeById(id: Long)
    suspend fun dislikeById(id:Long)
}
