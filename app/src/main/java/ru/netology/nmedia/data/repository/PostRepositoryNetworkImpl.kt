package ru.netology.nmedia.data.repository
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.data.model.PostInfo
import ru.netology.nmedia.data.model.PostInfoData
import ru.netology.nmedia.ui.posts.mapper.UiMapper
import java.util.concurrent.TimeUnit


class PostRepositoryNetworkImpl: PostRepository {
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
                    .map {
                            PostInfo(
                                id = it.id,
                                likesCount = it.likes,
                                sharedCount = 0,
                                viewsCount = 0,
                                isLiked = it.likedByMe,
                                authorName = it.author,
                                date = "24 июня",
                                content = it.content,
                                linkPart = null,
                                videoPreviewUrl = null,
                                videoUrl = null
                            )
                    }
            }

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

    override fun updatePostsData(post: PostInfo) {
        val postInfoData = PostInfoData(
            post.id,
            post.authorName,
            post.content,
            0,
            post.isLiked,
            post.likesCount
        )
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