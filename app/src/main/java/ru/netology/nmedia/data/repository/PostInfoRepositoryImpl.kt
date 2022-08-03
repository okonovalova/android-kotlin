package ru.netology.nmedia.data.repository

import androidx.lifecycle.map
import okio.IOException
import ru.netology.nmedia.data.api.*
import ru.netology.nmedia.data.api.PostsApi
import ru.netology.nmedia.data.room.PostDao
import ru.netology.nmedia.domain.model.PostInfo

class PostInfoRepositoryImpl(private val dao: PostDao) : PostInfoRepository {
    override val data = dao.getAll().map { list ->
        list.map {
            PostInfoMapper.mapDbToDomain(it)
        }
    }

    override suspend fun getAll() {
        try {
            val response = PostsApi.retrofitService.getAll()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(body.map {
                PostInfoMapper.mapDataToDomain(it)
            }.map {
                PostInfoMapper.mapDomainToDb(it)
            })
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun save(post: PostInfo) {
        try {
            val response = PostsApi.retrofitService.save(
                PostInfoMapper.mapDomainToData(post)
            )
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            val bodyToDomain = PostInfoMapper.mapDataToDomain(body)
            dao.insert(PostInfoMapper.mapDomainToDb(bodyToDomain))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun removeById(id: Long) {
        try {
            val response = PostsApi.retrofitService.removeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            dao.removeById(id)
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun likeById(id: Long) {
        try {
            val response = PostsApi.retrofitService.likeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            val bodyToDomain = PostInfoMapper.mapDataToDomain(body)
            dao.insert(PostInfoMapper.mapDomainToDb(bodyToDomain))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

}