package ru.netology.nmedia.data.repository

import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.data.api.PostsApi
import ru.netology.nmedia.data.model.PostInfo
import ru.netology.nmedia.data.model.PostInfoData
import java.lang.RuntimeException

class PostRepositoryRetrofitImpl : PostRepository {
    override fun getPostsDataAsync(callback: PostRepository.Callback<List<PostInfo>>) {
        PostsApi.retrofitService.getAll().enqueue(object : Callback<List<PostInfoData>> {
            override fun onResponse(
                call: Call<List<PostInfoData>>,
                response: Response<List<PostInfoData>>
            ) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }

                if (response.body() == null) throw RuntimeException("body is null")
                callback.onSuccess(response.body()!!.map(PostInfoMapper::mapDataToDomain))
            }

            override fun onFailure(call: Call<List<PostInfoData>>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    override fun likeByIDAsync(
        id: Long,
        isLiked: Boolean,
        callback: PostRepository.Callback<Unit>
    ) {
        val enqueueCallback = object : Callback<PostInfoData> {
            override fun onResponse(
                call: Call<PostInfoData>,
                response: Response<PostInfoData>
            ) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }

                if (response.body() == null) throw RuntimeException("body is null")
                callback.onSuccess(Unit)
            }

            override fun onFailure(call: Call<PostInfoData>, t: Throwable) {
                callback.onError(t)
            }
        }

        if (isLiked) {
            PostsApi.retrofitService.dislikeById(id)
                .enqueue(enqueueCallback)
        } else {
            PostsApi.retrofitService.likeById(id)
                .enqueue(enqueueCallback)
        }

    }

    override fun removeByIdAsync(id: Long, callback: PostRepository.Callback<Unit>) {
        PostsApi.retrofitService.removeById(id).enqueue(object : Callback<Unit> {
            override fun onResponse(
                call: Call<Unit>,
                response: Response<Unit>
            ) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }

                if (response.body() == null) throw RuntimeException("body is null")
                callback.onSuccess(Unit)
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    override fun updatePostsData(post: PostInfo, callback: PostRepository.Callback<Unit>) {
        val postInfoData = PostInfoMapper.mapDomainToData(post)
        PostsApi.retrofitService.save(postInfoData).enqueue(object : Callback<PostInfoData> {
            override fun onResponse(
                call: Call<PostInfoData>,
                response: Response<PostInfoData>
            ) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }

                if (response.body() == null) throw RuntimeException("body is null")
                callback.onSuccess(Unit)
            }

            override fun onFailure(call: Call<PostInfoData>, t: Throwable) {
                callback.onError(t)
            }
        })
    }
}