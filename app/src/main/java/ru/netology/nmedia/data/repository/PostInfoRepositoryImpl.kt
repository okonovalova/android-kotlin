package ru.netology.nmedia.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import ru.netology.nmedia.data.api.*
import ru.netology.nmedia.data.api.PostsApi
import ru.netology.nmedia.data.room.PostDao
import ru.netology.nmedia.domain.model.PostInfo

class PostInfoRepositoryImpl(private val dao: PostDao) : PostInfoRepository {
    override val data = dao.getAllVisiblePosts().map { list ->
        list.map {
            PostInfoMapper.mapDbToDomain(it)
        }
    }.flowOn(Dispatchers.Default)

    override suspend fun getAll(): Flow<DataResult<List<PostInfo>>> {
        return flow {
            val response = PostsApi.retrofitService.getAll()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            val bodyToDomain = body.map {
                PostInfoMapper.mapDataToDomain(it)
            }
            dao.insert(bodyToDomain.map {
                PostInfoMapper.mapDomainToDb(it)
            })
            emit(DataResult.success(bodyToDomain))
        }
            .catch { e ->
                emit(DataResult.error(AppError.from(e)))
            }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun save(post: PostInfo): Flow<DataResult<PostInfo>> {
        return flow {
            val response = PostsApi.retrofitService.save(
                PostInfoMapper.mapDomainToData(post)
            )
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            val bodyToDomain = PostInfoMapper.mapDataToDomain(body)
            dao.insert(PostInfoMapper.mapDomainToDb(bodyToDomain))
            emit(DataResult.success(bodyToDomain))
        }
            .catch { e ->
                emit(DataResult.error(AppError.from(e)))
            }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun removeById(id: Long): Flow<DataResult<Unit>> {
        return flow {
            val response = PostsApi.retrofitService.removeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            dao.removeById(id)
            emit(DataResult.success(Unit))
        }
            .catch { e ->
                emit(DataResult.error(AppError.from(e)))
            }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun likeById(id: Long): Flow<DataResult<PostInfo>> {
        return flow {
            val response = PostsApi.retrofitService.likeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            val bodyToDomain = PostInfoMapper.mapDataToDomain(body)
            dao.insert(PostInfoMapper.mapDomainToDb(bodyToDomain))
            emit(DataResult.success(bodyToDomain))
        }
            .catch { e ->
                emit(DataResult.error(AppError.from(e)))
            }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun dislikeById(id: Long): Flow<DataResult<PostInfo>> {
        return flow {
            val response = PostsApi.retrofitService.dislikeById(id)
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            val bodyToDomain = PostInfoMapper.mapDataToDomain(body)
            dao.insert(PostInfoMapper.mapDomainToDb(bodyToDomain))
            emit(DataResult.success(bodyToDomain))
        }
            .catch { e ->
                emit(DataResult.error(AppError.from(e)))
            }
            .flowOn(Dispatchers.IO)
    }

    override fun getNewerCount(id: Long): Flow<Int> = flow {
        while (true) {
            delay(10_000L)
            val response = PostsApi.retrofitService.getNewer(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            val bodyToDomain = body.map {
                PostInfoMapper.mapDataToDomain(it)
            }
            dao.insertOrIgnore(bodyToDomain.map {
                PostInfoMapper.mapDomainToDb(it).copy(isHidden = true)
            })
            emit(body.size)
        }
    }
        .flowOn(Dispatchers.IO)

    override suspend fun updateHiddenPosts(): Flow<DataResult<Unit>> {
        return flow {
            val hiddenPosts = dao.getAllHiddenPosts().map {
                it.copy(isHidden = false)
            }
            dao.insert(hiddenPosts)
            emit(DataResult.success(Unit))
        }
            .catch { e ->
                emit(DataResult.error(AppError.from(e)))
            }
            .flowOn(Dispatchers.IO)
    }
}