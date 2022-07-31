package ru.netology.nmedia.ui.posts

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import ru.netology.nmedia.data.model.PostInfo
import ru.netology.nmedia.ui.posts.model.PostInfoUi
import ru.netology.nmedia.data.repository.PostRepository
import ru.netology.nmedia.data.repository.PostRepositoryEnqueueImpl
import ru.netology.nmedia.data.repository.PostRepositoryRetrofitImpl
import ru.netology.nmedia.ui.posts.mapper.UiMapper
import ru.netology.nmedia.util.SingleLiveEvent
import kotlin.concurrent.thread


class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val postRepository: PostRepository = PostRepositoryRetrofitImpl()
    private val data: MutableLiveData<List<PostInfo>> =
        MutableLiveData(emptyList()) //postRepository.getPostsData()
    val error: SingleLiveEvent<String> = SingleLiveEvent()
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

                override fun onError(e: Throwable) {
                    error.postValue(e.message)
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

                override fun onError(e: Throwable) {
                    error.postValue(e.message)
                    Log.d("likeByIDAsync ${requestType}", "ERROR")
                }
            })

    }

    fun onShareButtonClicked(postInfoUi: PostInfoUi) {
        val posts = data.value?.toMutableList() ?: return
        val index = posts.indexOfFirst { it.id == postInfoUi.id }
        if (index == -1) return
        posts[index] = posts[index].copy(sharedCount = posts[index].sharedCount + 1)
            postRepository.updatePostsData(
                posts[index],
                object : PostRepository.Callback<Unit> {
                    override fun onSuccess(result: Unit) {
                        Log.d("Share post", "SUCCESS")
                        loadPosts()
                    }

                    override fun onError(e: Throwable) {
                        error.postValue(e.message)
                        Log.d("Share post", "ERROR")
                    }
                }
            )
    }

    fun onRemoveMenuItemClicked(postInfoUi: PostInfoUi) {
        postRepository.removeByIdAsync(
            postInfoUi.id,
            object : PostRepository.Callback<Unit> {
                override fun onSuccess(result: Unit) {
                    Log.d("removeByIdAsync", "SUCCESS")
                    loadPosts()
                }

                override fun onError(e: Throwable) {
                    error.postValue(e.message)
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
            authorName = "Нетология. Университет интернет-профессий",
            date = "21 мая 18:36",
            content = postText,
            linkPart = "",
            authorAvatar = null,
            attachment = null
        )
        postRepository.updatePostsData(
            post,
            object : PostRepository.Callback<Unit> {
                override fun onSuccess(result: Unit) {
                    Log.d("Add post", "SUCCESS")
                    loadPosts()
                }

                override fun onError(e: Throwable) {
                    error.postValue(e.message)
                    Log.d("Add post", "ERROR")
                }
            })

    }

    private fun editPost(postText: String, postInfoUi: PostInfoUi) {
        val posts = data.value?.toMutableList() ?: return
        val postId = postInfoUi.id
        val index = posts.indexOfFirst { it.id == postId }
        if (index != -1) {
            posts[index] = posts[index].copy(content = postText)
            postRepository.updatePostsData(
                posts[index],
                object : PostRepository.Callback<Unit> {
                    override fun onSuccess(result: Unit) {
                        Log.d("Edit post", "SUCCESS")
                        loadPosts()
                    }

                    override fun onError(e: Throwable) {
                        Log.d("Edit post", "ERROR")
                    }
                })
        }
    }

}