package ru.netology.nmedia.data.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.data.api.DataResult
import ru.netology.nmedia.domain.model.PostInfo


interface PostInfoRepository {
    val data: Flow<List<PostInfo>>
    suspend fun getAll(): Flow<DataResult<List<PostInfo>>>
    suspend fun save(post: PostInfo): Flow<DataResult<PostInfo>>
    suspend fun removeById(id: Long): Flow<DataResult<Unit>>
    suspend fun likeById(id: Long): Flow<DataResult<PostInfo>>
    suspend fun dislikeById(id: Long): Flow<DataResult<PostInfo>>
    fun getNewerCount(id: Long): Flow<Int>
    suspend fun updateHiddenPosts(): Flow<DataResult<Unit>>
}
