package ru.netology.nmedia.ui.posts

import android.app.Application
import androidx.lifecycle.*
import ru.netology.nmedia.data.db.AppDb
import ru.netology.nmedia.data.model.PostInfo
import ru.netology.nmedia.ui.posts.model.PostInfoUi
import ru.netology.nmedia.data.repository.PostRepository
import ru.netology.nmedia.data.repository.PostRepositorySQLiteImpl
import ru.netology.nmedia.ui.posts.mapper.UiMapper

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val postRepository: PostRepository = PostRepositorySQLiteImpl(
        AppDb.getInstance(application).postDao
    )
    private val data: LiveData<List<PostInfo>> = postRepository.getPostsData()
    private var idPostUiForDetail: Long = -1

    val uiData: LiveData<List<PostInfoUi>> = data
        .map {
            it.map {
                UiMapper.mapPostInfoToPostInfoUi(it)
            }
        }

    val postUiForDetail: LiveData<PostInfoUi?> = uiData
        .map {
            it.firstOrNull { it.id == idPostUiForDetail }
        }

    fun initPostUiForDetail(id: Long) {
        idPostUiForDetail = id
    }

    fun onLikeButtonClicked(postInfoUi: PostInfoUi) {
        postRepository.likeById(postInfoUi.id)
    }

    fun onShareButtonClicked(postInfoUi: PostInfoUi) {
        val posts = data.value?.toMutableList() ?: return
        val index = posts.indexOfFirst { it.id == postInfoUi.id }
        if (index == -1) return
        posts[index] = posts[index].copy(sharedCount = posts[index].sharedCount + 1)
        postRepository.updatePostsData(posts[index])
    }

    fun onRemoveMenuItemClicked(postInfoUi: PostInfoUi) {
        postRepository.removeById(postInfoUi.id)
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
            linkPart = ""
        )
        postRepository.updatePostsData(post)
    }

    private fun editPost(postText: String, postInfoUi: PostInfoUi) {
        val posts = data.value?.toMutableList() ?: return
        val postId = postInfoUi.id
        val index = posts.indexOfFirst { it.id == postId }
        if (index != -1) {
            posts[index] = posts[index].copy(content = postText)
            postRepository.updatePostsData(posts[index])
        }
    }

}