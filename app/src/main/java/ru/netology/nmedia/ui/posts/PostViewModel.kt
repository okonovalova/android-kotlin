package ru.netology.nmedia.ui.posts

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import ru.netology.nmedia.data.db.AppDb
import ru.netology.nmedia.data.model.PostInfo
import ru.netology.nmedia.ui.posts.model.PostInfoUi
import ru.netology.nmedia.data.repository.PostRepository
import ru.netology.nmedia.data.repository.PostRepositoryEnqueueImpl
import ru.netology.nmedia.data.repository.PostRepositoryNetworkImpl
import ru.netology.nmedia.data.repository.PostRepositorySQLiteImpl
import ru.netology.nmedia.ui.posts.mapper.UiMapper
import java.io.IOException
import kotlin.concurrent.thread


class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val postRepository: PostRepository = PostRepositoryEnqueueImpl()
    private val data: MutableLiveData<List<PostInfo>> =
        MutableLiveData(emptyList()) //postRepository.getPostsData()

    private var idPostUiForDetail: Long = -1

    val uiData: LiveData<List<PostInfoUi>> = data
        .map {
            it.map {
                UiMapper.mapPostInfoToPostInfoUi(it)
            }
        }

    init {
         loadPosts()
    }

    fun loadPosts() {
       postRepository.getPostsDataAsync(
            object : PostRepository.Callback<List<PostInfo>> {
                override fun onSuccess(posts: List<PostInfo>) {
                    data.postValue(posts)
                }

                override fun onError(e: Exception) {
                    Log.d("getPostsDataAsync", "ERROR")
                }
            }
        )
    }

    val postUiForDetail: MutableLiveData<PostInfoUi?> = uiData
        .map {
            it.firstOrNull { it.id == idPostUiForDetail }
        } as MutableLiveData<PostInfoUi?>

    fun initPostUiForDetail(id: Long) {
        idPostUiForDetail = id
        val post = uiData.value?.firstOrNull { it.id == idPostUiForDetail }
        postUiForDetail.value = post
    }

    fun onLikeButtonClicked(postInfoUi: PostInfoUi) {
        val requestType = if (postInfoUi.isLiked) "DELETE" else "POST"
        postRepository.likeByIDAsync(
            postInfoUi.id,
            postInfoUi.isLiked,
            object : PostRepository.Callback<Unit> {
                override fun onSuccess(result: Unit) {
                    Log.d("likeByIDAsync ${requestType}", "SUCCESS")
                    loadPosts()
                }

                override fun onError(e: Exception) {
                    Log.d("likeByIDAsync ${requestType}", "ERROR")
                }
            })

    }

    fun onShareButtonClicked(postInfoUi: PostInfoUi) {
        val posts = data.value?.toMutableList() ?: return
        val index = posts.indexOfFirst { it.id == postInfoUi.id }
        if (index == -1) return
        posts[index] = posts[index].copy(sharedCount = posts[index].sharedCount + 1)
        thread {
            postRepository.updatePostsData(posts[index])
            loadPosts()
        }
    }

    fun onRemoveMenuItemClicked(postInfoUi: PostInfoUi) {
        postRepository.removeByIdAsync(
            postInfoUi.id,
            object : PostRepository.Callback<Unit> {
                override fun onSuccess(result: Unit) {
                    Log.d("removeByIdAsync", "SUCCESS")
                    loadPosts()
                }

                override fun onError(e: Exception) {
                    Log.d("removeByIdAsync", "ERROR")
                }
            }
        )


    }

    fun onSaveButtonClicked(postText: String, postInfoUi: PostInfoUi?) {
        if (postInfoUi == null) addPost(postText) else editPost(postText, postInfoUi)
    }

    private fun addPost(postText: String) {
        val post: PostInfo = PostInfo(
            id = 0,
            likesCount = 0,
            sharedCount = 0,
            viewsCount = 0,
            isLiked = false,
            authorName = "??????????????????. ?????????????????????? ????????????????-??????????????????",
            date = "21 ?????? 18:36",
            content = postText,
            linkPart = ""
        )

        thread {
            postRepository.updatePostsData(post)
            loadPosts()
        }
    }

    private fun editPost(postText: String, postInfoUi: PostInfoUi) {
        val posts = data.value?.toMutableList() ?: return
        val postId = postInfoUi.id
        val index = posts.indexOfFirst { it.id == postId }
        if (index != -1) {
            posts[index] = posts[index].copy(content = postText)
            thread {
                postRepository.updatePostsData(posts[index])
                loadPosts()
            }
        }
    }

}