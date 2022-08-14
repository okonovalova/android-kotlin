package ru.netology.nmedia.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.domain.model.PostInfo
import ru.netology.nmedia.data.model.PostInfoData
import java.io.IOException
import java.util.concurrent.TimeUnit


class PostRepositoryEnqueueImpl : PostRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<List<PostInfoData>>() {}

    companion object {
        private const val BASE_URL = "http://10.0.2.2:9999"
        private val jsonType = "application/json".toMediaType()
    }

    override fun getPostsData(): List<PostInfo> {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/posts")
            .build()

        return client.newCall(request)
            .execute()
            .let { it.body?.string() ?: throw RuntimeException("body is null") }
            .let {
                gson.fromJson<List<PostInfoData>>(it, typeToken.type)
                    .map(PostInfoMapper::mapDataToDomain)
            }

    }

    override fun getPostsDataAsync(callback: PostRepository.Callback<List<PostInfo>>) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/posts")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    val body = response.body?.string() ?: throw RuntimeException("body is null")
                    try {
                        callback.onSuccess(gson.fromJson<List<PostInfoData>>(body, typeToken.type)
                            .map (PostInfoMapper::mapDataToDomain))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }

    override fun likeByIDAsync(
        id: Long,
        isLiked: Boolean,
        callback: PostRepository.Callback<Unit>
    ) {
        val enqueueCallback = object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    try {
                        callback.onSuccess(Unit)

                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            }

        if (isLiked) {
            val request: Request = Request.Builder()
                .delete()
                .url("${BASE_URL}/api/posts/$id/likes")
                .build()

            client.newCall(request)
                .enqueue(enqueueCallback)
        } else {
            val request: Request = Request.Builder()
                .post("".toRequestBody())
                .url("${BASE_URL}/api/posts/$id/likes")
                .build()

            client.newCall(request)
                .enqueue(enqueueCallback)
        }
    }

    override fun removeByIdAsync(id: Long, callback: PostRepository.Callback<Unit>) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/posts/$id")
            .build()

        client.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    try {
                        callback.onSuccess(Unit)

                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
    }


    override fun likeByID(id: Long, isLiked: Boolean) {
        if (isLiked) {
            val request: Request = Request.Builder()
                .delete()
                .url("${BASE_URL}/api/posts/$id/likes")
                .build()

            client.newCall(request)
                .execute()
                .close()
        } else {
            val request: Request = Request.Builder()
                .post("".toRequestBody())
                .url("${BASE_URL}/api/posts/$id/likes")
                .build()

            client.newCall(request)
                .execute()
                .close()
        }
    }

    override fun updatePostsData(post: PostInfo, callback: PostRepository.Callback<Unit>) {
        val postInfoData = PostInfoMapper.mapDomainToData(post)
        val request: Request = Request.Builder()
            .post(gson.toJson(postInfoData).toRequestBody(jsonType))
            .url("${BASE_URL}/api/posts")
            .build()

        client.newCall(request)
            .execute()
            .close()
    }

    override fun removeById(id: Long) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/posts/$id")
            .build()

        client.newCall(request)
            .execute()
            .close()
    }
}