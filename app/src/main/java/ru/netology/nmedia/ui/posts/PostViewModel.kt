package ru.netology.nmedia.ui.posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import ru.netology.nmedia.data.model.PostInfo
import ru.netology.nmedia.ui.posts.model.PostInfoUi
import ru.netology.nmedia.data.repository.PostRepository
import ru.netology.nmedia.data.repository.PostRepositoryImpl
import ru.netology.nmedia.ui.posts.mapper.UiMapper

class PostViewModel : ViewModel() {
    private val postRepository: PostRepository = PostRepositoryImpl()
    private val data: LiveData<List<PostInfo>> = postRepository.getPostsData()

    val uiData: LiveData<List<PostInfoUi>> = data
        .map {
            it.map {
                UiMapper.mapPostInfoToPostInfoUi(it)
            }
        }

    fun onLikeButtonClicked(postInfoUi: PostInfoUi) {
        val posts = data.value?.toMutableList() ?: return
        val index = posts.indexOfFirst { it.id == postInfoUi.id }
        if (index == -1) return
        val newLikesCount =
            if (posts[index].isLiked) posts[index].likesCount - 1 else posts[index].likesCount + 1
        posts[index] = posts[index].copy(
            likesCount = newLikesCount,
            isLiked = !postInfoUi.isLiked,
        )
        postRepository.updatePostsData(posts)
    }

    fun onShareButtonClicked(postInfoUi: PostInfoUi) {
        val posts = data.value?.toMutableList() ?: return
        val index = posts.indexOfFirst { it.id == postInfoUi.id }
        if (index == -1) return
        posts[index] = posts[index].copy(sharedCount = posts[index].sharedCount + 1)
        postRepository.updatePostsData(posts)
    }

    fun onRemoveMenuItemClicked(postInfoUi: PostInfoUi) {
        val posts = data.value?.toMutableList() ?: return
        val index = posts.indexOfFirst { it.id == postInfoUi.id }
        if (index == -1) return
        posts.remove(posts[index])
        postRepository.updatePostsData(posts)
    }

    fun onSaveButtonClicked(postText: String, postInfoUi: PostInfoUi?) {
        if (postInfoUi == null) addPost(postText) else editPost(postText, postInfoUi)
    }

    private fun addPost(postText: String) {
        val posts = data.value?.toMutableList() ?: return
        val lastPostId = posts.lastOrNull()?.id ?: 0
        val post: PostInfo = PostInfo(
            id = lastPostId + 1,
            likesCount = 0,
            sharedCount = 0,
            viewsCount = 0,
            isLiked = false,
            authorName = "Нетология. Университет интернет-профессий",
            date = "21 мая 18:36",
            content = postText,
            linkPart = ""
        )
        posts.add(post)
        postRepository.updatePostsData(posts)
    }

    private fun editPost(postText: String, postInfoUi: PostInfoUi) {
        val posts = data.value?.toMutableList() ?: return
        val postId = postInfoUi.id
        val index = posts.indexOfFirst { it.id == postId }
        if (index != -1) {
            posts[index] = posts[index].copy(content = postText)
            postRepository.updatePostsData(posts)
        }
    }

}